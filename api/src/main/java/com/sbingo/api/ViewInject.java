package com.sbingo.api;

/**
 * Author: Sbingo
 * Date:   2017/5/23
 */
public interface ViewInject<T> {
    void inject(T t, Object source);
}
