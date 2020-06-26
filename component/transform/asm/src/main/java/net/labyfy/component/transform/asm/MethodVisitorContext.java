package net.labyfy.component.transform.asm;

import net.labyfy.component.transform.asm.instruction.*;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;

public interface MethodVisitorContext {
  MethodVisitorContext onVisitInvokeDynamicInsn(VisitInvokeDynamicInsn visitInvokeDynamicInsn);

  MethodVisitorContext onVisitTypeInsn(VisitTypeInsn visitTypeInsn);

  MethodVisitorContext onVisitJumpInsn(VisitJumpInsn visitJumpInsn);

  MethodVisitorContext onVisitTableSwitchInsn(VisitTableSwitchInsn visitTableSwitchInsn);

  MethodVisitorContext onVisitLookupSwitchInsn(VisitLookupSwitchInsn visitLookupSwitchInsn);

  MethodVisitorContext onVisitMultiANewArrayInsn(VisitMultiANewArrayInsn visitMultiANewArrayInsn);

  MethodVisitorContext onVisitIincInsn(VisitIincInsn visitIincInsn);

  MethodVisitorContext onVisitIntInsn(VisitIntInsn visitIntInsn);

  MethodVisitorContext onVisitFieldInsn(VisitFieldInsn visitFieldInsn);

  MethodVisitorContext onVisitMethodInsn(VisitMethodInsn visitMethodInsn);

  MethodVisitorContext onVisitInsn(VisitInsn visitInsn);

  MethodVisitorContext onVisitCode(VisitCode visitCode);

  MethodVisitorContext onVisitLdcInsn(VisitLdcInsn visitLdcInsn);

  MethodVisitorContext onVisitLocalVariable(VisitLocalVariable visitLocalVariable);

  MethodVisitorContext onVisitVarInsn(VisitVarInsn visitVarInsn);

  void svisitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments);

  void svisitTypeInsn(int opcode, String type);

  void svisitJumpInsn(int opcode, Label label);

  void svisitTableSwitchInsn(int min, int max, Label dflt, Label... labels);

  void svisitLookupSwitchInsn(Label dflt, int[] keys, Label... labels);

  void svisitMultiANewArrayInsn(String descriptor, int numDimensions);

  void svisitIincInsn(int var, int increment);

  void visitVarInsn(int opcode, int var);

  void svisitVarInsn(int opcode, int var);

  void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index);

  void svisitLocalVariable(String name, String desc, String signature, Label start, Label end, int index);

  void visitFieldInsn(int opcode, String owner, String name, String desc);

  void svisitFieldInsn(int opcode, String owner, String name, String desc);

  void visitLdcInsn(Object cst);

  void svisitLdcInsn(Object cst);

  void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf);

  void svisitMethodInsn(int opcode, String owner, String name, String dest, boolean itf);

  void visitInsn(int opcode);

  void svisitIntInsn(int opcode, int operand);

  void visitIntInsn(int opcode, int operand);

  void svisitInsn(int opcode);

  void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments);

  void visitTypeInsn(int opcode, String type);

  void visitJumpInsn(int opcode, Label label);

  void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels);

  void visitLookupSwitchInsn(Label dflt, int[] keys, Label... labels);

  void visitMultiANewArrayInsn(String descriptor, int numDimensions);

  void visitIincInsn(int var, int increment);

  void visitCode();

  void svisitCode();

  MethodVisit getMethodVisit();

  MethodVisitorContext storeAsset(String key, Object value);

  <T> T getAsset(String key);
}
