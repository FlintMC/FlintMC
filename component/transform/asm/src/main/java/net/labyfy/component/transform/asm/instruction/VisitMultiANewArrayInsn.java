package net.labyfy.component.transform.asm.instruction;

import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitMultiANewArrayInsn {
  void visitMultiANewArrayInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private java.lang.String arg0;
    private int arg1;

    private Context(MethodVisitorContext methodVisitorContext, java.lang.String arg0, int arg1) {
      this.methodVisitorContext = methodVisitorContext;
      this.arg0 = arg0;
      this.arg1 = arg1;
    }

    public static Context of(
            MethodVisitorContext methodVisitorContext, java.lang.String arg0, int arg1) {
      return new Context(methodVisitorContext, arg0, arg1);
    }

    public java.lang.String getArg0() {
      return this.arg0;
    }

    public Context setArg0(java.lang.String arg0) {
      this.arg0 = arg0;
      return this;
    }

    public int getArg1() {
      return this.arg1;
    }

    public Context setArg1(int arg1) {
      this.arg1 = arg1;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitMultiANewArrayInsn(this.arg0, this.arg1);
      return this;
    }
  }
}
