package com.quick.common.utils;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 该类不是为了造轮子,是为了统一使用检查方法
 *
 * @author CShisan
 */
public class CheckUtil {
    public static boolean isNull(Object obj) {
        return Objects.isNull(obj);
    }

    public static boolean nonNull(Object obj) {
        return Objects.nonNull(obj);
    }

    public static boolean anyNull(Object... objs) {
        return Arrays.stream(objs).anyMatch(Objects::isNull);
    }

    public static boolean allNull(Object... objs) {
        return Arrays.stream(objs).allMatch(Objects::isNull);
    }

    public static boolean anyNonNull(Object... objs) {
        return Arrays.stream(objs).anyMatch(Objects::nonNull);
    }

    public static boolean allNonNull(Object... objs) {
        return Arrays.stream(objs).allMatch(Objects::nonNull);
    }

    public static boolean anyNull(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().anyMatch(Objects::isNull);
    }

    public static boolean allNull(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().allMatch(Objects::isNull);
    }

    public static boolean anyNonNull(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().anyMatch(Objects::nonNull);
    }

    public static boolean allNonNull(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().allMatch(Objects::nonNull);
    }

    public static boolean isEmpty(Object obj) {
        return ObjectUtils.isEmpty(obj);
    }

    public static boolean nonEmpty(Object obj) {
        return !ObjectUtils.isEmpty(obj);
    }

    public static boolean anyEmpty(Object... objs) {
        return Arrays.stream(objs).anyMatch(ObjectUtils::isEmpty);
    }

    public static boolean allEmpty(Object... objs) {
        return Arrays.stream(objs).allMatch(ObjectUtils::isEmpty);
    }

    public static boolean anyNonEmpty(Object... objs) {
        return Arrays.stream(objs).anyMatch(obj -> !ObjectUtils.isEmpty(obj));
    }

    public static boolean allNonEmpty(Object... objs) {
        return Arrays.stream(objs).noneMatch(ObjectUtils::isEmpty);
    }

    public static boolean anyEmpty(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().anyMatch(ObjectUtils::isEmpty);
    }

    public static boolean allEmpty(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().allMatch(ObjectUtils::isEmpty);
    }

    public static boolean anyNonEmpty(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().anyMatch(obj -> !ObjectUtils.isEmpty(obj));
    }

    public static boolean allNonEmpty(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().noneMatch(ObjectUtils::isEmpty);
    }

    /**
     * {@link org.springframework.util.ObjectUtils#isEmpty(Object)}
     */
    public static boolean isBlank(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Optional) {
            return !((Optional<?>) obj).isPresent();
        } else if (obj instanceof CharSequence) {
            boolean noLength = ((CharSequence) obj).length() == 0;
            boolean hasWhitespace = ((CharSequence) obj).chars().anyMatch(item -> !Character.isWhitespace(item));
            return noLength && hasWhitespace;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        } else {
            return obj instanceof Map && ((Map<?, ?>) obj).isEmpty();
        }
    }

    public static boolean nonBlank(Object obj) {
        return !isBlank(obj);
    }

    public static boolean anyBlank(Object... objs) {
        return Arrays.stream(objs).anyMatch(CheckUtil::isBlank);
    }

    public static boolean allBlank(Object... objs) {
        return Arrays.stream(objs).allMatch(CheckUtil::isBlank);
    }

    public static boolean anyNonBlank(Object... objs) {
        return Arrays.stream(objs).anyMatch(CheckUtil::nonBlank);
    }

    public static boolean allNonBlank(Object... objs) {
        return Arrays.stream(objs).allMatch(CheckUtil::nonBlank);
    }

    public static boolean anyBlank(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().anyMatch(CheckUtil::isBlank);
    }

    public static boolean allBlank(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().allMatch(CheckUtil::isBlank);
    }

    public static boolean anyNonBlank(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().anyMatch(CheckUtil::nonBlank);
    }

    public static boolean allNonBlank(Collection<?> collection) {
        return Objects.nonNull(collection) && collection.stream().allMatch(CheckUtil::nonBlank);
    }
}
