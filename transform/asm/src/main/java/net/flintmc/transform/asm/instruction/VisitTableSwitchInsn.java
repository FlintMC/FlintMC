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
public interface VisitTableSwitchInsn {
  void visitTableSwitchInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int min;
    private int max;
    private Label dflt;
    private Label[] labels;

    private Context(
        MethodVisitorContext methodVisitorContext, int min, int max, Label dflt, Label... labels) {
      this.methodVisitorContext = methodVisitorContext;
      this.min = min;
      this.max = max;
      this.dflt = dflt;
      this.labels = labels;
    }

    public static Context of(
        MethodVisitorContext methodVisitorContext, int min, int max, Label dflt, Label... labels) {
      return new Context(methodVisitorContext, min, max, dflt, labels);
    }

    public int getMin() {
      return this.min;
    }

    public Context setMin(int min) {
      this.min = min;
      return this;
    }

    public int getMax() {
      return this.max;
    }

    public Context setMax(int max) {
      this.max = max;
      return this;
    }

    public Label getDflt() {
      return this.dflt;
    }

    public Context setDflt(Label dflt) {
      this.dflt = dflt;
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
      this.methodVisitorContext.svisitTableSwitchInsn(this.min, this.max, this.dflt, this.labels);
      return this;
    }
  }
}
