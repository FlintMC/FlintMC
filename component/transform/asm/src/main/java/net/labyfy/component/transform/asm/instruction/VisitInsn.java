package net.labyfy.component.transform.asm.instruction;

import com.google.common.base.Preconditions;
import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitInsn {

  void visitInsn(Context context);

  class Context implements AbstractContext {
    private MethodVisitorContext methodVisitorContext;
    private int opcode;

    private Context(MethodVisitorContext methodVisitorContext, int opcode) {
      this.methodVisitorContext = methodVisitorContext;
      this.opcode = opcode;
    }

    public int getOpcode() {
      return opcode;
    }

    public Context setOpcode(int opcode) {
      this.opcode = opcode;
      return this;
    }

    public MethodVisitorContext getMethodVisitorContext() {
      return methodVisitorContext;
    }

    public Context setMethodVisitorContext(MethodVisitorContext methodVisitorContext) {
      this.methodVisitorContext = methodVisitorContext;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitInsn(this.opcode);
      return this;
    }

    public static Context of(MethodVisitorContext methodVisitorContext, int opcode) {
      Preconditions.checkNotNull(methodVisitorContext);
      return new Context(methodVisitorContext, opcode);
    }
  }
}
