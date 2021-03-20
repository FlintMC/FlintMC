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

package net.flintmc.util.i18n.v1_15_2.transformer;

import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.i18n.Localization;

/**
 * A transformer that adds the {@link Localization} interface to Minecraft locale.
 */
@Singleton
public class LocaleTransformer {

  @ClassTransform(value = "net.minecraft.client.resources.Locale")
  public void transform(ClassTransformContext context)
      throws NotFoundException, CannotCompileException {
    CtClass ctClass = context.getCtClass();

    CtClass localization = ctClass.getClassPool().get(Localization.class.getName());
    ctClass.addInterface(localization);

    ctClass.addMethod(
        CtMethod.make(
            "public java.util.Map getProperties() {" + "return " + context.getField("properties")
                .getName() + ";}", ctClass));
    ctClass.addMethod(
        CtMethod.make(
            "public void add(String key, String translation) {" + context.getField("properties")
                .getName() + ".put($$);}", ctClass));
  }
}
