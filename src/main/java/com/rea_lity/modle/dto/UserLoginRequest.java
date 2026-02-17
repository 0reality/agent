package com.rea_lity.modle.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户登录请求参数
 * 用于用户账号密码登录接口
 * @author rea_lity
 * @version 1.0
 * @since 2026-02-09
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户账号
     * 登录使用的唯一标识符
     * 必填字段
     */
    @Schema(description = "用户账号，登录使用的唯一标识符", required = true)
    private String userAccount;

    /**
     * 用户密码
     * 登录验证的密码
     * 必填字段
     */
    @Schema(description = "用户密码，登录验证的密码", required = true)
    private String userPassword;
}
