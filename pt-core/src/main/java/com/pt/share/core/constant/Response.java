package com.pt.share.core.constant;

/**
 * @ClassName Response
 * @Description 枚举已知返回信息
 * @Author potato
 * @Date 2020/12/15 下午10:54
 **/
public enum Response {
    SUCCESS(200, "短信发送成功"),
    LOGIN_SUCCESS(200,"登录成功"),
    REGISTER_SUCCESS(200,"注册成功"),
    CHECK_SUCCESS(200,"查询成功"),
    INVALID_INPUT(40001, "不合法信息"),
    UNAUTHORIZED(401,"未登录，或登录已过期"),
    INVALID_GRANT_TYPE(405,"不支持的认证方式"),
    INVALID_PASSWORD_USERNAME(403,"用户名或密码错误"),
    ILLEGAL_SCOPE(408,"不合法的授权范围"),
    USER_AUTH_UPDATE_SUCCESS(200,"用户认证信息更新成功"),
    USER_AUTH_UPDATE_FAILED(200,"用户认证信息更新失败"),
    USERINFO_UPDATE_SUCCESS(200,"用户详细信息更新成功"),
    USERINFO_UPDATE_FAILED(403,"用户详细信息更新失败"),
    INVALID_PHONE(40004, "无效的手机号"),
    SUCCESS_SMS_CODE(200,"验证码验证通过"),
    INVALID_SMS_CODE(400, "验证码错误"),
    ILLEGAL_CLIENT(406,"不合法的客户端"),
    ALREADY_DONE(40006, "不要重复操作"),
    UPDATE_FAIL(40007, "更新失败"),
    NO_PERMISSION(40301, "无权限"),
    WX_SESSION_OPEN_FAIL(406,"微信会话打开失败"),
    WX_SESSION_INVALID(407,"微信签名完整性错误"),
    ACCOUNT_LOCKED(408,"账号状态异常");

    private Integer code;
    private String message;

    Response(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
