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
public interface VisitCode {

  void visitCode(Context context);

  class Context implements AbstractContext {

    private final MethodVisitorContext methodVisitorContext;

    private Context(MethodVisitorContext methodVisitorContext) {
      this.methodVisitorContext = methodVisitorContext;
    }

    public static Context of(MethodVisitorContext methodVisitorContext) {
      Preconditions.checkNotNull(methodVisitorContext);
      return new Context(methodVisitorContext);
    }

    public Context write() {
      this.methodVisitorContext.svisitCode();
      return this;
    }
  }
}
