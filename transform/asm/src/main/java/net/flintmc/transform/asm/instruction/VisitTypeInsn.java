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

@FunctionalInterface
public interface VisitTypeInsn {
  void visitTypeInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int opcode;
    private String type;

    private Context(MethodVisitorContext methodVisitorContext, int opcode, String type) {
      this.methodVisitorContext = methodVisitorContext;
      this.opcode = opcode;
      this.type = type;
    }

    public static Context of(MethodVisitorContext methodVisitorContext, int opcode, String type) {
      return new Context(methodVisitorContext, opcode, type);
    }

    public int getOpcode() {
      return this.opcode;
    }

    public Context setOpcode(int opcode) {
      this.opcode = opcode;
      return this;
    }

    public String getType() {
      return this.type;
    }

    public Context setType(String type) {
      this.type = type;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitTypeInsn(this.opcode, this.type);
      return this;
    }
  }
}
