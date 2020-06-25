package net.labyfy.component.transform.asm.instruction;

import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitTypeInsn {
  void visitTypeInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int arg0;
    private java.lang.String arg1;

    private Context(MethodVisitorContext methodVisitorContext, int arg0, java.lang.String arg1) {
      this.methodVisitorContext = methodVisitorContext;
      this.arg0 = arg0;
      this.arg1 = arg1;
    }

    public static Context of(
            MethodVisitorContext methodVisitorContext, int arg0, java.lang.String arg1) {
      return new Context(methodVisitorContext, arg0, arg1);
    }

    public int getArg0() {
      return this.arg0;
    }

    public Context setArg0(int arg0) {
      this.arg0 = arg0;
      return this;
    }

    public java.lang.String getArg1() {
      return this.arg1;
    }

    public Context setArg1(java.lang.String arg1) {
      this.arg1 = arg1;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitTypeInsn(this.arg0, this.arg1);
      return this;
    }
  }
}
