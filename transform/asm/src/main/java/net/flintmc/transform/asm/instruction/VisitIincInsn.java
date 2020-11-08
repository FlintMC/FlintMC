package net.flintmc.transform.asm.instruction;

import net.flintmc.transform.asm.AbstractContext;
import net.flintmc.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitIincInsn {
  void visitIincInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int var;
    private int increment;

    private Context(MethodVisitorContext methodVisitorContext, int var, int increment) {
      this.methodVisitorContext = methodVisitorContext;
      this.var = var;
      this.increment = increment;
    }

    public static Context of(MethodVisitorContext methodVisitorContext, int var, int increment) {
      return new Context(methodVisitorContext, var, increment);
    }

    public int getVar() {
      return this.var;
    }

    public Context setVar(int var) {
      this.var = var;
      return this;
    }

    public int getIncrement() {
      return this.increment;
    }

    public Context setIncrement(int increment) {
      this.increment = increment;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitIincInsn(this.var, this.increment);
      return this;
    }
  }
}
