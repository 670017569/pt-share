package com.pt.share.auth.vo;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * @ClassName WxAuthenticationToken
 * @Description 封装微信认证的Token
 * @Author potato
 * @Date 2020/12/15 下午10:12
 **/
public class WxAuthenticationToken extends AbstractAuthenticationToken {

    // 序列化号和Security版本一致, 更换Security版本可能会导致token失效
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    /**
     * <p>未验证时存放{@link WxAuthInfo}</p>
     * <p>验证成功后存放{@link org.springframework.security.core.userdetails.UserDetails}</p>
     */
    private final Object principal;

    public WxAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    /**
     *
     * @param principal
     * @param authorities
     */
    public WxAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
