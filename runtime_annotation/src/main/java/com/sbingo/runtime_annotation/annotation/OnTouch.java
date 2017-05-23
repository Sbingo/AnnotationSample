package com.sbingo.runtime_annotation.annotation;

import android.support.annotation.IdRes;
import android.view.View;

import com.sbingo.runtime_annotation.annotation.internal.ListenerClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Sbingo
 * Date:   2017/5/23
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ListenerClass(setter = "setOnTouchListener", type = View.OnTouchListener.class, methodName = "onTouch")
public @interface OnTouch {
    @IdRes int[] value();
}
