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

package net.flintmc.transform.hook.internal;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import net.flintmc.framework.inject.method.MethodInjectionUtils;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.DefaultValues;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.HookFilter;
import net.flintmc.transform.hook.HookResult;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.commons.resolve.AnnotationResolver;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;

import java.util.Collection;
import java.util.Map;

@Singleton
@Service(value = Hook.class, priority = -20000, state = Service.State.AFTER_IMPLEMENT)
public class HookService implements ServiceHandler<Hook> {

  private final ClassPool pool;
  private final ClassMappingProvider mappingProvider;
  private final Provider<MethodInjectionUtils> methodInjectionUtils;
  private final String version;
  private final Collection<HookEntry> hooks;

  @Inject
  private HookService(
      ClassPool pool,
      ClassMappingProvider mappingProvider,
      Provider<MethodInjectionUtils> methodInjectionUtils,
      @Named("launchArguments") Map launchArguments) {
    this.pool = pool;
    this.mappingProvider = mappingProvider;
    this.methodInjectionUtils = methodInjectionUtils;
    this.hooks = Sets.newHashSet();
    this.version = (String) launchArguments.get("--game-version");
  }

  @Override
  public void discover(AnnotationMeta<Hook> meta) {
    Map<AnnotationMeta<HookFilter>, AnnotationResolver<Type, String>> subProperties =
        Maps.newHashMap();

    for (AnnotationMeta<HookFilter> subProperty : meta.getMetaData(HookFilter.class)) {
      subProperties.put(
          subProperty,
          InjectionHolder.getInjectedInstance(
              subProperty.getAnnotation().type().typeNameResolver()));
    }

    Hook annotation = meta.getAnnotation();

    if (!(annotation.version().isEmpty() || annotation.version().equals(this.version))) return;
    this.hooks.add(
        new HookEntry(
            meta,
            subProperties,
            InjectionHolder.getInjectedInstance(annotation.parameterTypeNameResolver()),
            InjectionHolder.getInjectedInstance(annotation.methodNameResolver())));
  }

  @ClassTransform
  public void transform(ClassTransformContext context)
      throws NotFoundException, CannotCompileException {
    CtClass ctClass = context.getCtClass();

    for (HookEntry entry : hooks) {
      AnnotationMeta<Hook> identifier = entry.hook;

      Hook hook = identifier.getAnnotation();
      if (!hook.className().isEmpty()) {
        String className = this.mappingProvider.get(hook.className()).getName();
        if (className != null && className.equals(ctClass.getName())) {
          this.modify(entry, hook, ctClass, identifier.getMethodIdentifier().getLocation());
        }
      } else {
        boolean cancel = false;
        for (Map.Entry<AnnotationMeta<HookFilter>, AnnotationResolver<Type, String>> subProperty :
            entry.subProperties.entrySet()) {
          HookFilter hookFilter = subProperty.getKey().getAnnotation();

          if (!hookFilter
              .value()
              .test(
                  ctClass,
                  this.mappingProvider
                      .get(subProperty.getValue().resolve(hookFilter.type()))
                      .getName())) {
            cancel = true;
          }
          if (!cancel) {
            this.modify(entry, hook, ctClass, identifier.getMethodIdentifier().getLocation());
          }
        }
      }
    }
  }

  private void insert(
      CtBehavior target, Hook hook, Hook.ExecutionTime executionTime, CtMethod hookMethod)
      throws CannotCompileException, NotFoundException {
    CtMethod getter =
        this.methodInjectionUtils
            .get()
            .generateInjector(target.getDeclaringClass(), hookMethod, HookInjector.class);

    // getMethodInjector().notifyHook(instance, args, executionTime)
    String notify =
        String.format(
            "%s.%s().notifyHook(%s, $args, net.flintmc.transform.hook.Hook.ExecutionTime.%s);",
            getter.getDeclaringClass().getName(),
            getter.getName(),
            Modifier.isStatic(target.getModifiers()) ? "null" : "$0",
            executionTime);
    String varName = "hookNotifyResult";

    String returnValue = null;
    String returnTypeName =
        target instanceof CtMethod ? ((CtMethod) target).getReturnType().getName() : "void";
    CtClass hookType = hookMethod.getReturnType();
    boolean hookResult = hookType.getName().equals(HookResult.class.getName());

    if (!hookType.getName().equals("void") && !returnTypeName.equals("void")) {
      returnValue = varName;
    }

    if (hookResult) {
      returnValue =
          hook.defaultValue().isEmpty()
              ? DefaultValues.getDefaultValue(returnTypeName)
              : hook.defaultValue();
    }

    if (executionTime == Hook.ExecutionTime.AFTER && returnTypeName.equals("void")) {
      // nothing to return, the method is already done
      returnValue = null;
    }

    if (returnValue == null) { // no values can be returned, no HookResult defined
      executionTime.insert(target, notify);
      return;
    }

    String src = "Object " + varName + " = " + notify;
    if (hookResult) {
      // check if the returned type is equal to BREAK
      String breakCheck =
          varName + " == " + HookResult.class.getName() + "." + HookResult.BREAK.name();

      src += " if (" + breakCheck + ") { return " + returnValue + "; }";
    } else {
      String returnPrefix = "(" + returnTypeName + ")";

      // Avoid VerifyErrors if there is already a return statement in the source, e.g. in a simple
      // getter
      src += "if (true) { return " + returnPrefix + " " + returnValue + "; }";
    }

    executionTime.insert(target, "{ " + src + " }");
  }

  private void modify(HookEntry hookEntry, Hook hook, CtClass ctClass, CtMethod callback)
      throws NotFoundException, CannotCompileException {
    CtClass[] parameters = new CtClass[hook.parameters().length];

    for (int i = 0; i < hook.parameters().length; i++) {
      String name = hookEntry.parameterTypeNameResolver.resolve(hook.parameters()[i]);
      ClassMapping classMapping = this.mappingProvider.get(name);

      if (classMapping == null) {
        classMapping = new ClassMapping(false, name, name);
      }

      parameters[i] = this.pool.get(classMapping.getName());
    }

    boolean constructor = hook.methodName().equals("<init>");

    CtBehavior target =
        constructor
            ? ctClass.getDeclaredConstructor(parameters)
            : ctClass.getDeclaredMethod(
                this.mappingProvider
                    .get(ctClass.getName())
                    .getMethod(hookEntry.methodNameResolver.resolve(hook), parameters)
                    .getName(),
                parameters);

    if (target != null) {
      for (Hook.ExecutionTime executionTime : hook.executionTime()) {
        this.insert(target, hook, executionTime, callback);
      }
    }
  }

  private static class HookEntry {
    private final AnnotationResolver<Type, String> parameterTypeNameResolver;
    private final AnnotationResolver<Hook, String> methodNameResolver;
    AnnotationMeta<Hook> hook;
    Map<AnnotationMeta<HookFilter>, AnnotationResolver<Type, String>> subProperties;

    private HookEntry(
        AnnotationMeta<Hook> hook,
        Map<AnnotationMeta<HookFilter>, AnnotationResolver<Type, String>> subProperties,
        AnnotationResolver<Type, String> parameterTypeNameResolver,
        AnnotationResolver<Hook, String> methodNameResolver) {
      this.hook = hook;
      this.subProperties = subProperties;
      this.parameterTypeNameResolver = parameterTypeNameResolver;
      this.methodNameResolver = methodNameResolver;
    }
  }
}
