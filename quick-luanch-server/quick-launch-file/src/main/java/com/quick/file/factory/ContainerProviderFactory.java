package com.quick.file.factory;

import com.quick.file.provider.ContainerUploadProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CShisan
 */
public class ContainerProviderFactory {
    private static final Map<Class<? extends ContainerUploadProvider>, ContainerUploadProvider> RELATION = new HashMap<>(5);

    public static ContainerUploadProvider classOf(Class<? extends ContainerUploadProvider> clazz) {
        return RELATION.get(clazz);
    }

    public static void register(ContainerUploadProvider provider) {
        RELATION.put(provider.getClass(), provider);
    }
}
