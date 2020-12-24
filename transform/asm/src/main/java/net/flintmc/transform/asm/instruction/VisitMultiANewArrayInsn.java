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
public interface VisitMultiANewArrayInsn {
  void visitMultiANewArrayInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private String descriptor;
    private int numDimensions;

    private Context(
        MethodVisitorContext methodVisitorContext, String descriptor, int numDimensions) {
      this.methodVisitorContext = methodVisitorContext;
      this.descriptor = descriptor;
      this.numDimensions = numDimensions;
    }

    public static Context of(
        MethodVisitorContext methodVisitorContext, String descriptor, int numDimensions) {
      return new Context(methodVisitorContext, descriptor, numDimensions);
    }

    public String getDescriptor() {
      return this.descriptor;
    }

    public Context setDescriptor(String descriptor) {
      this.descriptor = descriptor;
      return this;
    }

    public int getNumDimensions() {
      return this.numDimensions;
    }

    public Context setNumDimensions(int numDimensions) {
      this.numDimensions = numDimensions;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitMultiANewArrayInsn(this.descriptor, this.numDimensions);
      return this;
    }
  }
}
