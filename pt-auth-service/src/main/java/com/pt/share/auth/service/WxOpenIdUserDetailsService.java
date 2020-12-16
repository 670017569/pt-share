package com.pt.share.auth.service;

import cn.hutool.core.lang.Snowflake;
import com.google.gson.Gson;
import com.pt.share.auth.config.WxProperties;
import com.pt.share.auth.entity.SecurityUser;
import com.pt.share.auth.vo.WxUserSession;
import com.pt.share.entity.Role;
import com.pt.share.entity.User;
import com.pt.share.entity.WxUserInfo;
import com.pt.share.mapper.RoleMapper;
import com.pt.share.mapper.UserMapper;
import com.pt.share.mapper.WxUserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @ClassName WxOpenIdUserDetailsService
 * @Description 微信用户登录用户详情类实现
 * @Author potato
 * @Date 2020/12/15 下午6:00
 **/
@Service
@Slf4j
public class WxOpenIdUserDetailsService {

    @Resource
    private WxProperties properties;

    @Resource
    private UserMapper userMapper;

    @Resource
    private Snowflake snowflake;

    @Resource
    protected RestTemplate restTemplate;

    @Resource
    private Gson gson;

    @Resource
    private WxUserInfoMapper wxUserInfoMapper;

    @Resource
    private RoleMapper roleMapper;

    /**
     * 传入wxopenid, 用户存在则返回信息
     * @return {@link UserDetails}
     * @param wxOpenid 微信openid
     */
    public UserDetails loadUserByWxOpenid(String wxOpenid) {
        log.info("微信小程序用户登录开始: 传入wxOpenid:{}",wxOpenid);

        Example example = new Example(User.class);
        example.and()
                .andEqualTo("wxOpenid",wxOpenid)
                .andEqualTo("deleted","0");
        User user = userMapper.selectOneByExample(example);
        if (null == user){
            return null;
        }
        List<Role> roles = roleMapper.selectByUid(user.getId());
        user.setRoles(roles);
        log.info("查询到用户信息并返回:{}",user);
        return gson.fromJson(gson.toJson(user),SecurityUser.class);
    }

    /**
     * 用户不存在, 创建用户并返回{@link User}
     * 在这里未进行小程序用户的角色赋予
     * 将在job中进行
     * @return {@link User}
     * @param wxUserInfo {@link com.pt.share.entity.WxUserInfo}
     * @param wxOpenid 微信openid
     */
    public UserDetails createUser(WxUserInfo wxUserInfo, String wxOpenid) {
        Long id = snowflake.nextId();
        try {
            log.info("微信小程序用户注册 ==> 开始 生成uid:{}",id);
            User user = User.builder()
                    .id(id)
                    .wxOpenid(wxOpenid)
                    .build();
            int res = userMapper.insertSelective(user);
            if (1 == res) {
                log.info("用户表数据插入成功");
                wxUserInfo.setUid(id);
                log.info("生成微信用户信息：{}",wxUserInfo);
                wxUserInfoMapper.insertSelective(wxUserInfo);
                log.info("微信用户信息插入成功");
            }
        }catch (DuplicateKeyException e){
            log.info("用户信息插入失败:{}",e.toString());
            return null;
        }
        User user = userMapper.selectOneByUid(id);
        SecurityUser securityUser = gson.fromJson(gson.toJson(user), SecurityUser.class);
        log.info("微信小程序用户注册 <== 成功，返回用户信息：{}",securityUser);
        return securityUser;
    }

    /**
     * 创建微信用户会话
     * @param code
     * @return
     */
    public WxUserSession openSession(String code) throws IOException {
        log.info("创建微信用户会话 ==> 开始");
        String url = String.format(properties.getApi(), properties.getAppId(), properties.getAppSecret(),code);
        log.info("生成用户会话url：{}",url);
        log.info("开始请求微信后台接口");
        String res = restTemplate.getForObject(url, String.class);
        log.info("请求微信后台服务器 <== 成功：{}",res);
        return gson.fromJson(res, WxUserSession.class);
    }

    /**
     * 解码验证数据完整性
     * @param userInfo
     * @param sessionKey
     * @param signature
     * @return
     *
     */
    public boolean userInfoValidate(String userInfo, String sessionKey, String signature)throws UnauthorizedUserException {
        String signatureBytes = DigestUtils.sha1Hex(userInfo + sessionKey);
        return signature.equals(signatureBytes);
    }

}
