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

package net.flintmc.transform.asm.instruction;

import net.flintmc.transform.asm.AbstractContext;
import net.flintmc.transform.asm.MethodVisitorContext;
import org.objectweb.asm.Label;

@FunctionalInterface
public interface VisitJumpInsn {
  void visitJumpInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int opcode;
    private Label label;

    private Context(MethodVisitorContext methodVisitorContext, int opcode, Label label) {
      this.methodVisitorContext = methodVisitorContext;
      this.opcode = opcode;
      this.label = label;
    }

    public static Context of(MethodVisitorContext methodVisitorContext, int opcode, Label label) {
      return new Context(methodVisitorContext, opcode, label);
    }

    public int getOpcode() {
      return this.opcode;
    }

    public Context setOpcode(int opcode) {
      this.opcode = opcode;
      return this;
    }

    public Label getLabel() {
      return this.label;
    }

    public Context setLabel(Label label) {
      this.label = label;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitJumpInsn(this.opcode, this.label);
      return this;
    }
  }
}
