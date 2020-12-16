package com.pt.share.auth.service;

import com.google.gson.Gson;
import com.pt.share.auth.entity.SecurityUser;
import com.pt.share.entity.User;
import com.pt.share.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserDetailServiceImpl
 * @Description 用户信息Service实现类
 * 实现UserDetailsService接口，通过username查询数据库中的信息并返回
 * @Author potato
 * @Date 2020/12/15 下午5:43
 **/
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private Gson gson;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("查询用户信息 ==> 开始:{}",username);
        User user = userMapper.selectByUsername(username);

        if (null == user){
            log.info("用户查询为空");
            throw new UsernameNotFoundException("用户名不存在");
        }
        log.info("查询用户信息 <== 结束{}",user);
        return gson.fromJson(gson.toJson(user), SecurityUser.class);
    }
}
