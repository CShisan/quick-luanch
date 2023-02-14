package com.quick.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ServiceException;

/**
 * @author yuanbai
 */
public class JsonUtil {
    private static final ObjectMapper MAPPER = generate();

    /**
     * 生成mapper
     */
    public static ObjectMapper generate() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    /**
     * entity转string
     */
    public static String toJson(Object o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new ServiceException(CodeEnum.FAIL, "JSON工具转换异常", e);
        }
    }

    /**
     * json转entity
     */
    public static <T> T toEntity(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new ServiceException(CodeEnum.FAIL, "JSON工具转换异常", e);
        }
    }

    /**
     * entity转entity
     */
    public static <T> T e2e(Object entity, Class<T> clazz) {
        return toEntity(toJson(entity), clazz);
    }

    /**
     * 获取单个属性值
     */
    public static String getField(String json, String fieldName) {
        try {
            JsonNode tree = MAPPER.readTree(json);
            JsonNode node = tree.get(fieldName);
            return node.asText();
        } catch (JsonProcessingException e) {
            throw new ServiceException(CodeEnum.FAIL, "JSON工具转换异常", e);
        }
    }
}
