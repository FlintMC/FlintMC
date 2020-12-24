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
public interface VisitVarInsn {

  void visitVarInsn(Context context);

  class Context implements AbstractContext {

    private MethodVisitorContext methodVisitorContext;
    private int opcode;
    private int var;

    private Context(MethodVisitorContext methodVisitorContext, int opcode, int var) {
      this.methodVisitorContext = methodVisitorContext;
      this.opcode = opcode;
      this.var = var;
    }

    public static Context of(MethodVisitorContext methodVisitorContext, int opcode, int var) {
      Preconditions.checkNotNull(methodVisitorContext);
      return new Context(methodVisitorContext, opcode, var);
    }

    public MethodVisitorContext getMethodVisitorContext() {
      return methodVisitorContext;
    }

    public void setMethodVisitorContext(MethodVisitorContext methodVisitorContext) {
      this.methodVisitorContext = methodVisitorContext;
    }

    public int getOpcode() {
      return opcode;
    }

    public Context setOpcode(int opcode) {
      this.opcode = opcode;
      return this;
    }

    public int getVar() {
      return var;
    }

    public Context setVar(int var) {
      this.var = var;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitVarInsn(this.opcode, this.var);
      return this;
    }
  }
}
