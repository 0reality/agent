package com.rea_lity.modle.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户更新请求参数
 * 用于管理员更新用户信息接口
 * @author rea_lity
 * @version 1.0
 * @since 2026-02-09
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * 用户ID
     * 需要更新的用户主键ID
     * 必填字段
     */
    @Schema(description = "用户ID，需要更新的用户主键ID", required = true)
    private Long id;

    /**
     * 用户昵称
     * 用户在系统中显示的名称
     * 可为空
     */
    @Schema(description = "用户昵称，用户在系统中显示的名称", nullable = true)
    private String userName;

    /**
     * 用户头像URL
     * 用户头像的网络地址
     * 可为空
     */
    @Schema(description = "用户头像URL，用户头像的网络地址", nullable = true)
    private String userAvatar;

    /**
     * 用户个人简介
     * 用户的自我介绍信息
     * 可为空
     */
    @Schema(description = "用户个人简介，用户的自我介绍信息", nullable = true)
    private String userProfile;

    /**
     * 用户角色
     * user: 普通用户
     * admin: 管理员
     * ban: 被封禁用户
     * 可为空
     */
    @Schema(description = "用户角色，user: 普通用户, admin: 管理员, ban: 被封禁用户", nullable = true)
    private String userRole;

    private static final long serialVersionUID = 1L;
}