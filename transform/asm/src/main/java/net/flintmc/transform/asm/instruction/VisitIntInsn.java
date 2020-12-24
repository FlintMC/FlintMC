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

import com.google.common.base.Preconditions;
import net.flintmc.transform.asm.AbstractContext;
import net.flintmc.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitIntInsn {

  void visitIntInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int opcode;
    private int operand;

    public Context(MethodVisitorContext methodVisitorContext, int opcode, int operand) {
      this.methodVisitorContext = methodVisitorContext;
      this.opcode = opcode;
      this.operand = operand;
    }

    public static Context of(MethodVisitorContext methodVisitorContext, int opcode, int operand) {
      Preconditions.checkNotNull(methodVisitorContext);
      return new Context(methodVisitorContext, opcode, operand);
    }

    public Context write() {
      this.methodVisitorContext.svisitIntInsn(this.opcode, this.operand);
      return this;
    }

    public MethodVisitorContext getMethodVisitorContext() {
      return methodVisitorContext;
    }

    public int getOpcode() {
      return opcode;
    }

    public Context setOpcode(int opcode) {
      this.opcode = opcode;
      return this;
    }

    public int getOperand() {
      return operand;
    }

    public Context setOperand(int operand) {
      this.operand = operand;
      return this;
    }
  }
}
