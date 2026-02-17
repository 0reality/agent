package com.rea_lity.modle.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户创建请求参数
 * 用于管理员创建新用户接口
 * @author rea_lity
 * @version 1.0
 * @since 2026-02-09
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     * 用户在系统中显示的名称
     * 可为空
     */
    @Schema(description = "用户昵称，用户在系统中显示的名称", nullable = true)
    private String userName;

    /**
     * 用户账号
     * 用户登录的唯一标识符
     * 必填字段
     */
    @Schema(description = "用户账号，用户登录的唯一标识符", required = true)
    private String userAccount;

    /**
     * 用户头像URL
     * 用户头像的网络地址
     * 可为空
     */
    @Schema(description = "用户头像URL，用户头像的网络地址", nullable = true)
    private String userAvatar;

    /**
     * 用户角色
     * user: 普通用户
     * admin: 管理员
     * 必填字段
     */
    @Schema(description = "用户角色，user: 普通用户, admin: 管理员", required = true)
    private String userRole;

    private static final long serialVersionUID = 1L;
}