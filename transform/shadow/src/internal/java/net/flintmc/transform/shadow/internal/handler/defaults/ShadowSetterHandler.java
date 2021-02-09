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
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.internal.ShadowHelper;
import net.flintmc.transform.shadow.internal.handler.RegisterShadowHandler;
import net.flintmc.transform.shadow.internal.handler.ShadowHandler;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.FieldMapping;

@Singleton
@RegisterShadowHandler(value = FieldSetter.class, priority = 4)
public class ShadowSetterHandler implements ShadowHandler<FieldSetter> {

  private final ClassMappingProvider mappingProvider;
  private final ShadowHelper helper;

  @Inject
  private ShadowSetterHandler(ClassMappingProvider mappingProvider, ShadowHelper helper) {
    this.mappingProvider = mappingProvider;
    this.helper = helper;
  }

  @Override
  public void transform(
      CtClass shadowInterface, CtClass implementation, AnnotationMeta<FieldSetter> meta)
      throws ClassTransformException {
    try {
      FieldSetter fieldSetter = meta.getAnnotation();
      CtMethod method = this.helper.getLocation(meta.getIdentifier());

      CtClass[] parameters = method.getParameterTypes();
      if (parameters.length != 1) {
        throw new IllegalArgumentException(
            "Setter " + method + " must have one arguments.");
      }
      if (method.getReturnType() != CtClass.voidType) {
        throw new IllegalStateException(
            "Return type for " + method + " must be void");
      }

      String fieldName = this.mapFieldName(implementation.getName(), fieldSetter.value());
      CtField field = implementation.getField(fieldName);

      if (Modifier.isFinal(field.getModifiers())) {
        field.setModifiers(field.getModifiers() & ~Modifier.FINAL);
      }

      CtMethod existingMethod = this.helper.getMethod(implementation, method.getName(), parameters);
      if (existingMethod != null) {
        if (!Modifier.isPublic(existingMethod.getModifiers())) {
          existingMethod.setModifiers(Modifier.setPublic(existingMethod.getModifiers()));
        }
      } else {
        implementation.addMethod(
            CtMethod.make(
                String.format(
                    "public void %s(%s arg){this.%s = arg;}",
                    method.getName(), parameters[0].getName(),
                    fieldName),
                implementation));
      }
    } catch (NotFoundException | ClassNotFoundException | CannotCompileException exception) {
      throw new ClassTransformException(exception);
    }
  }

  private String mapFieldName(String className, String fieldName) {
    ClassMapping classMapping = this.mappingProvider.get(className);
    if (classMapping != null) {
      FieldMapping fieldMapping = classMapping.getField(fieldName);
      if (fieldMapping != null) {
        return fieldMapping.getName();
      }
    }

    return fieldName;
  }
}
