package net.labyfy.component.transform.asm.instruction;

import com.google.common.base.Preconditions;
import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitIntInsn {

  void visitIntInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int opcode;
    private int operand;

    public Context(MethodVisitorContext methodVisitorContext, int opcode, int operand) {
      this.methodVisitorContext = methodVisitorContext;
      this.opcode = opcode;
      this.operand = operand;
    }

    public static Context of(
        MethodVisitorContext methodVisitorContext, int opcode, int operand) {
      Preconditions.checkNotNull(methodVisitorContext);
      return new Context(methodVisitorContext, opcode, operand);
    }

    public Context write() {
      this.methodVisitorContext.svisitIntInsn(this.opcode, this.operand);
      return this;
    }

    public MethodVisitorContext getMethodVisitorContext() {
      return methodVisitorContext;
    }

    public int getOpcode() {
      return opcode;
    }

    public Context setOpcode(int opcode) {
      this.opcode = opcode;
      return this;
    }

    public int getOperand() {
      return operand;
    }

    public Context setOperand(int operand) {
      this.operand = operand;
      return this;
    }
  }
}
