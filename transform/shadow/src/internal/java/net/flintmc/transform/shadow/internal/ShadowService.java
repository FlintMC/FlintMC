package net.flintmc.transform.shadow.internal;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.*;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.launcher.LaunchController;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.MethodIdentifier;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.shadow.*;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.FieldMapping;
import net.flintmc.util.mappings.MethodMapping;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collection;

@Singleton
@Service(value = Shadow.class, priority = -20000, state = Service.State.AFTER_IMPLEMENT)
public class ShadowService implements ServiceHandler<Shadow> {

  private final Logger logger;
  private final ClassMappingProvider classMappingProvider;
  private final Multimap<String, AnnotationMeta<Shadow>> transforms;

  @Inject
  private ShadowService(@InjectLogger Logger logger, ClassMappingProvider classMappingProvider) {
    this.logger = logger;
    this.classMappingProvider = classMappingProvider;
    this.transforms = HashMultimap.create();
  }

  @Override
  public void discover(AnnotationMeta<Shadow> meta) throws ServiceNotFoundException {
    CtClass location = meta.getClassIdentifier().getLocation();
    String target = meta.getAnnotation().value();

    if (!location.isInterface()) {
      throw new ServiceNotFoundException(
          String.format(
              "Shadow annotation can only be used on interfaces, but found one on %s (not an interface) pointing to %s",
              location.getName(), target));
    }

    transforms.put(target, meta);
  }

  @ClassTransform
  public void transform(ClassTransformContext classTransformContext) {
    CtClass transforming = classTransformContext.getCtClass();

    ClassMapping classMapping = this.classMappingProvider.get(transforming.getName());
    String name =
        classMapping != null ? classMapping.getDeobfuscatedName() : transforming.getName();

    if (!this.transforms.containsKey(name)) {
      return;
    }

    Collection<AnnotationMeta<Shadow>> metas = this.transforms.get(name);
    ClassPool classPool = transforming.getClassPool();

    for (AnnotationMeta<Shadow> meta : metas) {
      CtClass ifc = meta.getClassIdentifier().getLocation();
      transforming.addInterface(ifc);

      try {
        this.handleMethodProxies(meta, classPool, transforming);
      } catch (ClassNotFoundException | NotFoundException exception) {
        this.logger.trace(
            String.format(
                "Failed to generate a method proxy (@MethodProxy) in %s for shadow interface %s",
                transforming.getName(), ifc.getName()),
            exception);
      }
      try {
        this.handleFieldCreators(meta, transforming);
      } catch (NotFoundException | CannotCompileException exception) {
        this.logger.trace(
            String.format(
                "Failed to generate a field (@FieldCreate) in %s for shadow interface %s",
                transforming.getName(), ifc.getName()),
            exception);
      }
      try {
        this.handleFieldGetters(meta, transforming);
      } catch (NotFoundException | CannotCompileException | ClassNotFoundException exception) {
        this.logger.trace(
            String.format(
                "Failed to generate a field getter (@FieldGetter) in %s for shadow interface %s",
                transforming.getName(), ifc.getName()),
            exception);
      }
      try {
        this.handleFieldSetters(meta, transforming);
      } catch (CannotCompileException | NotFoundException | ClassNotFoundException exception) {
        this.logger.trace(
            String.format(
                "Failed to generate a field setter (@FieldSetter) in %s for shadow interface %s",
                transforming.getName(), ifc.getName()),
            exception);
      }
    }
  }

