package com.pt.share.auth.config;

import com.pt.share.auth.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName LoginSecurityConfigurer
 * @Description Oauth2认证服务器配置
 * 对认证管理器进行了重新组装
 * 组装了自定义认证方式
 * 包括微信小程序登录、手机号登录、用户名登录
 * @Author potato
 * @Date 2020/12/15 下午10:18
 **/
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    public static final String CLIENT_ID_WECHAT_MINIPROGRAM = "bmy_wechat_mp";
    public static final String GRANT_TYPE_WX = "wx";

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Resource
    private UserDetailsServiceImpl userDetailsService;


    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private ClientDetailsService clientDetailsService;

//    @Resource
//    private PhoneUserDetailService phoneUserDetailService;

    private TokenGranter tokenGranter;
    private AuthorizationCodeServices authorizationCodeServices;
    private OAuth2RequestFactory requestFactory;
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 在内存中内置client信息
        clients.inMemory()
                .withClient(CLIENT_ID_WECHAT_MINIPROGRAM)
//                .resourceIds(ResourceServerConfig.RESOURCE_ID)
                .authorizedGrantTypes("password", "client_credentials", "refresh_token", "wx", "phone")
                .scopes("bmy")
                .secret(passwordEncoder.encode("123456"));
    }

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("bmy-auth-token:");
//        redisTokenStore.setSerializationStrategy(new JacksonRedisTokenStoreSerializationStrategy());
        return redisTokenStore;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        authorizationServerTokenServices = endpoints.getTokenServices();
        authorizationCodeServices = endpoints.getAuthorizationCodeServices();
        requestFactory = endpoints.getOAuth2RequestFactory();
        endpoints
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore())
                .tokenGranter(this.tokenGranter())
                .authenticationManager(authenticationManager);

        DefaultTokenServices tokenService = new DefaultTokenServices();
        tokenService.setTokenStore(endpoints.getTokenStore());
        tokenService.setSupportRefreshToken(true);
        tokenService.setClientDetailsService(endpoints.getClientDetailsService());
        tokenService.setTokenEnhancer(endpoints.getTokenEnhancer());
        //token有效期 1小时
        tokenService.setAccessTokenValiditySeconds(3600);
        //token刷新有效期 15天
        tokenService.setRefreshTokenValiditySeconds(3600 * 12 * 15);
        tokenService.setReuseRefreshToken(true);
        endpoints.tokenServices(tokenService);

    }
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()") //url:/oauth/token_key,exposes public key for token verification if using JWT tokens
                .checkTokenAccess("permitAll()") //url:/oauth/check_token allow check token
                .allowFormAuthenticationForClients();
    }

    /**
     * 重新组装{@link TokenGranter}列表<br/>
     * 新旧授权方式都要创建<br/>
     */
    private List<TokenGranter> getDefaultTokenGranters() {
        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
        // 授权码模式
        tokenGranters.add(new AuthorizationCodeTokenGranter(
                authorizationServerTokenServices,
                authorizationCodeServices,
                clientDetailsService,
                requestFactory));
        // 刷新
        tokenGranters.add(new RefreshTokenGranter(
                authorizationServerTokenServices,
                clientDetailsService,
                requestFactory));
        // 隐式登陆
        tokenGranters.add(new ImplicitTokenGranter(
                authorizationServerTokenServices,
                clientDetailsService,
                requestFactory));
        // 客户端模式
        tokenGranters.add(new ClientCredentialsTokenGranter(
                authorizationServerTokenServices,
                clientDetailsService,
                requestFactory));
        // 密码模式
        tokenGranters.add(new ResourceOwnerPasswordTokenGranter(
                authenticationManager,
                authorizationServerTokenServices,
                clientDetailsService,
                requestFactory));
//        // 手机号登陆模式(自定义)
//        tokenGranters.add(new PhoneTokenGrantor(
//                authorizationServerTokenServices,
//                clientDetailsService,
//                requestFactory,
//                phoneUserDetailService));
        return tokenGranters;
    }

    /**
     * 将TokenGranter列表组装成复杂TokenGranter
     */
    private TokenGranter tokenGranter() {
        if (tokenGranter == null) {
            tokenGranter = new TokenGranter() {
                private CompositeTokenGranter delegate;
                @Override
                public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
                    if (delegate == null) {
                        delegate = new CompositeTokenGranter(getDefaultTokenGranters());
                    }
                    return delegate.grant(grantType, tokenRequest);
                }
            };
        }
        return tokenGranter;
    }




}
