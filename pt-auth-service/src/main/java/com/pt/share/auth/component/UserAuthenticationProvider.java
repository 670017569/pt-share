package com.pt.share.auth.component;

import com.pt.share.auth.entity.SecurityUser;
import com.pt.share.auth.service.UserDetailsServiceImpl;
import com.pt.share.core.constant.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName UserAuthenticationProvider
 * @Description 用户名密码认证重写
 * @Author potato
 * @Date 2020/12/15 下午10:18
 **/
@Component
@Slf4j
public class UserAuthenticationProvider implements AuthenticationProvider {

    public UserAuthenticationProvider(){}

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        SecurityUser currentSecurityUser = userDetailsService.loadUserByUsername(username);
        if (currentSecurityUser.getUsername().equals(username) && passwordEncoder.encode(currentSecurityUser.getPassword()).equals(password)){
            authentication.setAuthenticated(true);
        }
        if (!currentSecurityUser.isAccountNonLocked() || !currentSecurityUser.isAccountNonExpired() || !currentSecurityUser.isCredentialsNonExpired()) {
            log.info("账号状态异常");
            throw new LockedException(Response.ACCOUNT_LOCKED.getMessage());
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
