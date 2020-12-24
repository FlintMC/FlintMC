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
public interface VisitIincInsn {
  void visitIincInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int var;
    private int increment;

    private Context(MethodVisitorContext methodVisitorContext, int var, int increment) {
      this.methodVisitorContext = methodVisitorContext;
      this.var = var;
      this.increment = increment;
    }

    public static Context of(MethodVisitorContext methodVisitorContext, int var, int increment) {
      return new Context(methodVisitorContext, var, increment);
    }

    public int getVar() {
      return this.var;
    }

    public Context setVar(int var) {
      this.var = var;
      return this;
    }

    public int getIncrement() {
      return this.increment;
    }

    public Context setIncrement(int increment) {
      this.increment = increment;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitIincInsn(this.var, this.increment);
      return this;
    }
  }
}
