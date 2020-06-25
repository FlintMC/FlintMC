package net.labyfy.component.transform.asm.instruction;

import com.google.common.base.Preconditions;
import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitVarInsn {

  void visitVarInsn(Context context);

  class Context implements AbstractContext {

    private MethodVisitorContext methodVisitorContext;
    private int opcode;
    private int var;

    private Context(MethodVisitorContext methodVisitorContext, int opcode, int var) {
      this.methodVisitorContext = methodVisitorContext;
      this.opcode = opcode;
      this.var = var;
    }

    public MethodVisitorContext getMethodVisitorContext() {
      return methodVisitorContext;
    }

    public void setMethodVisitorContext(MethodVisitorContext methodVisitorContext) {
      this.methodVisitorContext = methodVisitorContext;
    }

    public int getOpcode() {
      return opcode;
    }

    public Context setOpcode(int opcode) {
      this.opcode = opcode;
      return this;
    }

    public int getVar() {
      return var;
    }

    public Context setVar(int var) {
      this.var = var;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitVarInsn(this.opcode, this.var);
      return this;
    }

    public static Context of(MethodVisitorContext methodVisitorContext, int opcode, int var) {
        Preconditions.checkNotNull(methodVisitorContext);
        return new Context(methodVisitorContext, opcode, var);
    }
  }
}
