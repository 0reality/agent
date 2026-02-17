package com.rea_lity.modle.dto;

import com.rea_lity.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * 用户查询请求参数
 * 用于分页查询用户列表接口
 * 继承自 PageRequest，包含分页参数
 * @author rea_lity
 * @version 1.0
 * @since 2026-02-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * 用户ID
     * 精确查询用户ID
     * 可为空
     */
    @Schema(description = "用户ID，精确查询用户ID", nullable = true)
    private Long id;

    /**
     * 微信开放平台ID
     * 用于微信第三方登录用户查询
     * 可为空
     */
    @Schema(description = "微信开放平台ID，用于微信第三方登录用户查询", nullable = true)
    private String unionId;

    /**
     * 微信公众号OpenID
     * 用于微信公众号登录用户查询
     * 可为空
     */
    @Schema(description = "微信公众号OpenID，用于微信公众号登录用户查询", nullable = true)
    private String mpOpenId;

    /**
     * 用户昵称
     * 模糊查询用户昵称
     * 可为空
     */
    @Schema(description = "用户昵称，模糊查询用户昵称", nullable = true)
    private String userName;

    /**
     * 用户个人简介
     * 模糊查询用户简介
     * 可为空
     */
    @Schema(description = "用户个人简介，模糊查询用户简介", nullable = true)
    private String userProfile;

    /**
     * 用户角色
     * user: 普通用户
     * admin: 管理员
     * ban: 被封禁用户
     * 精确查询用户角色
     * 可为空
     */
    @Schema(description = "用户角色，user: 普通用户, admin: 管理员, ban: 被封禁用户，精确查询用户角色", nullable = true)
    private String userRole;

    private static final long serialVersionUID = 1L;
}