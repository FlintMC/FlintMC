package net.labyfy.internal.component.transform.hook;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Key;
import com.google.inject.name.Names;
import javassist.*;
import net.labyfy.component.commons.resolve.AnnotationResolver;
import net.labyfy.component.inject.InjectedInvocationHelper;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.property.Property;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.transform.hook.HookFilter;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

@Singleton
@Service(Hook.class)
@Deprecated
public class HookService implements ServiceHandler {

  private final ClassMappingProvider classMappingProvider;
  private final String version;
  private final Collection<HookEntry> hooks;

  @Inject
  private HookService(
      ClassMappingProvider classMappingProvider,
      @Named("launchArguments") Map launchArguments
  ) {
    this.classMappingProvider = classMappingProvider;
    this.hooks = Sets.newHashSet();
    this.version = (String) launchArguments.get("--game-version");
  }

  public static void notify(
      Object instance,
      Hook.ExecutionTime executionTime,
      Class<?> clazz,
      String method,
      Class<?>[] parameters,
      Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Map<Key<?>, Object> availableParameters = Maps.newHashMap();
    availableParameters.put(Key.get(Hook.ExecutionTime.class), executionTime);
    availableParameters.put(Key.get(Object.class, Names.named("instance")), instance);
    availableParameters.put(Key.get(instance.getClass()), instance);
    availableParameters.put(Key.get(Object[].class, Names.named("args")), args);

    Method declaredMethod = clazz.getDeclaredMethod(method, parameters);
    InjectionHolder.getInjectedInstance(InjectedInvocationHelper.class)
        .invokeMethod(
            declaredMethod,
            InjectionHolder.getInjectedInstance(declaredMethod.getDeclaringClass()),
            availableParameters);
  }

  @Override
  public void discover(Identifier.Base property) {
    Map<Property.Base, AnnotationResolver<Type, String>> subProperties = Maps.newHashMap();

    for (Property.Base subProperty : property.getProperty().getSubProperties(HookFilter.class)) {
      subProperties.put(
          subProperty,
          InjectionHolder.getInjectedInstance(
              subProperty
                  .getLocatedIdentifiedAnnotation()
                  .<HookFilter>getAnnotation()
                  .type()
                  .typeNameResolver()));
    }

    Hook annotation = property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();

    this.hooks.add(
        new HookEntry(
            property,
            subProperties,
            InjectionHolder.getInjectedInstance(annotation.parameterTypeNameResolver()),
            InjectionHolder.getInjectedInstance(annotation.methodNameResolver())));
  }

  @ClassTransform
  public void transform(ClassTransformContext classTransformContext) throws NotFoundException, CannotCompileException {
    CtClass ctClass = classTransformContext.getCtClass();

    for (HookEntry entry : hooks) {
      Identifier.Base identifier = entry.hook;

      Hook hook = identifier.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
      if (!(hook.version().isEmpty() || hook.version().equals(this.version))) continue;
      if (!hook.className().isEmpty()) {
        String className = classMappingProvider.get(classTransformContext.getNameResolver().resolve(hook.className())).getName();
        if (className != null && className.equals(ctClass.getName())) {
          this.modify(
              entry,
              hook,
              ctClass,
              identifier.getProperty().getLocatedIdentifiedAnnotation().getLocation());
        }
      } else {
        boolean cancel = false;
        for (Map.Entry<Property.Base, AnnotationResolver<Type, String>> subProperty :
            entry.subProperties.entrySet()) {
          HookFilter hookFilter =
              subProperty.getKey().getLocatedIdentifiedAnnotation().getAnnotation();

          if (!hookFilter
              .value()
              .test(ctClass, classMappingProvider.get(subProperty.getValue().resolve(hookFilter.type())).getName())) {
            cancel = true;
          }
          if (!cancel) {
            this.modify(
                entry,
                hook,
                ctClass,
                identifier.getProperty().getLocatedIdentifiedAnnotation().getLocation());
          }
        }
      }
    }
  }

  private String arrayClassToString(Class<?> clazz) {
    if (clazz.isArray()) {
      return "[]" + arrayClassToString(clazz.getComponentType());
    } else {
      return "";
    }
  }

  private void insert(CtMethod target, Hook.ExecutionTime executionTime, Method hook) throws CannotCompileException {
    StringBuilder stringBuilder = new StringBuilder();
    for (Class<?> parameterType : hook.getParameterTypes()) {
      String className =
          parameterType.isArray()
              ? parameterType.getComponentType().getName()
              : parameterType.getName();
      if (parameterType.isArray()) {
        className += arrayClassToString(parameterType);
      }

      if (stringBuilder.toString().isEmpty()) {
        stringBuilder.append(className).append(".class");
      } else {
        stringBuilder.append(", ").append(className).append(".class");
      }
    }

    executionTime.insert(
        target,
        "net.labyfy.internal.component.transform.hook.HookService.notify("
            + "this,"
            + "net.labyfy.component.transform.hook.Hook.ExecutionTime."
            + executionTime
            + ","
            + hook.getDeclaringClass().getName()
            + ".class, \""
            + hook.getName()
            + "\", "
            + (stringBuilder.toString().isEmpty()
            ? "new Class[0]"
            : "new Class[]{" + stringBuilder.toString() + "}")
            + ", $args);");
  }

  private void modify(HookEntry hookEntry, Hook hook, CtClass ctClass, Method callback) throws NotFoundException, CannotCompileException {
    CtClass[] parameters = new CtClass[hook.parameters().length];

    for (int i = 0; i < hook.parameters().length; i++) {
      parameters[i] =
          ClassPool.getDefault()
              .get(classMappingProvider.get(hookEntry.parameterTypeNameResolver.resolve(hook.parameters()[i])).getName());
    }

    CtMethod declaredMethod =
        ctClass.getDeclaredMethod(classMappingProvider.get(ctClass.getName()).getMethod(hookEntry.methodNameResolver.resolve(hook), parameters).getName(), parameters);
    if (declaredMethod != null) {
      for (Hook.ExecutionTime executionTime : hook.executionTime()) {
        this.insert(declaredMethod, executionTime, callback);
      }
    }
  }

  private static class HookEntry {
    private final Identifier.Base hook;
    private final Map<Property.Base, AnnotationResolver<Type, String>> subProperties;
    private final AnnotationResolver<Type, String> parameterTypeNameResolver;
    private final AnnotationResolver<Hook, String> methodNameResolver;

    private HookEntry(
        Identifier.Base hook,
        Map<Property.Base, AnnotationResolver<Type, String>> subProperties,
        AnnotationResolver<Type, String> parameterTypeNameResolver,
        AnnotationResolver<Hook, String> methodNameResolver) {
      this.hook = hook;
      this.subProperties = subProperties;
      this.parameterTypeNameResolver = parameterTypeNameResolver;
      this.methodNameResolver = methodNameResolver;
    }
  }
}
