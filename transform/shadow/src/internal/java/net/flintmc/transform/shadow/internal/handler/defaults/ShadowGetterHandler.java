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
import javassist.NotFoundException;
import net.flintmc.metaprogramming.AnnotationMeta;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.internal.ShadowHelper;
import net.flintmc.transform.shadow.internal.handler.RegisterShadowHandler;
import net.flintmc.transform.shadow.internal.handler.ShadowHandler;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.FieldMapping;

@Singleton
@RegisterShadowHandler(value = FieldGetter.class, priority = 3)
public class ShadowGetterHandler implements ShadowHandler<FieldGetter> {

  private final ClassMappingProvider mappingProvider;
  private final ShadowHelper helper;

  @Inject
  private ShadowGetterHandler(ClassMappingProvider mappingProvider, ShadowHelper helper) {
    this.mappingProvider = mappingProvider;
    this.helper = helper;
  }

  @Override
  public void transform(
      CtClass shadowInterface, CtClass implementation, AnnotationMeta<FieldGetter> meta)
      throws ClassTransformException {
    try {
      FieldGetter fieldGetter = meta.getAnnotation();
      CtMethod method = this.helper.getLocation(meta.getIdentifier());

      CtClass[] parameters = method.getParameterTypes();
      if (parameters.length != 0) {
        throw new IllegalArgumentException(
            "Getter " + method + " must not have arguments.");
      }
      if (!this.helper.hasMethod(implementation, method.getName(), parameters)) {
        ClassMapping classMapping = this.mappingProvider.get(implementation.getName());
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

        implementation.addMethod(
            CtMethod.make(
                String.format(
                    "public %s %s(){return this.%s;}",
                    method.getReturnType().getName(), method.getName(), name),
                implementation));
      }
    } catch (NotFoundException | CannotCompileException | ClassNotFoundException exception) {
      throw new ClassTransformException(exception);
    }
  }
}
