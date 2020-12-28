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
public interface VisitLookupSwitchInsn {
  void visitLookupSwitchInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private Label dflt;
    private int[] keys;
    private Label[] labels;

    private Context(
        MethodVisitorContext methodVisitorContext, Label dflt, int[] keys, Label... labels) {
      this.methodVisitorContext = methodVisitorContext;
      this.dflt = dflt;
      this.keys = keys;
      this.labels = labels;
    }

    public static Context of(
        MethodVisitorContext methodVisitorContext, Label dflt, int[] keys, Label... labels) {
      return new Context(methodVisitorContext, dflt, keys, labels);
    }

    public Label getDflt() {
      return this.dflt;
    }

    public Context setDflt(Label dflt) {
      this.dflt = dflt;
      return this;
    }

    public int[] getKeys() {
      return this.keys;
    }

    public Context setKeys(int[] keys) {
      this.keys = keys;
      return this;
    }

    public Label[] getLabels() {
      return this.labels;
    }

    public Context setLabels(Label[] labels) {
      this.labels = labels;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitLookupSwitchInsn(this.dflt, this.keys, this.labels);
      return this;
    }
  }
}
