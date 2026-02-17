package com.rea_lity.modle.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 用户角色枚举
 * 定义系统中用户的权限角色
 * @author rea_lity
 * @version 1.0
 * @since 2026-02-09
 */
public enum UserRoleEnum {

    /** 普通用户角色 */
    USER("用户", "user"),
    /** 管理员角色 */
    ADMIN("管理员", "admin"),
    /** 被封禁用户角色 */
    BAN("被封号", "ban");

    private final String text;

    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有角色值列表
     * 用于前端下拉框等场景
     *
     * @return 角色值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据角色值获取对应的枚举对象
     * 用于权限验证和角色转换
     *
     * @param value 角色值(user/admin/ban)
     * @return 对应的UserRoleEnum枚举对象，未找到返回null
     */
    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
