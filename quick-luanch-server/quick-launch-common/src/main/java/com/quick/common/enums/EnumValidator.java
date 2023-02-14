package com.quick.common.enums;

import com.quick.common.exception.ServiceException;
import com.quick.common.utils.CheckUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 弃用
 * 原因: 现在直接用枚举当类型, jackson解析不存在值会直接抛异常, 轮不到这个注解验证
 *
 * @author yuanbai
 */
@Documented
@Constraint(validatedBy = {EnumValidator.Validator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Deprecated
public @interface EnumValidator {
    /**
     * 错误信息
     */
    String message() default "枚举值错误";

    /**
     * 枚举类
     */
    Class<? extends Enum<?>> clazz();

    /**
     * 枚举校验的方法
     */
    String method() default "isValid";

    /**
     * 允许为空
     */
    boolean allowNull() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 校验器
     */
    class Validator implements ConstraintValidator<EnumValidator, Object> {

        private Class<? extends Enum<?>> enumClass;
        private String enumMethod;
        private boolean allowNull;

        @Override
        public void initialize(EnumValidator enumValue) {
            enumMethod = enumValue.method();
            enumClass = enumValue.clazz();
            allowNull = enumValue.allowNull();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            if (CheckUtil.isEmpty(value)) {
                return allowNull;
            }
            if (CheckUtil.anyNull(enumClass, enumMethod)) {
                return true;
            }
            Class<?> valueClass = value.getClass();
            try {
                Method method = enumClass.getMethod(enumMethod, valueClass);
                if (!Boolean.TYPE.equals(method.getReturnType()) && !Boolean.class.equals(method.getReturnType())) {
                    throw new ServiceException(CodeEnum.FAIL, String.format("类%s的%s方法返回类型不为boolean", enumClass, enumMethod));
                }
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new ServiceException(CodeEnum.FAIL, String.format("类%s的%s方法不是静态方法", enumClass, enumMethod));
                }
                Boolean result = (Boolean) method.invoke(null, value);
                return CheckUtil.nonNull(result) && result;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new ServiceException(CodeEnum.FAIL, e);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new ServiceException(CodeEnum.FAIL, String.format("类%s不存在%s(%s)方法", enumClass, enumMethod, valueClass), e);
            }
        }
    }
}
