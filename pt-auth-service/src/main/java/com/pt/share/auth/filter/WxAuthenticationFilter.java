package com.pt.share.auth.filter;

import com.google.gson.Gson;
import com.pt.share.auth.vo.WxAuthInfo;
import com.pt.share.auth.vo.WxAuthenticationToken;
import com.pt.share.core.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName WxAuthenticationFilter
 * @Description 微信认证过滤器
 * 过滤微信小程序请求
 * 若请求体信息为空，则生成未登录token并返回
 * @Author potato
 * @Date 2020/12/15 下午11:18
 **/
@Slf4j
public class WxAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Gson gson = new Gson();

    public WxAuthenticationFilter() {
        super(new AntPathRequestMatcher("/oauth/wx", "POST"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        String requestBodyString = HttpRequestUtil.readBodyToString(request);
        log.info("读取请求体中的字符串并转换:{}",requestBodyString);
        WxAuthInfo authInfo = gson.fromJson(requestBodyString, WxAuthInfo.class);
        log.info("gson将authInfo字符串转换为WxAuthInfo：{}",authInfo);

        //若获取的信息为null，则转化为空字符串
        if (null == authInfo.getLoginCode() ) {
            log.info("请求体数据为空");
            authInfo.setLoginCode("");
        }
        if (null == authInfo.getUserInfo()) {
            authInfo.setUserInfo("");
        }
        if (null == authInfo.getSignature()) {
            authInfo.setSignature("");
        }
        log.info("loginCode转换:{}",authInfo.getLoginCode().trim());

        authInfo.setLoginCode(authInfo.getLoginCode().trim());
        authInfo.setUserInfo(authInfo.getUserInfo().trim());
        authInfo.setSignature(authInfo.getSignature().trim());

        // 创建未通过验证的token
        WxAuthenticationToken wxAuthenticationToken = new WxAuthenticationToken(authInfo);
        wxAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));

        // 调用ProviderManager并进行验证
        return this.getAuthenticationManager().authenticate(wxAuthenticationToken);
    }

}
