package com.quick.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuanbai
 */
@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {
    /**
     * 资源类型枚举
     */
    FRONT_END(1, "前端"),
    BACK_END(2, "后端");

    @JsonValue
    @EnumValue
    private final int code;
    private final String desc;
    private static final Map<Integer, ResourceTypeEnum> MAP = new HashMap<>(16);

    static {
        for (ResourceTypeEnum value : values()) {
            MAP.put(value.code, value);
        }
    }

    public static ResourceTypeEnum codeOf(int code) {
        return MAP.get(code);
    }
}
