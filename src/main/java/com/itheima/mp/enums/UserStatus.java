package com.itheima.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * UserStatus
 *
 * @author sundae
 * @date 2024/11/26
 * @description 用户状态枚举类
 */
@Getter
public enum UserStatus {
    NORMAL(1, "正常"),
    FROZEN(2, "冻结"),
    ;

    @EnumValue // 指定mp将枚举的value字段映射为数据库中的值
    @JsonValue // 指定响应的字段为value
    private final int value;
    private final String desc;

    UserStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 按值获取enum
     *
     * @param value
     * @return {@link UserStatus }
     */
    public static UserStatus getEnumByValue(int value) {
        for (UserStatus anEnum : UserStatus.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }
}

