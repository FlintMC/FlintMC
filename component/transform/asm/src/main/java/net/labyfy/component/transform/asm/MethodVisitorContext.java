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

  public MethodVisitorContext(MethodVisit methodVisit) {
    super(Opcodes.ASM5);
    this.methodVisit = methodVisit;
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

  public void visitVarInsn(int opcode, int var) {
    if (this.visitVarInsn != null) {
      this.visitVarInsn.visitVarInsn(VisitVarInsn.Context.create(this, opcode, var));
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
          VisitLocalVariable.Context.create(this, name, desc, signature, start, end, index));
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
          VisitFieldInsn.Context.create(this, opcode, owner, name, desc));
    } else {
      super.visitFieldInsn(opcode, owner, name, desc);
    }
  }

  public void svisitFieldInsn(int opcode, String owner, String name, String desc) {
    super.visitFieldInsn(opcode, owner, name, desc);
  }

  public void visitLdcInsn(Object cst) {
    if (this.visitLdcInsn != null) {
      this.visitLdcInsn.visitLdcInsn(VisitLdcInsn.Context.create(this, cst));
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
          VisitMethodInsn.Context.create(this, opcode, owner, name, desc, itf));
    } else {
      super.visitMethodInsn(opcode, owner, name, desc, itf);
    }
  }

  public void svisitMethodInsn(int opcode, String owner, String name, String dest, boolean itf) {
    super.visitMethodInsn(opcode, owner, name, dest, itf);
  }

  public void visitInsn(int opcode) {
    if (this.visitInsn != null) {
      this.visitInsn.visitInsn(VisitInsn.Context.create(this, opcode));
    } else {
      super.visitInsn(opcode);
    }
  }

  public void svisitInsn(int opcode) {
    super.visitInsn(opcode);
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
