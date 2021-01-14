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

package net.flintmc.framework.config.internal.generator.base;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.ConfigImplementer;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.inject.implement.Implement;

@Singleton
@Implement(ConfigImplementer.class)
public class DefaultConfigImplementer implements ConfigImplementer {

  private final ClassPool pool;

  @Inject
  private DefaultConfigImplementer(ClassPool pool) {
    this.pool = pool;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void implementParsedConfig(CtClass implementation, String name)
      throws NotFoundException, CannotCompileException {
    for (CtClass ifc : implementation.getInterfaces()) {
      if (ifc.getName().equals(ParsedConfig.class.getName())) {
        return;
      }
    }

    implementation.addInterface(this.pool.get(ParsedConfig.class.getName()));

    CtField referencesField =
        CtField.make(
            "private final transient java.util.Collection references = new java.util.HashSet();",
            implementation);
    implementation.addField(referencesField);
    implementation.addMethod(CtNewMethod.getter("getConfigReferences", referencesField));

    String escapedName = name.replace("\\", "\\\\").replace("\"", "\\\"");
    implementation.addMethod(
        CtNewMethod.make(
            "public java.lang.String getConfigName() { return \"" + escapedName + "\"; }",
            implementation));
  }
}
