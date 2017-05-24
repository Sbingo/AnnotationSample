package com.sbingo;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Author: Sbingo
 * Date:   2017/5/23
 */

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.sbingo.BindView"})
public class ViewInjectProcessor extends AbstractProcessor {

    private Map<String, ProxyInfo> mProxyMap = new HashMap<String, ProxyInfo>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "process...");
        mProxyMap.clear();
        Elements elements = processingEnv.getElementUtils();
        Filer filer = processingEnv.getFiler();
        Set<? extends Element> elementsWithBind = roundEnv.getElementsAnnotatedWith(BindView.class);
        for (Element element : elementsWithBind) {
            checkAnnotationValid(element, BindView.class);
            VariableElement variableElement = (VariableElement) element;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            String fqClassName = classElement.getQualifiedName().toString();
            //One Activity or Fragment needs one ProxyInfo
            ProxyInfo proxyInfo = mProxyMap.get(fqClassName);
            if (proxyInfo == null) {
                proxyInfo = new ProxyInfo(elements, classElement);
                mProxyMap.put(fqClassName, proxyInfo);
            }

            BindView bindAnnotation = variableElement.getAnnotation(BindView.class);
            int id = bindAnnotation.value();
            proxyInfo.injectVariables.put(id, variableElement);
        }

        for (String key : mProxyMap.keySet()) {
            ProxyInfo proxyInfo = mProxyMap.get(key);
            try {
                JavaFileObject jfo = filer.createSourceFile(
                        proxyInfo.getProxyClassFullName(),
                        proxyInfo.getTypeElement());
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                error(proxyInfo.getTypeElement(),
                        "Unable to write injector for type %s: %s",
                        proxyInfo.getTypeElement(), e.getMessage());
            }
        }
        return true;
    }

    private boolean checkAnnotationValid(Element annotatedElement, Class clazz) {
        if (annotatedElement.getKind() != ElementKind.FIELD) {
            error(annotatedElement, "%s must be declared on field.", clazz.getSimpleName());
            return false;
        }
        if (ClassValidator.isPrivate(annotatedElement)) {
            error(annotatedElement, "field \"%s\" must can not be private.", annotatedElement.getSimpleName());
            return false;
        }
        return true;
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message, element);
    }
}