  private void handleFieldCreators(AnnotationMeta<Shadow> identifierMeta, CtClass ctClass)
      throws NotFoundException, CannotCompileException {
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
        CtField ctField =
            new CtField(
                ctClass.getClassPool().get(fieldCreate.typeName()), fieldCreate.name(), ctClass);
        if (fieldCreate.defaultValue().isEmpty()) {
          ctClass.addField(ctField);
        } else {
          ctClass.addField(ctField, fieldCreate.defaultValue());
        }
      }
    }
  }

  private void handleFieldSetters(AnnotationMeta<Shadow> identifierMeta, CtClass ctClass)
      throws CannotCompileException, NotFoundException, ClassNotFoundException {
    for (AnnotationMeta<FieldSetter> fieldSetterMeta :
        identifierMeta.getMetaData(FieldSetter.class)) {
      FieldSetter fieldSetter = fieldSetterMeta.getAnnotation();
      CtMethod method = getLocation(fieldSetterMeta.getIdentifier());

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
      if (fieldName == null) fieldName = fieldSetter.value();

      CtField field = ctClass.getField(fieldName);

      if (Modifier.isFinal(field.getModifiers())) {
        field.setModifiers(field.getModifiers() & ~Modifier.FINAL);
      }

      if (!hasMethod(ctClass, method.getName(), parameters)) {
        ctClass.addMethod(
            CtMethod.make(
                String.format(
                    "public void %s(%s arg){this.%s = arg;}",
                    method.getName(), parameters[0].getName(), fieldSetter.value()),
                ctClass));
      }
    }
  }

  private boolean hasMethod(CtClass ctClass, String name, CtClass[] parameters) throws NotFoundException {
    for (CtMethod method : ctClass.getDeclaredMethods()) {
      if (method.getName().equals(name) && Arrays.equals(method.getParameterTypes(), parameters)) {
        return true;
      }
    }

    return false;
  }

  private void handleFieldGetters(AnnotationMeta<Shadow> identifierMeta, CtClass ctClass)
      throws NotFoundException, CannotCompileException, ClassNotFoundException {
    for (AnnotationMeta<FieldGetter> fieldGetterMeta :
        identifierMeta.getMetaData(FieldGetter.class)) {
      FieldGetter fieldGetter = fieldGetterMeta.getAnnotation();
      CtMethod method = this.getLocation(fieldGetterMeta.getIdentifier());

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
      AnnotationMeta<Shadow> identifierMeta, ClassPool classPool, CtClass targetClass)
      throws ClassNotFoundException, NotFoundException {
    for (AnnotationMeta<MethodProxy> methodProxyMeta :
        identifierMeta.getMetaData(MethodProxy.class)) {
      CtMethod proxyMethod = getLocation(methodProxyMeta.getIdentifier());

      CtClass[] parameters = proxyMethod.getParameterTypes();
      CtClass[] classes = new CtClass[parameters.length];
      for (int i = 0; i < classes.length; i++) {
        classes[i] = classPool.get(parameters[i].getName());
      }

      CtMethod target;
      ClassMapping classMapping = this.classMappingProvider.get(targetClass.getName());
      if (classMapping != null) {
        MethodMapping methodMapping = classMapping.getMethod(proxyMethod.getName(), parameters);
        if (methodMapping != null) {
          target = targetClass.getDeclaredMethod(methodMapping.getName(), classes);
          if (!methodMapping.getName().equals(proxyMethod.getName())) {
            try {
              String src =
                  String.format(
                      "public %s %s(){return this.%s($$);}",
                      target.getReturnType().getName(), proxyMethod.getName(), target.getName());

              targetClass.addMethod(CtMethod.make(src, targetClass));
            } catch (CannotCompileException e) {
              this.logger.trace(
                  String.format(
                      "Failed to compile the method proxy for %s#%s",
                      targetClass.getName(), proxyMethod.getName()),
                  e);
            }
          }
        } else {
          target = targetClass.getDeclaredMethod(proxyMethod.getName(), classes);
        }
      } else {
        target = targetClass.getDeclaredMethod(proxyMethod.getName(), classes);
      }
      target.setModifiers(Modifier.setPublic(target.getModifiers()));
    }
  }

  private CtMethod getLocation(MethodIdentifier identifier) throws ClassNotFoundException {
    // we need to load the interface class so parameter and return types get appropriately
    // remapped before getLocation is called
    Class.forName(identifier.getOwner(), false, LaunchController.getInstance().getRootLoader());
    return identifier.getLocation();
  }
}
