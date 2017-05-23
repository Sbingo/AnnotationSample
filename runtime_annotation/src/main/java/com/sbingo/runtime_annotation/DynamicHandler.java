package com.sbingo.runtime_annotation;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Author: Sbingo
 * Date:   2017/5/23
 */

public class DynamicHandler implements InvocationHandler {

    private WeakReference<Object> handlerRef;
    private final HashMap<String, Method> methodMap = new HashMap<String, Method>(1);

    public DynamicHandler(Object handler) {
        setHandler(handler);
    }

    public void addMethod(String name, Method method) {
        methodMap.put(name, method);
    }

    public Object getHandler() {
        return handlerRef.get();
    }

    public void setHandler(Object handler) {
        this.handlerRef = new WeakReference<>(handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object handler = getHandler();
        if (handler != null) {
            String methodName = method.getName();
            Log.d("handler", methodName + args.toString());
            Method realMethod = methodMap.get(methodName);
            if (realMethod != null) {
                return realMethod.invoke(handler, args);
            }
        }
        return null;
    }
}
