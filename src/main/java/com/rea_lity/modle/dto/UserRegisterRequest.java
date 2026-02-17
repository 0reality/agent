package com.rea_lity.modle.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户注册请求参数
 * 用于新用户注册接口
 * @author rea_lity
 * @version 1.0
 * @since 2026-02-09
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户账号
     * 注册时设置的唯一标识符
     * 长度限制：8-16位字符
     * 必填字段
     */
    @Schema(description = "用户账号，注册时设置的唯一标识符，长度8-16位字符", required = true)
    private String userAccount;

    /**
     * 用户密码
     * 注册时设置的登录密码
     * 长度限制：8-16位字符
     * 必填字段
     */
    @Schema(description = "用户密码，注册时设置的登录密码，长度8-16位字符", required = true)
    private String userPassword;

    /**
     * 确认密码
     * 用于验证两次输入的密码是否一致
     * 必填字段
     */
    @Schema(description = "确认密码，用于验证两次输入的密码是否一致", required = true)
    private String checkPassword;
}
