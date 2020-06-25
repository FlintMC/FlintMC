package net.labyfy.component.transform.asm;

import net.labyfy.component.transform.asm.instruction.*;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;

public class MethodVisitorContext extends MethodVisitor {

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

  public MethodVisitorContext(MethodVisit methodVisit) {
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
          java.lang.String arg0, java.lang.String arg1, org.objectweb.asm.Handle arg2, Object[] arg3) {
    super.visitInvokeDynamicInsn(arg0, arg1, arg2, arg3);
  }

  public void svisitTypeInsn(int arg0, java.lang.String arg1) {
    super.visitTypeInsn(arg0, arg1);
  }

  public void svisitJumpInsn(int arg0, org.objectweb.asm.Label arg1) {
    super.visitJumpInsn(arg0, arg1);
  }

  public void svisitTableSwitchInsn(
          int arg0, int arg1, org.objectweb.asm.Label arg2, org.objectweb.asm.Label[] arg3) {
    super.visitTableSwitchInsn(arg0, arg1, arg2, arg3);
  }

  public void svisitLookupSwitchInsn(
          org.objectweb.asm.Label arg0, int[] arg1, org.objectweb.asm.Label[] arg2) {
    super.visitLookupSwitchInsn(arg0, arg1, arg2);
  }

  public void svisitMultiANewArrayInsn(java.lang.String arg0, int arg1) {
    super.visitMultiANewArrayInsn(arg0, arg1);
  }

  public void svisitIincInsn(int arg0, int arg1) {
    super.visitIincInsn(arg0, arg1);
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
          java.lang.String arg0, java.lang.String arg1, org.objectweb.asm.Handle arg2, Object[] arg3) {
    if (this.visitInvokeDynamicInsn != null) {
      this.visitInvokeDynamicInsn.visitInvokeDynamicInsn(
              VisitInvokeDynamicInsn.Context.of(this, arg0, arg1, arg2, arg3));
    } else {
      super.visitInvokeDynamicInsn(arg0, arg1, arg2, arg3);
    }
  }

  public void visitTypeInsn(int arg0, java.lang.String arg1) {
    if (this.visitTypeInsn != null) {
      this.visitTypeInsn.visitTypeInsn(VisitTypeInsn.Context.of(this, arg0, arg1));
    } else {
      super.visitTypeInsn(arg0, arg1);
    }
  }

  public void visitJumpInsn(int arg0, org.objectweb.asm.Label arg1) {
    if (this.visitJumpInsn != null) {
      this.visitJumpInsn.visitJumpInsn(VisitJumpInsn.Context.of(this, arg0, arg1));
    } else {
      super.visitJumpInsn(arg0, arg1);
    }
  }

  public void visitTableSwitchInsn(
          int arg0, int arg1, org.objectweb.asm.Label arg2, org.objectweb.asm.Label[] arg3) {
    if (this.visitTableSwitchInsn != null) {
      this.visitTableSwitchInsn.visitTableSwitchInsn(
              VisitTableSwitchInsn.Context.of(this, arg0, arg1, arg2, arg3));
    } else {
      super.visitTableSwitchInsn(arg0, arg1, arg2, arg3);
    }
  }

  public void visitLookupSwitchInsn(
          org.objectweb.asm.Label arg0, int[] arg1, org.objectweb.asm.Label[] arg2) {
    if (this.visitLookupSwitchInsn != null) {
      this.visitLookupSwitchInsn.visitLookupSwitchInsn(
              VisitLookupSwitchInsn.Context.of(this, arg0, arg1, arg2));
    } else {
      super.visitLookupSwitchInsn(arg0, arg1, arg2);
    }
  }

  public void visitMultiANewArrayInsn(java.lang.String arg0, int arg1) {
    if (this.visitMultiANewArrayInsn != null) {
      this.visitMultiANewArrayInsn.visitMultiANewArrayInsn(
              VisitMultiANewArrayInsn.Context.of(this, arg0, arg1));
    } else {
      super.visitMultiANewArrayInsn(arg0, arg1);
    }
  }

  public void visitIincInsn(int arg0, int arg1) {
    if (this.visitIincInsn != null) {
      this.visitIincInsn.visitIincInsn(VisitIincInsn.Context.of(this, arg0, arg1));
    } else {
      super.visitIincInsn(arg0, arg1);
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
