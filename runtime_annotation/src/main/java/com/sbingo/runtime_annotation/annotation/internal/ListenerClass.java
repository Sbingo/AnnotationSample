package com.sbingo.runtime_annotation.annotation.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Sbingo
 * Date:   2017/5/23
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerClass {
    String setter();

    Class<?> type();

    String methodName();
}
