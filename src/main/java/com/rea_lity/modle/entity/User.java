package com.rea_lity.modle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户实体类
 * 对应数据库表 user
 * 包含用户的基本信息和系统字段
 * @author rea_lity
 * @version 1.0
 * @since 2026-02-09
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 主键ID
     * 数据库自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID，数据库自增主键")
    private Long id;

    /**
     * 用户账号
     * 用于登录的唯一标识符
     * 长度限制：8-16位字符
     */
    @TableField(value = "userAccount")
    @NotBlank(message = "账号不能为空")
    @Min(message = "账号长度不能少于8位", value = 8)
    @Max(message = "账号长度不能多于16位", value = 16)
    @Schema(description = "用户账号，用于登录的唯一标识符，长度8-16位字符")
    private String userAccount;

    /**
     * 用户密码
     * 经过MD5加密存储
     * 长度限制：8-16位字符
     */
    @TableField(value = "userPassword")
    @NotBlank(message = "账号不能为空")
    @Min(message = "账号长度不能少于8位", value = 8)
    @Max(message = "账号长度不能多于16位", value = 16)
    @Schema(description = "用户密码，经过MD5加密存储，长度8-16位字符")
    private String userPassword;

    /**
     * 微信开放平台ID
     * 用于微信第三方登录
     * 可为空
     */
    @TableField(value = "unionId")
    @Schema(description = "微信开放平台ID，用于微信第三方登录", nullable = true)
    private String unionId;

    /**
     * 微信公众号OpenID
     * 用于微信公众号登录
     * 可为空
     */
    @TableField(value = "mpOpenId")
    @Schema(description = "微信公众号OpenID，用于微信公众号登录", nullable = true)
    private String mpOpenId;

    /**
     * 用户昵称
     * 用户在系统中显示的名称
     * 可为空
     */
    @TableField(value = "userName")
    @Schema(description = "用户昵称，用户在系统中显示的名称", nullable = true)
    private String userName;

    /**
     * 用户头像URL
     * 存储用户头像的网络地址
     * 可为空
     */
    @TableField(value = "userAvatar")
    @Schema(description = "用户头像URL，存储用户头像的网络地址", nullable = true)
    private String userAvatar;

    /**
     * 用户个人简介
     * 用户的自我介绍信息
     * 可为空
     */
    @TableField(value = "userProfile")
    @Schema(description = "用户个人简介，用户的自我介绍信息", nullable = true)
    private String userProfile;

    /**
     * 用户角色
     * user: 普通用户
     * admin: 管理员
     * ban: 被封禁用户
     */
    @TableField(value = "userRole")
    @Schema(description = "用户角色，user: 普通用户, admin: 管理员, ban: 被封禁用户")
    private String userRole;

    /**
     * 记录创建时间
     * 自动生成，不可修改
     */
    @TableField(value = "createTime")
    @Schema(description = "记录创建时间，自动生成，不可修改")
    private Date createTime;

    /**
     * 记录最后更新时间
     * 每次更新记录时自动更新
     */
    @TableField(value = "updateTime")
    @Schema(description = "记录最后更新时间，每次更新记录时自动更新")
    private Date updateTime;

    /**
     * 逻辑删除标识
     * 0: 未删除
     * 1: 已删除
     */
    @TableField(value = "isDelete")
    @Schema(description = "逻辑删除标识，0: 未删除, 1: 已删除")
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}