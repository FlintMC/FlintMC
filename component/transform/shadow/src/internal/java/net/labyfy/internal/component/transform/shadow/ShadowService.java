package net.labyfy.internal.component.transform.shadow;

import com.google.inject.Singleton;
import javassist.*;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.processing.autoload.identifier.ClassIdentifier;
import net.labyfy.component.processing.autoload.identifier.MethodIdentifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.component.transform.shadow.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Service(Shadow.class)
public class ShadowService implements ServiceHandler<Shadow> {

  private final Map<String, AnnotationMeta<Shadow>> transforms = new HashMap<>();

  @Override
  public void discover(AnnotationMeta<Shadow> identifierMeta) throws ServiceNotFoundException {
    transforms.put(identifierMeta.getAnnotation().value(), identifierMeta);
  }

  @ClassTransform
  public void transform(ClassTransformContext classTransformContext)
      throws NotFoundException, CannotCompileException {
    if (!this.transforms.containsKey(classTransformContext.getCtClass().getName())) return;
    CtClass ctClass = classTransformContext.getCtClass();

    AnnotationMeta<Shadow> identifierMeta =
        this.transforms.get(classTransformContext.getCtClass().getName());
    ClassPool classPool = classTransformContext.getCtClass().getClassPool();
    classTransformContext
        .getCtClass()
        .addInterface(
            classPool.get(
                identifierMeta.<ClassIdentifier>getIdentifier().getName()));
    handleMethodProxies(identifierMeta, classPool, ctClass);
    handleFieldCreators(identifierMeta, ctClass);
    handleFieldGetters(identifierMeta, ctClass);
    handleFieldSetters(identifierMeta, ctClass);
  }

  private void handleFieldCreators(AnnotationMeta<Shadow> identifierMeta, CtClass ctClass) {
    for (AnnotationMeta<FieldCreate> fieldCreateMeta :
        identifierMeta.getMetaData(FieldCreate.class)) {
      FieldCreate fieldCreate = fieldCreateMeta.getAnnotation();
      boolean exist = false;
      for (CtField field : ctClass.getFields()) {
        if (field.getName().equals(fieldCreate.name())) {
          exist = true;
        }
      }
      if (!exist) {
        try {
          CtField ctField =
              new CtField(
                  ctClass.getClassPool().get(fieldCreate.typeName()), fieldCreate.name(), ctClass);
          if (fieldCreate.defaultValue().isEmpty()) {
            ctClass.addField(ctField);
          } else {
            ctClass.addField(ctField, fieldCreate.defaultValue());
          }

        } catch (CannotCompileException | NotFoundException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void handleFieldSetters(AnnotationMeta<Shadow> identifierMeta, CtClass ctClass)
      throws CannotCompileException, NotFoundException {
    for (AnnotationMeta<FieldSetter> fieldSetterMeta :
        identifierMeta.getMetaData(FieldSetter.class)) {
      FieldSetter fieldSetter = fieldSetterMeta.getAnnotation();
      CtMethod method =
          fieldSetterMeta.<MethodIdentifier>getIdentifier().getLocation();

      CtClass[] parameters = method.getParameterTypes();
      if (parameters.length != 1) {
        throw new IllegalArgumentException("Setter " + method + " must have one arguments.");
      }
      if (method.getReturnType() != CtClass.voidType) {
        throw new IllegalStateException("Return type for " + method + " must be void");
      }

      if (!hasMethod(ctClass, method.getName(), parameters))
        ctClass.addMethod(
            CtMethod.make(
                String.format(
                    "public void %s(%s arg){this.%s = arg;}",
                    method.getName(), parameters[0].getName(), fieldSetter.value()),
                ctClass));

      String fieldName = fieldSetter.value();
      CtField field = ctClass.getField(fieldName);

      if (fieldSetter.removeFinal() && Modifier.isFinal(field.getModifiers())) {
        field.setModifiers(field.getModifiers() & ~Modifier.FINAL);
      }

      if (!hasMethod(ctClass, method.getName(), parameters))
        ctClass.addMethod(
            CtMethod.make(
                String.format(
                    "public void %s(%s arg){this.%s = arg;}",
                    method.getName(), parameters[0].getName(), fieldSetter.value()),
                ctClass));
    }
  }

  private boolean hasMethod(CtClass ctClass, String name, CtClass[] parameters) {
    return Arrays.stream(ctClass.getDeclaredMethods())
        .anyMatch(
            method -> {
              try {
                return method.getName().equals(name)
                    && Arrays.equals(method.getParameterTypes(), parameters);
              } catch (NotFoundException e) {
                e.printStackTrace();
              }
              return false;
            });
  }

  private void handleFieldGetters(AnnotationMeta<Shadow> identifierMeta, CtClass ctClass)
      throws CannotCompileException, NotFoundException {
    for (AnnotationMeta<FieldGetter> fieldGetterMeta :
        identifierMeta.getMetaData(FieldGetter.class)) {
      FieldGetter fieldGetter = fieldGetterMeta.getAnnotation();
      CtMethod method =
          fieldGetterMeta.<MethodIdentifier>getIdentifier().getLocation();

      CtClass[] parameters = method.getParameterTypes();
      if (parameters.length != 0) {
        throw new IllegalArgumentException("Getter " + method + " must not have arguments.");
      }
      if (!hasMethod(ctClass, method.getName(), parameters))
        ctClass.addMethod(
            CtMethod.make(
                String.format(
                    "public %s %s(){return this.%s;}",
                    method.getReturnType().getName(), method.getName(), fieldGetter.value()),
                ctClass));
    }
  }

  private void handleMethodProxies(
      AnnotationMeta<Shadow> identifierMeta, ClassPool classPool, CtClass ctClass)
      throws NotFoundException {
    for (AnnotationMeta<MethodProxy> methodProxyMeta :
        identifierMeta.getMetaData(MethodProxy.class)) {
      CtMethod method =
          methodProxyMeta.<MethodIdentifier>getIdentifier().getLocation();

      CtClass[] parameters = method.getParameterTypes();
      CtClass[] classes = new CtClass[parameters.length];
      for (int i = 0; i < classes.length; i++) {
        classes[i] = classPool.get(parameters[i].getName());
      }

      CtMethod target = ctClass.getDeclaredMethod(method.getName(), classes);
      target.setModifiers(Modifier.setPublic(target.getModifiers()));
    }
  }
}
