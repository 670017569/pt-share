package com.pt.share.auth.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @ClassName WxUserSession
 * @Description 微信会话信息类
 * @Author potato
 * @Date 2020/12/15 下午9:18
 **/
@Data
public class WxUserSession {
    private String openid;
    @SerializedName("session_key")
    private String sessionKey;
    private String unionid;
    private String errcode;
    private String errmsg;
}