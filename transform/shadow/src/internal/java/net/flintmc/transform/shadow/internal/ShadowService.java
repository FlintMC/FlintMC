package net.flintmc.transform.shadow.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.*;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.ClassIdentifier;
import net.flintmc.processing.autoload.identifier.MethodIdentifier;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.shadow.*;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.FieldMapping;
import net.flintmc.util.mappings.MethodMapping;
import net.flintmc.util.mappings.utils.MappingUtils;
import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Service(value = Shadow.class, priority = -20000, state = Service.State.AFTER_IMPLEMENT)
public class ShadowService implements ServiceHandler<Shadow> {

  private final ClassMappingProvider classMappingProvider;
  private final Map<String, AnnotationMeta<Shadow>> transforms = new HashMap<>();

  @Inject
  private ShadowService(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  @Override
  public void discover(AnnotationMeta<Shadow> identifierMeta) throws ServiceNotFoundException {
    transforms.put(identifierMeta.getAnnotation().value(), identifierMeta);
  }

  @ClassTransform
  public void transform(ClassTransformContext classTransformContext)
      throws NotFoundException, CannotCompileException {
    ClassMapping classMapping = this.classMappingProvider.get(classTransformContext.getCtClass().getName());
    String name = classMapping != null ? classMapping.getDeobfuscatedName() : classTransformContext.getCtClass().getName();
    if (!this.transforms.containsKey(name)) return;
    CtClass ctClass = classTransformContext.getCtClass();

    AnnotationMeta<Shadow> identifierMeta =
        this.transforms.get(name);
    ClassPool classPool = classTransformContext.getCtClass().getClassPool();
    classTransformContext
        .getCtClass()
        .addInterface(classPool.get(identifierMeta.<ClassIdentifier>getIdentifier().getName()));
    handleMethodProxies(identifierMeta, classPool, classTransformContext);
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
      CtMethod method = fieldSetterMeta.<MethodIdentifier>getIdentifier().getLocation();

      CtClass[] parameters = method.getParameterTypes();
      if (parameters.length != 1) {
        throw new IllegalArgumentException("Setter " + method + " must have one arguments.");
      }
      if (method.getReturnType() != CtClass.voidType) {
        throw new IllegalStateException("Return type for " + method + " must be void");
      }

      String fieldName = null;
      ClassMapping classMapping = classMappingProvider.get(ctClass.getName());
      if (classMapping != null) {
        fieldName = classMapping.getField(fieldSetter.value()).getName();
      }
      if (fieldName == null)
        fieldName = fieldSetter.value();

      if (!hasMethod(ctClass, method.getName(), parameters)) {

        ctClass.addMethod(
            CtMethod.make(
                String.format(
                    "public void %s(%s arg){this.%s = arg;}",
                    method.getName(), parameters[0].getName(), fieldName),
                ctClass));
      }
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
      CtMethod method = fieldGetterMeta.<MethodIdentifier>getIdentifier().getLocation();

      CtClass[] parameters = method.getParameterTypes();
      if (parameters.length != 0) {
        throw new IllegalArgumentException("Getter " + method + " must not have arguments.");
      }
      if (!hasMethod(ctClass, method.getName(), parameters)) {
        ClassMapping classMapping = classMappingProvider.get(ctClass.getName());
        String name;
        if (classMapping != null) {
          FieldMapping field = classMapping.getField(fieldGetter.value());
          if (field != null) {
            name = field.getName();
          } else {
            name = fieldGetter.value();
          }
        } else {
          name = fieldGetter.value();
        }

        ctClass.addMethod(
            CtMethod.make(
                String.format(
                    "public %s %s(){return this.%s;}",
                    method.getReturnType().getName(), method.getName(), name),
                ctClass));
      }
    }

  }

  private void handleMethodProxies(
      AnnotationMeta<Shadow> identifierMeta, ClassPool classPool, ClassTransformContext classTransformContext)
      throws NotFoundException {
    for (AnnotationMeta<MethodProxy> methodProxyMeta :
        identifierMeta.getMetaData(MethodProxy.class)) {
      CtMethod proxyMethod = methodProxyMeta.<MethodIdentifier>getIdentifier().getLocation();

      CtClass[] parameters = proxyMethod.getParameterTypes();
      CtClass[] classes = new CtClass[parameters.length];
      for (int i = 0; i < classes.length; i++) {
        classes[i] = classPool.get(parameters[i].getName());
      }

      CtMethod target;
      ClassMapping classMapping = this.classMappingProvider.get(classTransformContext.getCtClass().getName());
      if (classMapping != null) {
        MethodMapping methodMapping = classMapping.getMethod(proxyMethod.getName(), parameters);
        if (methodMapping != null) {
          target = classTransformContext.getCtClass().getDeclaredMethod(methodMapping.getName(), classes);
          if (!methodMapping.getName().equals(proxyMethod.getName())) {
            try {
              classTransformContext.getCtClass().addMethod(CtMethod.make(String.format("public %s %s(){return this.%s($$);}", target.getReturnType().getName(), proxyMethod.getName(), target.getName()), classTransformContext.getCtClass()));
            } catch (CannotCompileException e) {
              e.printStackTrace();
            }
            System.out.println("OKAY SHIT WE HAVE TO HANDLE " + proxyMethod + " to " + target);
          }
        } else {
          target = classTransformContext.getCtClass().getDeclaredMethod(proxyMethod.getName(), classes);
        }
      } else {
        target = classTransformContext.getCtClass().getDeclaredMethod(proxyMethod.getName(), classes);
      }
      target.setModifiers(Modifier.setPublic(target.getModifiers()));
    }
  }
}
