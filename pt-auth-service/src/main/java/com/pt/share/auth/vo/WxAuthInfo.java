package com.pt.share.auth.vo;

import lombok.Data;

/**
 * @ClassName WxAuthInfo
 * @Description 微信登录认证信息类
 * 用于接收微信小程序登录认证的信息
 * @Author potato
 * @Date 2020/12/15 下午5:37
 **/
@Data
public class WxAuthInfo {
    /**
     * 微信认证授权码
     */
    private String loginCode;
    /**
     * 用户信息的字符串 （需要用gson转为对象）
     */
    private String userInfo;
    /**
     * 签名
     */
    private String signature;

}
