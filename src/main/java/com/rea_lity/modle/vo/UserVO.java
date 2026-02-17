package com.rea_lity.modle.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户视图对象
 * 用于返回给其他用户的用户信息
 * 包含脱敏后的公开用户数据
 * @author rea_lity
 * @version 1.0
 * @since 2026-02-09
 */
@Data
public class UserVO implements Serializable {

    /**
     * 用户ID
     * 用户的唯一标识符
     */
    @Schema(description = "用户ID，用户的唯一标识符")
    private Long id;

    /**
     * 用户昵称
     * 用户在系统中显示的名称
     */
    @Schema(description = "用户昵称，用户在系统中显示的名称")
    private String userName;

    /**
     * 用户头像URL
     * 用户头像的网络地址
     */
    @Schema(description = "用户头像URL，用户头像的网络地址")
    private String userAvatar;

    /**
     * 用户个人简介
     * 用户的自我介绍信息
     */
    @Schema(description = "用户个人简介，用户的自我介绍信息")
    private String userProfile;

    /**
     * 用户角色
     * user: 普通用户
     * admin: 管理员
     * ban: 被封禁用户
     */
    @Schema(description = "用户角色，user: 普通用户, admin: 管理员, ban: 被封禁用户")
    private String userRole;

    /**
     * 账号创建时间
     * 用户注册的时间
     */
    @Schema(description = "账号创建时间，用户注册的时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}