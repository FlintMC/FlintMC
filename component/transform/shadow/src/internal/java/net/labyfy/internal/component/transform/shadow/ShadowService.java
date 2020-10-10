package net.labyfy.internal.component.transform.shadow;

import javassist.*;
import net.labyfy.component.processing.autoload.DetectableAnnotationProvider;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.component.transform.shadow.*;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Service(Shadow.class)
public class ShadowService implements ServiceHandler<Shadow> {

  private final Map<String, DetectableAnnotationProvider.AnnotationMeta<Shadow>> transforms = new HashMap<>();

  @Override
  public void discover(DetectableAnnotationProvider.AnnotationMeta<Shadow> identifierMeta) throws ServiceNotFoundException {
    transforms.put(identifierMeta.getAnnotation().value(), identifierMeta);
  }

  @ClassTransform
  public void transform(ClassTransformContext classTransformContext) throws NotFoundException, CannotCompileException {
    if (!this.transforms.containsKey(classTransformContext.getCtClass().getName())) return;
    CtClass ctClass = classTransformContext.getCtClass();

    DetectableAnnotationProvider.AnnotationMeta<Shadow> identifierMeta = this.transforms.get(classTransformContext.getCtClass().getName());
    ClassPool classPool = classTransformContext.getCtClass().getClassPool();
    classTransformContext.getCtClass().addInterface(classPool.get(identifierMeta.<DetectableAnnotationProvider.AnnotationMeta.ClassIdentifier>getIdentifier().getName()));
    handleMethodProxies(identifierMeta, classPool, ctClass);
    handleFieldCreators(identifierMeta, ctClass);
    handleFieldGetters(identifierMeta, ctClass);
    handleFieldSetters(identifierMeta, ctClass);
  }

  private void handleFieldCreators(DetectableAnnotationProvider.AnnotationMeta<Shadow> identifierMeta, CtClass ctClass) {
    for (DetectableAnnotationProvider.AnnotationMeta<FieldCreate> fieldCreateMeta : identifierMeta.getMetaData(FieldCreate.class)) {
      FieldCreate fieldCreate = fieldCreateMeta.getAnnotation();
      boolean exist = false;
      for (CtField field : ctClass.getFields()) {
        if (field.getName().equals(fieldCreate.name())) {
          exist = true;
        }
      }
      if (!exist) {
        try {
          CtField ctField = new CtField(
              ctClass.getClassPool().get(fieldCreate.typeName()),
              fieldCreate.name(),
              ctClass
          );
          if (fieldCreate.defaultValue().isEmpty()) {
            ctClass.addField(ctField);
          } else {
            ctClass.addField(
                ctField,
                fieldCreate.defaultValue());
          }

        } catch (CannotCompileException | NotFoundException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void handleFieldSetters(DetectableAnnotationProvider.AnnotationMeta<Shadow> identifierMeta, CtClass ctClass) throws CannotCompileException, NotFoundException {
    for (DetectableAnnotationProvider.AnnotationMeta<FieldSetter> fieldSetterMeta : identifierMeta.getMetaData(FieldSetter.class)) {
      FieldSetter fieldSetter = fieldSetterMeta.getAnnotation();
      CtMethod method = fieldSetterMeta.<DetectableAnnotationProvider.AnnotationMeta.MethodIdentifier>getIdentifier().getLocation();

      CtClass[] parameters = method.getParameterTypes();
      if (parameters.length != 1) {
        throw new IllegalArgumentException("Setter " + method + " must have one arguments.");
      }
      if (method.getReturnType() != CtClass.voidType) {
        throw new IllegalStateException("Return type for " + method + " must be void");
      }
      ctClass.addMethod(CtMethod.make("public void " + method.getName() + "(" + parameters[0].getName() + " arg){this." + fieldSetter.value() + " = arg;}", ctClass));
    }
  }

  private void handleFieldGetters(DetectableAnnotationProvider.AnnotationMeta<Shadow> identifierMeta, CtClass ctClass) throws CannotCompileException, NotFoundException {
    for (DetectableAnnotationProvider.AnnotationMeta<FieldGetter> fieldGetterMeta : identifierMeta.getMetaData(FieldGetter.class)) {
      FieldGetter fieldGetter = fieldGetterMeta.getAnnotation();
      CtMethod method = fieldGetterMeta.<DetectableAnnotationProvider.AnnotationMeta.MethodIdentifier>getIdentifier().getLocation();

      CtClass[] parameters = method.getParameterTypes();
      if (parameters.length != 0) {
        throw new IllegalArgumentException("Getter " + method + " must not have arguments.");
      }
      ctClass.addMethod(CtMethod.make("public " + method.getReturnType().getName() + " " + method.getName() + "(){return " + "this." + fieldGetter.value() + ";}", ctClass));
    }
  }

  private void handleMethodProxies(DetectableAnnotationProvider.AnnotationMeta<Shadow> identifierMeta, ClassPool classPool, CtClass ctClass) throws
      NotFoundException {
    for (DetectableAnnotationProvider.AnnotationMeta<MethodProxy> methodProxyMeta : identifierMeta.getMetaData(MethodProxy.class)) {
      CtMethod method = methodProxyMeta.<DetectableAnnotationProvider.AnnotationMeta.MethodIdentifier>getIdentifier().getLocation();

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
