package com.pt.share.auth.component;

import com.google.gson.Gson;
import com.pt.share.auth.service.WxOpenIdUserDetailsService;
import com.pt.share.auth.vo.WxAuthInfo;
import com.pt.share.auth.vo.WxAuthenticationToken;
import com.pt.share.auth.vo.WxUserSession;
import com.pt.share.core.constant.Response;
import com.pt.share.core.exception.UnAuthorizedException;
import com.pt.share.entity.WxUserInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @ClassName WxAuthenticationProvider
 * @Description 微信认证封装重写
 * @Author potato
 * @Date 2020/12/15 下午10:18
 **/

@Slf4j
@Component
public class WxAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private Gson gson;

    @Resource
    private WxOpenIdUserDetailsService wxOpenIdUserDetailsService;

    /**
     * 微信小程序鉴权
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        WxAuthInfo info = (WxAuthInfo) authentication.getPrincipal();
        WxUserSession session;
        WxUserInfo wxUserInfo;
        try {
            //打开微信会话
            log.info("开始微信会话");
            session = wxOpenIdUserDetailsService.openSession(info.getLoginCode());
            log.info("微信会话打开成功:{}",session);
        }catch (RuntimeException e){
            log.info("微信会话打开失败:{}",e.toString());
            throw new UnAuthorizedException(Response.WX_SESSION_OPEN_FAIL);
        }

        try {
            log.info("用户信息完整性验证 ==> 开始");
            boolean isValidate = wxOpenIdUserDetailsService.userInfoValidate(info.getUserInfo(), session.getSessionKey(), info.getSignature());
            if (!isValidate) {
                log.info("用户信息完整性验证错误");
                throw new UnAuthorizedException(Response.WX_SESSION_INVALID);
            }
            log.info("用户信息完整性验证 <== 完成");
        }catch (Exception e){
            log.info("用户信息完整性错误:{}",e.toString());
            throw new UnAuthorizedException(Response.WX_SESSION_INVALID);
        }

        wxUserInfo = (gson.fromJson(info.getUserInfo(), WxUserInfo.class));
        log.info("gson解析微信用户信息:{}",wxUserInfo);

        UserDetails userDetails = wxOpenIdUserDetailsService.loadUserByWxOpenid(session.getOpenid());
        log.info("根据openid查询用户信息:{}",userDetails);


        if(null != userDetails){
            WxAuthenticationToken authenticationToken = new WxAuthenticationToken(userDetails, userDetails.getAuthorities());
            log.info("用户存在, 直接创建Token:{}",authenticationToken);
            return authenticationToken;
        }else {
            log.info("用户不存在, 创建用户再创建Token");
            userDetails = wxOpenIdUserDetailsService.createUser(wxUserInfo, session.getOpenid());
            WxAuthenticationToken authenticationToken = new WxAuthenticationToken(userDetails, userDetails.getAuthorities());
            log.info("用户创建成功,生成token：{}",authenticationToken);
            return authenticationToken;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WxAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
