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
import javassist.NotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.shadow.FieldCreate;
import net.flintmc.transform.shadow.internal.handler.RegisterShadowHandler;
import net.flintmc.transform.shadow.internal.handler.ShadowHandler;
import net.flintmc.util.mappings.utils.line.MappingLineParser;

@Singleton
@RegisterShadowHandler(value = FieldCreate.class, priority = 1)
public class ShadowFieldCreateHandler implements ShadowHandler<FieldCreate> {

  private final MappingLineParser lineParser;

  @Inject
  private ShadowFieldCreateHandler(MappingLineParser lineParser) {
    this.lineParser = lineParser;
  }

  @Override
  public void transform(
      CtClass shadowInterface, CtClass implementation, AnnotationMeta<FieldCreate> meta)
      throws ClassTransformException {
    FieldCreate fieldCreate = meta.getAnnotation();
    for (CtField field : implementation.getFields()) {
      if (field.getName().equals(fieldCreate.name())) {
        return;
      }
    }

    try {
      CtField ctField =
          new CtField(
              implementation.getClassPool().get(fieldCreate.typeName()),
              fieldCreate.name(), implementation);
      if (fieldCreate.defaultValue().isEmpty()) {
        implementation.addField(ctField);
      } else {
        String line = this.lineParser.translateMappings(fieldCreate.defaultValue());
        implementation.addField(ctField, line);
      }
    } catch (NotFoundException exception) {
      throw new ClassTransformException("Type " + fieldCreate.typeName() + " not found", exception);
    } catch (CannotCompileException exception) {
      throw new ClassTransformException("Cannot compile created field", exception);
    }
  }
}
