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

package net.flintmc.transform.shadow.internal.handler.defaults;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import net.flintmc.metaprogramming.AnnotationMeta;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.shadow.MethodProxy;
import net.flintmc.transform.shadow.internal.ShadowHelper;
import net.flintmc.transform.shadow.internal.handler.RegisterShadowHandler;
import net.flintmc.transform.shadow.internal.handler.ShadowHandler;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.MethodMapping;

@Singleton
@RegisterShadowHandler(value = MethodProxy.class, priority = 2)
public class ShadowProxyHandler implements ShadowHandler<MethodProxy> {

  private final ClassMappingProvider mappingProvider;
  private final ShadowHelper helper;

  @Inject
  private ShadowProxyHandler(ClassMappingProvider mappingProvider, ShadowHelper helper) {
    this.mappingProvider = mappingProvider;
    this.helper = helper;
  }

  @Override
  public void transform(
      CtClass shadowInterface, CtClass implementation, AnnotationMeta<MethodProxy> meta)
      throws ClassTransformException {
    try {
      MethodProxy annotation = meta.getAnnotation();
      CtMethod proxyMethod = this.helper.getLocation(meta.getIdentifier());

      CtClass[] parameters = proxyMethod.getParameterTypes();
      CtClass[] classes = new CtClass[parameters.length];
      for (int i = 0; i < classes.length; i++) {
        classes[i] = implementation.getClassPool().get(parameters[i].getName());
      }

      CtMethod target;
      ClassMapping classMapping = this.mappingProvider.get(implementation.getName());
      MethodMapping methodMapping =
          classMapping != null ? classMapping
              .getMethod(annotation.value().isEmpty() ? proxyMethod.getName() : annotation.value(),
                  parameters) : null;

      if (methodMapping != null) {
        target = implementation.getDeclaredMethod(methodMapping.getName(), classes);
        if (!methodMapping.getName().equals(proxyMethod.getName())) {
          String src =
              String.format(
                  "public %s %s(%s){return this.%s($$);}",
                  target.getReturnType().getName(), proxyMethod.getName(),
                  this.buildParameters(classes), target.getName());

          implementation.addMethod(CtMethod.make(src, implementation));
        }
      } else {
        target = implementation.getDeclaredMethod(
            annotation.value().isEmpty() ? proxyMethod.getName() : annotation.value(), classes);
      }

      target.setModifiers(Modifier.setPublic(target.getModifiers()));
    } catch (NotFoundException | ClassNotFoundException | CannotCompileException exception) {
      throw new ClassTransformException(exception);
    }
  }

  private String buildParameters(CtClass[] parameters) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < parameters.length; i++) {
      CtClass parameter = parameters[i];
      builder.append(parameter.getName()).append(" _").append(i);

      if (i != parameters.length - 1) {
        builder.append(',');
      }
    }
    return builder.toString();
  }
}
