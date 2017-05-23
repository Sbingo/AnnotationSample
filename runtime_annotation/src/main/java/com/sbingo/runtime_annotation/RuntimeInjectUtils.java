package com.sbingo.runtime_annotation;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.sbingo.runtime_annotation.annotation.AttachView;
import com.sbingo.runtime_annotation.annotation.ContentView;
import com.sbingo.runtime_annotation.annotation.internal.ListenerClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author: Sbingo
 * Date:   2017/5/22
 */

public class RuntimeInjectUtils {

    private static final String SET_CONTENT_VIEW = "setContentView";
    private static final String FIND_VIEW_BY_ID = "findViewById";

    public static void inject(Activity a) {
        injectContentView(a);
        injectViews(a);
        injectEvents(a);
    }

    private static void injectContentView(Activity activity) {
        Class<? extends Activity> cls = activity.getClass();
        ContentView contentView = cls.getAnnotation(ContentView.class);
        if (contentView != null) {
            int contentViewId = contentView.value();
            Log.d("contentViewId", contentViewId + "");
            try {
                Method method = cls.getMethod(SET_CONTENT_VIEW, int.class);
                method.invoke(activity, contentViewId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectViews(Activity activity) {
        Class<? extends Activity> cls = activity.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            AttachView attachView = field.getAnnotation(AttachView.class);
            if (attachView != null) {
                int viewId = attachView.value();
                Log.d("viewId", viewId + "");
                if (viewId != -1) {
                    try {
                        Method method = cls.getMethod(FIND_VIEW_BY_ID, int.class);
                        Object value = method.invoke(activity, viewId);
                        field.setAccessible(true);
                        field.set(activity, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void injectEvents(Activity activity) {
        Class<? extends Activity> cls = activity.getClass();
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                ListenerClass listenerClass = annotationType.getAnnotation(ListenerClass.class);
                //如果注解框架中所有事件监听注解内都有ListenerClass注解，则本方法可以注入所有监听事件
                if (listenerClass != null) {
                    String setter = listenerClass.setter();
                    Class<?> type = listenerClass.type();
                    String methodName = listenerClass.methodName();
                    try {
                        //拿到Onclick注解中的value方法
                        Method aMethod = annotationType.getDeclaredMethod("value");
                        //取出所有的viewId
                        int[] viewIds = (int[]) aMethod.invoke(annotation);
                        //通过InvocationHandler设置代理
                        DynamicHandler handler = new DynamicHandler(activity);
                        handler.addMethod(methodName, method);
                        Object listener = Proxy.newProxyInstance(
                                type.getClassLoader(),
                                new Class<?>[]{type}, handler);
                        //遍历所有的View，设置事件
                        for (int viewId : viewIds) {
                            Log.d("event viewId", viewId + "");
                            View view = activity.findViewById(viewId);
                            Method setEventListenerMethod = view.getClass()
                                    .getMethod(setter, type);
                            setEventListenerMethod.invoke(view, listener);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
