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

package net.flintmc.mcapi.v1_15_2.player.transformer;

import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import net.flintmc.mcapi.player.FieldOfViewModifier;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;

public class VersionedAbstractClientPlayerTransformer {

  private static final String FOV_MODIFIER_CLASS = FieldOfViewModifier.class.getName();
  private static final int LOCAL_FOV = 1;

  @ClassTransform(value = "net.minecraft.client.entity.player.AbstractClientPlayerEntity")
  public void transform(ClassTransformContext context) throws NotFoundException, BadBytecode {
    CtMethod fovModifierMethod = context.getDeclaredMethod("getFovModifier");

    MethodInfo fovModifierMethodInfo = fovModifierMethod.getMethodInfo();
    CodeAttribute codeAttribute = fovModifierMethodInfo.getCodeAttribute();
    CodeIterator codeIterator = codeAttribute.iterator();

    int lastIndex = -1;
    while (codeIterator.hasNext()) {
      int index = codeIterator.next();
      int opcode = codeIterator.byteAt(index);

      if (opcode == Opcode.FRETURN) {

        Bytecode bytecode = new Bytecode(fovModifierMethodInfo.getConstPool(), 0, 0);

        bytecode.addLdc(fovModifierMethodInfo.getConstPool().addClassInfo(FOV_MODIFIER_CLASS));
        bytecode.addInvokestatic(
            "net.flintmc.framework.inject.primitive.InjectionHolder",
            "getInjectedInstance",
            "(Ljava/lang/Class;)Ljava/lang/Object;");
        bytecode.addCheckcast(FOV_MODIFIER_CLASS);
        bytecode.addFload(LOCAL_FOV);
        bytecode.addInvokeinterface(FOV_MODIFIER_CLASS, "fieldOfView", "(F)F", 2);
        bytecode.addFstore(LOCAL_FOV);

        if (lastIndex != -1) {
          codeIterator.insert(lastIndex, bytecode.get());
        }
      }

      lastIndex = index;
    }
  }
}
