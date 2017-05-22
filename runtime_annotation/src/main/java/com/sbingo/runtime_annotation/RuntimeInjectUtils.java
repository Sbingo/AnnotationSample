package com.sbingo.runtime_annotation;

import android.app.Activity;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
    }

    private static void injectContentView(Activity a) {
        Class<?> cls = a.getClass();
        ContentView contentView = cls.getAnnotation(ContentView.class);
        if (contentView != null) {
            int contentViewId = contentView.value();
            Log.d("utils", contentViewId + "");
            try {
                Method method = cls.getMethod(SET_CONTENT_VIEW, int.class);
                method.invoke(a, contentViewId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectViews(Activity a) {
        Class<?> cls = a.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            AttachView attachView = field.getAnnotation(AttachView.class);
            if (attachView != null) {
                int viewId = attachView.value();
                Log.d("utils", viewId + "");
                if (viewId != -1) {
                    try {
                        Method method = cls.getMethod(FIND_VIEW_BY_ID, int.class);
                        Object value = method.invoke(a, viewId);
                        field.setAccessible(true);
                        field.set(a, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
