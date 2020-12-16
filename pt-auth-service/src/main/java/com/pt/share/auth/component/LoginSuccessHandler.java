package com.pt.share.auth.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pt.share.auth.config.Oauth2ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @ClassName LoginSuccessHandler
 * @Description 微信认证集成提交
 * @Author potato
 * @Date 2020/12/15 下午10:18
 **/
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String CLIENT_ID = Oauth2ServerConfig.CLIENT_ID_WECHAT_MINIPROGRAM;
    private static final String GRANT_TYPE = Oauth2ServerConfig.GRANT_TYPE_WX;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 读取在内存中配置的client信息<br/>
     * 详见{@link Oauth2ServerConfig}
     */
    @Resource
    private ClientDetailsService clientDetailsService;

    /**
     * 负责创建AccessToken<br/>
     * 实现类默认为{@link DefaultTokenServices}<br/>
     */
    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            // 从内存中获取client信息
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(CLIENT_ID);
            // 创建token
            TokenRequest tokenRequest = new TokenRequest(new HashMap<>(),
                    clientDetails.getClientId(),
                    clientDetails.getScope(),
                    GRANT_TYPE);
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
            // rest风格返回OAuth2AccessToken
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.append(objectMapper.writeValueAsString(oAuth2AccessToken));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
    }

}
