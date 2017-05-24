package com.sbingo;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Author: Sbingo
 * Date:   2017/5/23
 */

public class ProxyInfo {
    private String packageName;
    private String proxyClassName;
    private TypeElement typeElement;

    public Map<Integer, VariableElement> injectVariables = new HashMap<>();

    public static final String PROXY = "ViewInject";

    public ProxyInfo(Elements elementUtils, TypeElement classElement) {
        this.typeElement = classElement;
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        String packageName = packageElement.getQualifiedName().toString();
        String className = ClassValidator.getClassName(classElement, packageName);
        this.packageName = packageName;
        this.proxyClassName = className + "$$" + PROXY;
    }

    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!!!\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import com.sbingo.api.*;\n\n");
        builder.append("public class ").append(proxyClassName).append(" implements " + PROXY + "<" + typeElement.getQualifiedName() + ">");
        builder.append(" {\n\n");
        generateMethods(builder);
        builder.append("\n}");
        return builder.toString();
    }

    private void generateMethods(StringBuilder builder) {
        String space4 = "    ";
        String space8 = space4 + space4;
        builder.append(space4 + "@Override\n");
        builder.append(space4 + "public void inject(" + typeElement.getQualifiedName() + " host, Object source) {\n");
        for (int id : injectVariables.keySet()) {
            VariableElement element = injectVariables.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            builder.append(space8 + "if (source instanceof android.app.Activity) {\n");
            builder.append(space4 + space8 + "host." + name).append(" = ");
            builder.append("(" + type + ") (((android.app.Activity) source).findViewById(" + id + "));\n");
            builder.append(space8 + "} else {\n");
            builder.append(space4 + space8 + "host." + name).append(" = ");
            builder.append("(" + type + ") (((android.view.View) source).findViewById(" + id + "));\n");
            builder.append(space8 + "}\n");
        }
        builder.append(space4 + "}\n");
    }

    public String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

}