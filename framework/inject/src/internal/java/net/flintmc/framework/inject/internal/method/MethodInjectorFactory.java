/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.framework.inject.internal.method;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.method.MethodInjector;
import net.flintmc.framework.inject.method.MethodInjectorGenerationException;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.util.commons.javassist.DefaultValues;
import net.flintmc.util.commons.javassist.CtResolver;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@inheritDoc}
 */
@Singleton
@Implement(MethodInjector.Factory.class)
public class MethodInjectorFactory implements MethodInjector.Factory {

  private final Logger logger;
  private final ClassPool pool;
  private final AtomicInteger idCounter;
  private final Map<Integer, MethodInjector> injectorCache;

  @Inject
  private MethodInjectorFactory(@InjectLogger Logger logger) {
    this.logger = logger;
    this.pool = ClassPool.getDefault();
    this.idCounter = new AtomicInteger();
    this.injectorCache = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> T generate(CtMethod targetMethod, Class<T> ifc) {
    return this.generateAndCache(targetMethod, ifc);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> T generate(Object instance, CtMethod targetMethod, Class<T> ifc) {
    T t = this.generateAndCache(targetMethod, ifc);
    ((MethodInjector) t).setInstance(instance);
    return t;
  }

  @SuppressWarnings("unchecked")
  private <T> T generateAndCache(CtMethod targetMethod, Class<T> ifc) {
    int hash = CtResolver.hash(targetMethod);
    if (this.injectorCache.containsKey(hash)) {
      return (T) this.injectorCache.get(hash);
    }

    MethodInjector injector = this.generateInternal(targetMethod, ifc);

    this.injectorCache.put(hash, injector);
    return (T) injector;
  }

  private MethodInjector generateInternal(CtMethod targetMethod, Class<?> ifc) {
    if (!ifc.isInterface()) {
      throw new MethodInjectorGenerationException(
          "Cannot generate method injector for class "
              + ifc.getName()
              + " because it is not an interface");
    }

    try {
      return this.generateInternal0(targetMethod, ifc);
    } catch (Exception exception) {
      throw new MethodInjectorGenerationException(
          String.format(
              "Failed to generate a method injector for %s.%s based on the source from %s",
              targetMethod.getDeclaringClass().getName(), targetMethod.getName(), ifc.getName()),
          exception);
    }
  }

  private CtClass makeNewClass() {
    return this.pool.makeClass(
        "GeneratedMethodInjector_"
            + this.idCounter.incrementAndGet()
            + "_"
            + UUID.randomUUID().toString().replace("-", ""));
  }

  private MethodInjector generateInternal0(CtMethod targetMethod, Class<?> ifc)
      throws CannotCompileException, IOException, NotFoundException, ReflectiveOperationException {
    Method[] methods = ifc.getDeclaredMethods();
    if (methods.length != 1) {
      throw new IllegalArgumentException(
          "Cannot generate MethodInjector for an interface with more or less than 1 method");
    }
    Method method = methods[0];

    CtClass generated = this.makeNewClass();

    if (!targetMethod.getDeclaringClass().isFrozen()) {
      targetMethod.setModifiers(Modifier.setPublic(targetMethod.getModifiers()));
    }

    if (!Modifier.isPublic(targetMethod.getModifiers())) {
      this.logger.trace(
          "Cannot change accessor of method {}.{} to public, the method cannot be invoked",
          targetMethod.getDeclaringClass().getName(),
          targetMethod.getName());
    }

    this.addBase(generated, ifc, targetMethod.getDeclaringClass().getName());

    Method resolvedTargetMethod = CtResolver.get(targetMethod);
    List<Dependency<?>> targetDependencies = this.generateDependencies(resolvedTargetMethod);
    InjectionSource[] args = this.buildArgs(targetDependencies, resolvedTargetMethod, method);

    CtMethod generatedMethod =
        this.generateImplementation(generated, args, resolvedTargetMethod, method);
    generated.addMethod(generatedMethod);

    return this.finalize(generated, args);
  }

  private void addBase(CtClass generated, Class<?> ifc, String targetClass)
      throws CannotCompileException, NotFoundException {
    // interfaces
    generated.addInterface(this.pool.get(MethodInjector.class.getName()));
    generated.addInterface(this.pool.get(ifc.getName()));

    // fields
    generated.addField(
        CtField.make(
            String.format("private final %s[] args;", InjectionSource.class.getName()), generated));
    generated.addField(CtField.make(String.format("private %s instance;", targetClass), generated));
    generated.addField(CtField.make("private Object[] constantArgs;", generated));

    // constructor
    generated.addConstructor(
        CtNewConstructor.make(
            String.format(
                "public %s(%s[] args) { this.args = args; }",
                generated.getName(), InjectionSource.class.getName()),
            generated));

    // methods
    generated.addMethod(
        CtNewMethod.make(
            String.format(
                "private %s getCastedInstance() {"
                    + "if (this.instance == null) {"
                    + "  this.instance = (%1$s) %s.getInjectedInstance(%1$s.class);"
                    + "}"
                    + "return this.instance;"
                    + "}",
                targetClass, InjectionHolder.class.getName()),
            generated));
    generated.addMethod(
        CtNewMethod.make(
            "public Object getInstance() { return (Object) this.getCastedInstance(); }",
            generated));

    generated.addMethod(
        CtNewMethod.make(
            "public void setInstance(Object instance) {"
                + String.format("this.instance = (%s) instance;", targetClass)
                + "}",
            generated));
  }

  private CtMethod generateImplementation(
      CtClass declaring, InjectionSource[] args, Method targetMethod, Method method)
      throws CannotCompileException {
    StringBuilder body = new StringBuilder();

    body.append(
        String.format(
            "if (this.constantArgs == null) { this.constantArgs = %s.generateConstantArgs(this.args); }",
            super.getClass().getName()));

    boolean targetReturn = !targetMethod.getReturnType().equals(Void.TYPE);
    boolean sourceReturn = !method.getReturnType().equals(Void.TYPE);
    if (targetReturn && sourceReturn) {
      body.append("return ");
    }

    body.append("this.getCastedInstance().").append(targetMethod.getName()).append('(');

    for (int i = 0; i < args.length; i++) {
      InjectionSource source = args[i];
      body.append('(').append(this.buildTypeName(source.getType())).append(')');

      if (source.isFromInjector()) {
        body.append("this.constantArgs[").append(i).append(']');
      } else {
        body.append('$').append(source.getSourceIndex() + 1);
        // + 1 - Javassist uses 0 for this and the parameters begin at 1
      }

      if (i != args.length - 1) {
        body.append(',');
      }
    }

    body.append(");");

    if (sourceReturn && !targetReturn) {
      body.append("return ")
          .append(DefaultValues.getDefaultValue(method.getReturnType().getName()))
          .append(';');
    }

    return this.buildWrappedMethod(method, body.toString(), declaring);
  }

  private InjectionSource[] buildArgs(
      List<Dependency<?>> targetDependencies, Method targetMethod, Method method) {
    InjectionSource[] args = new InjectionSource[targetDependencies.size()];

    List<Dependency<?>> dependencies = this.generateDependencies(method);
    Class<?>[] targetParameters = targetMethod.getParameterTypes();

    for (int targetIndex = 0; targetIndex < targetDependencies.size(); targetIndex++) {
      Key<?> key = targetDependencies.get(targetIndex).getKey();
      int sourceIndex = this.getIndex(dependencies, key);
      Class<?> type = targetParameters[targetIndex];

      args[targetIndex] = new InjectionSource(sourceIndex, key, type);
    }

    return args;
  }

  /**
   * Searches for the given key in the given dependencies list and retrieves the index of the key in
   * the list. Also includes super classes and interfaces from the given key.
   */
  private int getIndex(List<Dependency<?>> dependencies, Key<?> key) {
    int baseIndex = this.findRawIndex(dependencies, key);
    if (baseIndex != -1) {
      return baseIndex;
    }

    Collection<Class<?>> superClasses = new ArrayList<>();
    this.addSuperClasses(superClasses, key.getTypeLiteral().getRawType());

    for (Class<?> superClass : superClasses) {
      int i = this.findRawIndex(dependencies, key.ofType(superClass));
      if (i != -1) {
        return i;
      }
    }

    return -1;
  }

  private int findRawIndex(List<Dependency<?>> dependencies, Key<?> key) {
    for (int i = 0; i < dependencies.size(); i++) {
      if (dependencies.get(i).getKey().equals(key)) {
        return i;
      }
    }
    return -1;
  }

  private void addSuperClasses(Collection<Class<?>> target, Class<?> clazz) {
    do {
      if (target.contains(clazz)) {
        continue;
      }

      target.add(clazz);
      this.addInterfaces(target, clazz);
    } while ((clazz = clazz.getSuperclass()) != null && clazz != Object.class);
  }

  private void addInterfaces(Collection<Class<?>> target, Class<?> clazz) {
    for (Class<?> ifc : clazz.getInterfaces()) {
      if (target.contains(ifc)) {
        continue;
      }

      this.addInterfaces(target, ifc);
      target.add(ifc);
    }
  }

  private CtMethod buildWrappedMethod(Method wrapped, String body, CtClass declaring)
      throws CannotCompileException {
    return CtMethod.make(
        String.format(
            "public %s %s(%s) {" + body + "}",
            this.buildTypeName(wrapped.getReturnType()),
            wrapped.getName(),
            this.buildSourceParameterList(wrapped)),
        declaring);
  }

  private String buildSourceParameterList(Method method) {
    StringBuilder parameters = new StringBuilder();
    int i = 0;
    for (Class<?> parameter : method.getParameterTypes()) {
      parameters.append(this.buildTypeName(parameter)).append(" arg").append(i++).append(", ");
    }

    return parameters.length() == 0 ? "" : parameters.substring(0, parameters.length() - 2);
  }

  private String buildTypeName(Class<?> type) {
    String suffix = "";

    if (type.isArray()) {
      StringBuilder array = new StringBuilder();
      while (type.isArray()) {
        array.append("[]");
        type = type.getComponentType();
      }
      suffix = array.toString();
    }

    return type.getName() + suffix;
  }

  private List<Dependency<?>> generateDependencies(Method method) {
    return InjectionPoint.forMethod(method, TypeLiteral.get(method.getDeclaringClass()))
        .getDependencies();
  }

  /**
   * Called from generated code, see above.
   */
  public static Object[] generateConstantArgs(InjectionSource[] args) {
    Object[] constantArgs = new Object[args.length];
    Injector injector = InjectionHolder.getInstance().getInjector();

    for (int i = 0; i < constantArgs.length; i++) {
      if (!args[i].isFromInjector()) {
        // the argument has already been found in the parameters of the method
        continue;
      }

      constantArgs[i] = injector.getInstance(args[i].getKey());
    }

    return constantArgs;
  }

  private MethodInjector finalize(CtClass generated, InjectionSource[] args)
      throws IOException, CannotCompileException, ReflectiveOperationException {
    Class<?> resolved = CtResolver.defineClass(generated);

    return (MethodInjector)
        resolved.getDeclaredConstructor(InjectionSource[].class).newInstance(new Object[]{args});
  }
}
