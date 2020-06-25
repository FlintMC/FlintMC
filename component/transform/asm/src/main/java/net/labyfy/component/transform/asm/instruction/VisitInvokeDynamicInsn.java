package net.labyfy.component.transform.asm.instruction;

import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitInvokeDynamicInsn {
  void visitInvokeDynamicInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private java.lang.String arg0;
    private java.lang.String arg1;
    private org.objectweb.asm.Handle arg2;
    private java.lang.Object[] arg3;

    private Context(
            MethodVisitorContext methodVisitorContext,
            java.lang.String arg0,
            java.lang.String arg1,
            org.objectweb.asm.Handle arg2,
            java.lang.Object[] arg3) {
      this.methodVisitorContext = methodVisitorContext;
      this.arg0 = arg0;
      this.arg1 = arg1;
      this.arg2 = arg2;
      this.arg3 = arg3;
    }

    public static Context of(
            MethodVisitorContext methodVisitorContext,
            java.lang.String arg0,
            java.lang.String arg1,
            org.objectweb.asm.Handle arg2,
            java.lang.Object[] arg3) {
      return new Context(methodVisitorContext, arg0, arg1, arg2, arg3);
    }

    public java.lang.String getArg0() {
      return this.arg0;
    }

    public Context setArg0(java.lang.String arg0) {
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

    public org.objectweb.asm.Handle getArg2() {
      return this.arg2;
    }

    public Context setArg2(org.objectweb.asm.Handle arg2) {
      this.arg2 = arg2;
      return this;
    }

    public java.lang.Object[] getArg3() {
      return this.arg3;
    }

    public Context setArg3(java.lang.Object[] arg3) {
      this.arg3 = arg3;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitInvokeDynamicInsn(this.arg0, this.arg1, this.arg2, this.arg3);
      return this;
    }
  }
}
