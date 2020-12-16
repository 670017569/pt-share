package com.pt.share.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName WxUserInfo
 * @Description 微信用户信息
 * @Author potato
 * @Date 2020/12/15 下午8:52
 **/
@Data
@Table(name = "wx_userinfo")
@Builder
public class WxUserInfo {
    /**
     * 用户id
     */
    @Id
    private Long uid;
    /**
     * 省份
     */
    @Column
    private String province;
    /**
     * 昵称
     */
    @Column
    private String nickName;
    /**
     * 语言
     */
    @Column
    private String language;
    /**
     * 性别
     */
    @Column
    private String gender;
    /**
     * 国家
     */
    @Column
    private String country;
    /**
     * 城市
     */
    @Column
    private String city;
    /**
     * 头像链接
     */
    @Column
    private String avatarUrl;

}
