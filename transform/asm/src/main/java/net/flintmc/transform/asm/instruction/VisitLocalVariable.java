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
import org.objectweb.asm.Label;

@FunctionalInterface
public interface VisitLocalVariable {

  void visitLocalVariable(Context context);

  class Context implements AbstractContext {

    private final MethodVisitorContext methodVisitorContext;
    private String name;
    private String desc;
    private String signature;
    private Label start;
    private Label end;
    private int index;

    private Context(
        MethodVisitorContext methodVisitorContext,
        String name,
        String desc,
        String signature,
        Label start,
        Label end,
        int index) {
      this.methodVisitorContext = methodVisitorContext;
      this.name = name;
      this.desc = desc;
      this.signature = signature;
      this.start = start;
      this.end = end;
      this.index = index;
    }

    public static Context of(
        MethodVisitorContext methodVisitorContext,
        String name,
        String desc,
        String signature,
        Label start,
        Label end,
        int index) {
      Preconditions.checkNotNull(methodVisitorContext);
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(desc);
      Preconditions.checkNotNull(signature);
      Preconditions.checkNotNull(start);
      Preconditions.checkNotNull(end);
      return new Context(methodVisitorContext, name, desc, signature, start, end, index);
    }

    public String getName() {
      return name;
    }

    public Context setName(String name) {
      this.name = name;
      return this;
    }

    public String getDesc() {
      return desc;
    }

    public Context setDesc(String desc) {
      this.desc = desc;
      return this;
    }

    public String getSignature() {
      return signature;
    }

    public Context setSignature(String signature) {
      this.signature = signature;
      return this;
    }

    public Label getStart() {
      return start;
    }

    public Context setStart(Label start) {
      this.start = start;
      return this;
    }

    public Label getEnd() {
      return end;
    }

    public Context setEnd(Label end) {
      this.end = end;
      return this;
    }

    public int getIndex() {
      return index;
    }

    public Context setIndex(int index) {
      this.index = index;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitLocalVariable(
          this.name, this.desc, this.signature, this.start, this.end, this.index);
      return this;
    }
  }
}
