package com.quick.file.factory;

import com.quick.file.handler.upload.UploadHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CShisan
 */
public class UploadHandlerFactory {
    private static final Map<Class<?>, UploadHandler<?>> RELATION = new HashMap<>(16);

    public static UploadHandler<?> classOf(Class<?> clazz) {
        return RELATION.get(clazz);
    }

    public static void register(UploadHandler<?> handler) {
        RELATION.put(handler.getClass(), handler);
    }
}
