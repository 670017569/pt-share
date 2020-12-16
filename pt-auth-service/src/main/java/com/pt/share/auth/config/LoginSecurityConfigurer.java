package com.pt.share.auth.config;

import com.pt.share.auth.component.LoginSuccessHandler;
import com.pt.share.auth.component.WxAuthenticationProvider;
import com.pt.share.auth.filter.WxAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName LoginSecurityConfigurer
 * @Description 组装自定义登陆功能
 * @Author potato
 * @Date 2020/12/15 下午10:18
 **/
@Component
public class LoginSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    @Resource
    private WxAuthenticationProvider wxAuthenticationProvider;

//    @Resource
//    private PhoneAuthenticationProvider phoneAuthenticationProvider;



    @Override
    public void configure(HttpSecurity http) {
        // 创建过滤器实例
        WxAuthenticationFilter wxAuthenticationFilter = new WxAuthenticationFilter();
        wxAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        wxAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
//
//        PhoneAuthenticationFilter phoneAuthenticationFilter = new PhoneAuthenticationFilter();
//        phoneAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
//        phoneAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        http.authenticationProvider(wxAuthenticationProvider).addFilterAfter(wxAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        http.authenticationProvider(phoneAuthenticationProvider).addFilterAfter(phoneAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
