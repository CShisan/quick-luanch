package com.quick.common.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author CShisan
 **/
@Data
public class Result<T> implements Serializable {

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;
}
