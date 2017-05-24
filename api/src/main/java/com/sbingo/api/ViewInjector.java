package com.sbingo.api;

import android.app.Activity;
import android.view.View;

/**
 * Author: Sbingo
 * Date:   2017/5/23
 */
public class ViewInjector {
    private static final String SUFFIX = "$$ViewInject";

    public static void injectView(Activity activity) {
        ViewInject proxyActivity = findProxyActivity(activity);
        proxyActivity.inject(activity, activity);
    }

    public static void injectView(Object object, View view) {
        ViewInject proxyActivity = findProxyActivity(object);
        proxyActivity.inject(object, view);
    }

    private static ViewInject findProxyActivity(Object activity) {
        try {
            Class clazz = activity.getClass();
            Class injectorClazz = Class.forName(clazz.getName() + SUFFIX);
            return (ViewInject) injectorClazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("can not find %s , something must be wrong while compiling.", activity.getClass().getSimpleName() + SUFFIX));
    }
}
