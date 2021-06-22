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

package net.flintmc.util.mappings.utils.line;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.FieldMapping;
import net.flintmc.util.mappings.utils.line.handler.LineMappingHandler;
import net.flintmc.util.mappings.utils.line.handler.LineObfuscationMapper;

@Singleton
@LineObfuscationMapper({"mapField", "f"})
public class FieldLineMappingHandler implements LineMappingHandler {

  private final ClassMappingProvider mappingProvider;
  private final ClassPool pool;

  @Inject
  private FieldLineMappingHandler(ClassMappingProvider mappingProvider, ClassPool pool) {
    this.mappingProvider = mappingProvider;
    this.pool = pool;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String mapToObfuscated(MappingLine line, String value) {
    if (line.getCurrentClassName() == null) {
      throw new IllegalArgumentException("Cannot map a field without having a class specified");
    }

    ClassMapping mapping = this.mappingProvider.get(line.getCurrentClassName());
    if (mapping == null) {
      return value;
    }

    FieldMapping field = mapping.getField(value);
    if (field != null) {
      try {
        CtClass type = this.pool.get(mapping.getName()).getField(field.getName()).getType();
        line.setCurrentClassName(type.getName());
      } catch (NotFoundException exception) {
        line.setCurrentClassName(null);

        throw new IllegalArgumentException(
            "Cannot find field " + field.getName() + " in class " + mapping.getName()
                + " for " + value + " in '" + line.getFullLine() + "'");
      }
    }

    return field != null ? field.getName() : value;
  }
}
