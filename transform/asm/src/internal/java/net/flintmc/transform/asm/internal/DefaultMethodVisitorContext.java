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

package net.flintmc.transform.asm.internal;

import net.flintmc.transform.asm.MethodVisit;
import net.flintmc.transform.asm.MethodVisitorContext;
import net.flintmc.transform.asm.instruction.*;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultMethodVisitorContext extends MethodVisitor implements MethodVisitorContext {

  private final MethodVisit methodVisit;
  private final Collection<Consumer<MethodVisitor>> consumers = new LinkedList<>();
  private final Map<String, Object> assets = new LinkedHashMap<>();
  private VisitMethodInsn visitMethodInsn;
  private VisitInsn visitInsn;
  private VisitLdcInsn visitLdcInsn;
  private VisitCode visitCode;
  private VisitFieldInsn visitFieldInsn;
  private VisitLocalVariable visitLocalVariable;
  private VisitVarInsn visitVarInsn;
  private VisitIntInsn visitIntInsn;
  private VisitInvokeDynamicInsn visitInvokeDynamicInsn;
  private VisitTypeInsn visitTypeInsn;
  private VisitJumpInsn visitJumpInsn;
  private VisitTableSwitchInsn visitTableSwitchInsn;
  private VisitLookupSwitchInsn visitLookupSwitchInsn;
  private VisitMultiANewArrayInsn visitMultiANewArrayInsn;
  private VisitIincInsn visitIincInsn;

  public DefaultMethodVisitorContext(MethodVisit methodVisit) {
    super(Opcodes.ASM5);
    this.methodVisit = methodVisit;
  }

  public MethodVisitorContext onVisitInvokeDynamicInsn(
      VisitInvokeDynamicInsn visitInvokeDynamicInsn) {
    this.visitInvokeDynamicInsn = visitInvokeDynamicInsn;
    return this;
  }

  public MethodVisitorContext onVisitTypeInsn(VisitTypeInsn visitTypeInsn) {
    this.visitTypeInsn = visitTypeInsn;
    return this;
  }

  public MethodVisitorContext onVisitJumpInsn(VisitJumpInsn visitJumpInsn) {
    this.visitJumpInsn = visitJumpInsn;
    return this;
  }

  public MethodVisitorContext onVisitTableSwitchInsn(VisitTableSwitchInsn visitTableSwitchInsn) {
    this.visitTableSwitchInsn = visitTableSwitchInsn;
    return this;
  }

  public MethodVisitorContext onVisitLookupSwitchInsn(VisitLookupSwitchInsn visitLookupSwitchInsn) {
    this.visitLookupSwitchInsn = visitLookupSwitchInsn;
    return this;
  }

  public MethodVisitorContext onVisitMultiANewArrayInsn(
      VisitMultiANewArrayInsn visitMultiANewArrayInsn) {
    this.visitMultiANewArrayInsn = visitMultiANewArrayInsn;
    return this;
  }

  public MethodVisitorContext onVisitIincInsn(VisitIincInsn visitIincInsn) {
    this.visitIincInsn = visitIincInsn;
    return this;
  }

  public MethodVisitorContext onVisitIntInsn(VisitIntInsn visitIntInsn) {
    this.visitIntInsn = visitIntInsn;
    return this;
  }

  public MethodVisitorContext onVisitFieldInsn(VisitFieldInsn visitFieldInsn) {
    this.visitFieldInsn = visitFieldInsn;
    return this;
  }

  public MethodVisitorContext onVisitMethodInsn(VisitMethodInsn visitMethodInsn) {
    this.visitMethodInsn = visitMethodInsn;
    return this;
  }

  public MethodVisitorContext onVisitInsn(VisitInsn visitInsn) {
    this.visitInsn = visitInsn;
    return this;
  }

  public MethodVisitorContext onVisitCode(VisitCode visitCode) {
    this.visitCode = visitCode;
    return this;
  }

  public MethodVisitorContext onVisitLdcInsn(VisitLdcInsn visitLdcInsn) {
    this.visitLdcInsn = visitLdcInsn;
    return this;
  }

  public MethodVisitorContext onVisitLocalVariable(VisitLocalVariable visitLocalVariable) {
    this.visitLocalVariable = visitLocalVariable;
    return this;
  }

  public MethodVisitorContext onVisitVarInsn(VisitVarInsn visitVarInsn) {
    this.visitVarInsn = visitVarInsn;
    return this;
  }

  public void svisitInvokeDynamicInsn(
      String name,
      String descriptor,
      Handle bootstrapMethodHandle,
      Object[] bootstrapMethodArguments) {
    super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
  }

  public void svisitTypeInsn(int opcode, String type) {
    super.visitTypeInsn(opcode, type);
  }

  public void svisitJumpInsn(int opcode, Label label) {
    super.visitJumpInsn(opcode, label);
  }

  public void svisitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
    super.visitTableSwitchInsn(min, max, dflt, labels);
  }

  public void svisitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
    super.visitLookupSwitchInsn(dflt, keys, labels);
  }

  public void svisitMultiANewArrayInsn(String descriptor, int numDimensions) {
    super.visitMultiANewArrayInsn(descriptor, numDimensions);
  }

  public void svisitIincInsn(int var, int increment) {
    super.visitIincInsn(var, increment);
  }

  public void visitVarInsn(int opcode, int var) {
    if (this.visitVarInsn != null) {
      this.visitVarInsn.visitVarInsn(VisitVarInsn.Context.of(this, opcode, var));
    } else {
      super.visitVarInsn(opcode, var);
    }
  }

  public void svisitVarInsn(int opcode, int var) {
    super.visitVarInsn(opcode, var);
  }

  public void visitLocalVariable(
      String name, String desc, String signature, Label start, Label end, int index) {
    if (this.visitLocalVariable != null) {
      this.visitLocalVariable.visitLocalVariable(
          VisitLocalVariable.Context.of(this, name, desc, signature, start, end, index));
    } else {
      super.visitLocalVariable(name, desc, signature, start, end, index);
    }
  }

  public void svisitLocalVariable(
      String name, String desc, String signature, Label start, Label end, int index) {
    super.visitLocalVariable(name, desc, signature, start, end, index);
  }

  public void visitFieldInsn(int opcode, String owner, String name, String desc) {
    if (this.visitFieldInsn != null) {
      this.visitFieldInsn.visitFieldInsn(
          VisitFieldInsn.Context.of(this, opcode, owner, name, desc));
    } else {
      super.visitFieldInsn(opcode, owner, name, desc);
    }
  }

  public void svisitFieldInsn(int opcode, String owner, String name, String desc) {
    super.visitFieldInsn(opcode, owner, name, desc);
  }

  public void visitLdcInsn(Object cst) {
    if (this.visitLdcInsn != null) {
      this.visitLdcInsn.visitLdcInsn(VisitLdcInsn.Context.of(this, cst));
    } else {
      super.visitLdcInsn(cst);
    }
  }

  public void svisitLdcInsn(Object cst) {
    super.visitLdcInsn(cst);
  }

  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
    if (this.visitMethodInsn != null) {
      this.visitMethodInsn.visitMethodInsn(
          VisitMethodInsn.Context.of(this, opcode, owner, name, desc, itf));
    } else {
      super.visitMethodInsn(opcode, owner, name, desc, itf);
    }
  }

  public void svisitMethodInsn(int opcode, String owner, String name, String dest, boolean itf) {
    super.visitMethodInsn(opcode, owner, name, dest, itf);
  }

  public void svisitMethodInsn(int opcode, String owner, String name, String dest) {
    super.visitMethodInsn(opcode, owner, name, dest);
  }

  public void visitInsn(int opcode) {
    if (this.visitInsn != null) {
      this.visitInsn.visitInsn(VisitInsn.Context.of(this, opcode));
    } else {
      super.visitInsn(opcode);
    }
  }

  public void svisitIntInsn(int opcode, int operand) {
    super.visitIntInsn(opcode, operand);
  }

  public void visitIntInsn(int opcode, int operand) {
    if (this.visitIntInsn != null) {
      this.visitIntInsn.visitIntInsn(VisitIntInsn.Context.of(this, opcode, operand));
    } else {
      super.visitIntInsn(opcode, operand);
    }
  }

  public void svisitInsn(int opcode) {
    super.visitInsn(opcode);
  }

  public void visitInvokeDynamicInsn(
      String name,
      String descriptor,
      Handle bootstrapMethodHandle,
      Object... bootstrapMethodArguments) {
    if (this.visitInvokeDynamicInsn != null) {
      this.visitInvokeDynamicInsn.visitInvokeDynamicInsn(
          VisitInvokeDynamicInsn.Context.of(
              this, name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
    } else {
      super.visitInvokeDynamicInsn(
          name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }
  }

  public void visitTypeInsn(int opcode, String type) {
    if (this.visitTypeInsn != null) {
      this.visitTypeInsn.visitTypeInsn(VisitTypeInsn.Context.of(this, opcode, type));
    } else {
      super.visitTypeInsn(opcode, type);
    }
  }

  public void visitJumpInsn(int opcode, Label label) {
    if (this.visitJumpInsn != null) {
      this.visitJumpInsn.visitJumpInsn(VisitJumpInsn.Context.of(this, opcode, label));
    } else {
      super.visitJumpInsn(opcode, label);
    }
  }

  public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
    if (this.visitTableSwitchInsn != null) {
      this.visitTableSwitchInsn.visitTableSwitchInsn(
          VisitTableSwitchInsn.Context.of(this, min, max, dflt, labels));
    } else {
      super.visitTableSwitchInsn(min, max, dflt, labels);
    }
  }

  public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
    if (this.visitLookupSwitchInsn != null) {
      this.visitLookupSwitchInsn.visitLookupSwitchInsn(
          VisitLookupSwitchInsn.Context.of(this, dflt, keys, labels));
    } else {
      super.visitLookupSwitchInsn(dflt, keys, labels);
    }
  }

  public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
    if (this.visitMultiANewArrayInsn != null) {
      this.visitMultiANewArrayInsn.visitMultiANewArrayInsn(
          VisitMultiANewArrayInsn.Context.of(this, descriptor, numDimensions));
    } else {
      super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }
  }

  public void visitIincInsn(int var, int increment) {
    if (this.visitIincInsn != null) {
      this.visitIincInsn.visitIincInsn(VisitIincInsn.Context.of(this, var, increment));
    } else {
      super.visitIincInsn(var, increment);
    }
  }

  public void visitCode() {
    if (this.visitCode != null) {
      this.visitCode.visitCode(VisitCode.Context.of(this));
    } else {
      super.visitCode();
    }
  }

  public void svisitCode() {
    super.visitCode();
  }

  public MethodVisit getMethodVisit() {
    return methodVisit;
  }

  public MethodVisitorContext storeAsset(String key, Object value) {
    this.assets.put(key, value);
    return this;
  }

  public <T> T getAsset(String key) {
    return ((T) this.assets.get(key));
  }

  public MethodVisitorContext setMethodVisitor(MethodVisitor mv) {
    this.mv = mv;
    return this;
  }
}
