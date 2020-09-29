package net.labyfy.internal.component.transform.shadow;

import javassist.*;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.property.Property;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.component.transform.shadow.*;

import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Service(Shadow.class)
public class ShadowService implements ServiceHandler {

  private final Map<String, Property.Base> transforms = new HashMap<>();

  @Override
  public void discover(Identifier.Base property) throws ServiceNotFoundException {
    transforms.put(property.getProperty().getLocatedIdentifiedAnnotation().<Shadow>getAnnotation().value(), property.getProperty());
  }

  @ClassTransform
  public void transform(ClassTransformContext classTransformContext) throws NotFoundException, CannotCompileException {
    if (!this.transforms.containsKey(classTransformContext.getCtClass().getName())) return;
    Property.Base property = this.transforms.get(classTransformContext.getCtClass().getName());
    ClassPool classPool = classTransformContext.getCtClass().getClassPool();
    classTransformContext.getCtClass().addInterface(classPool.get(property.getLocatedIdentifiedAnnotation().<Class<?>>getLocation().getName()));
    handleMethodProxies(property, classPool, classTransformContext);
    handleFieldCreators(property, classTransformContext.getCtClass());
    handleFieldGetters(property, classTransformContext.getCtClass());
    handleFieldSetters(property, classTransformContext.getCtClass());
  }

  private void handleFieldCreators(Property.Base property, CtClass ctClass) {
    for (Property.Base fieldCreators : property.getSubProperties(FieldCreate.class)) {
      FieldCreate fieldCreate = fieldCreators.getLocatedIdentifiedAnnotation().<FieldCreate>getAnnotation();

      boolean exist = false;
      for (CtField field : ctClass.getFields()) {
        if (field.getName().equals(fieldCreate.name())) {
          exist = true;
        }
      }
      if (!exist) {
        try {
          ctClass.addField(new CtField(ctClass.getClassPool().get(fieldCreate.typeName()), fieldCreate.name(), ctClass), fieldCreate.defaultValue());
        } catch (CannotCompileException | NotFoundException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void handleFieldSetters(Property.Base property, CtClass ctClass) throws CannotCompileException {
    for (Property.Base fieldSetterProperty : property.getSubProperties(FieldSetter.class)) {
      Method method = fieldSetterProperty.getLocatedIdentifiedAnnotation().getLocation();
      FieldSetter fieldSetter = fieldSetterProperty.getLocatedIdentifiedAnnotation().getAnnotation();

      Parameter[] parameters = method.getParameters();
      if (parameters.length != 1) {
        throw new IllegalArgumentException("Setter " + method + " must have one arguments.");
      }
      if (method.getReturnType() != Void.TYPE) {
        throw new IllegalStateException("Return type for " + method + " must be void");
      }

      ctClass.addMethod(CtMethod.make("public void " + method.getName() + "(" + parameters[0].getType().getTypeName() + " arg){this." + fieldSetter.value() + " = arg;}", ctClass));
    }
  }

  private void handleFieldGetters(Property.Base property, CtClass ctClass) throws CannotCompileException {
    for (Property.Base fieldGetterProperty : property.getSubProperties(FieldGetter.class)) {
      Method method = fieldGetterProperty.getLocatedIdentifiedAnnotation().getLocation();
      FieldGetter fieldGetter = fieldGetterProperty.getLocatedIdentifiedAnnotation().getAnnotation();

      Parameter[] parameters = method.getParameters();
      if (parameters.length != 0) {
        throw new IllegalArgumentException("Getter " + method + " must not have arguments.");
      }
      ctClass.addMethod(CtMethod.make("public " + method.getReturnType().getTypeName() + " " + method.getName() + "(){return " + "this." + fieldGetter.value() + ";}", ctClass));
    }
  }

  private void handleMethodProxies(Property.Base property, ClassPool classPool, ClassTransformContext classTransformContext) throws NotFoundException {
    for (Property.Base methodProxy : property.getSubProperties(MethodProxy.class)) {
      Method method = methodProxy.getLocatedIdentifiedAnnotation().getLocation();

      Parameter[] parameters = method.getParameters();
      CtClass[] classes = new CtClass[parameters.length];
      for (int i = 0; i < classes.length; i++) {
        classes[i] = classPool.get(parameters[i].getType().getName());
      }

      CtMethod target = classTransformContext.getCtClass().getDeclaredMethod(method.getName(), classes);
      target.setModifiers(Modifier.setPublic(target.getModifiers()));
    }
  }
}
