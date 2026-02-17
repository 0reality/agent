package com.rea_lity.modle.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户更新个人信息请求参数
 * 用于用户更新自己信息接口
 * @author rea_lity
 * @version 1.0
 * @since 2026-02-09
 */
@Data
public class UserUpdateMyRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}