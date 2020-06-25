package net.labyfy.component.transform.asm.instruction;

import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitTableSwitchInsn {
  void visitTableSwitchInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int arg0;
    private int arg1;
    private org.objectweb.asm.Label arg2;
    private org.objectweb.asm.Label[] arg3;

    private Context(
            MethodVisitorContext methodVisitorContext,
            int arg0,
            int arg1,
            org.objectweb.asm.Label arg2,
            org.objectweb.asm.Label[] arg3) {
      this.methodVisitorContext = methodVisitorContext;
      this.arg0 = arg0;
      this.arg1 = arg1;
      this.arg2 = arg2;
      this.arg3 = arg3;
    }

    public static Context of(
            MethodVisitorContext methodVisitorContext,
            int arg0,
            int arg1,
            org.objectweb.asm.Label arg2,
            org.objectweb.asm.Label[] arg3) {
      return new Context(methodVisitorContext, arg0, arg1, arg2, arg3);
    }

    public int getArg0() {
      return this.arg0;
    }

    public Context setArg0(int arg0) {
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

    public org.objectweb.asm.Label getArg2() {
      return this.arg2;
    }

    public Context setArg2(org.objectweb.asm.Label arg2) {
      this.arg2 = arg2;
      return this;
    }

    public org.objectweb.asm.Label[] getArg3() {
      return this.arg3;
    }

    public Context setArg3(org.objectweb.asm.Label[] arg3) {
      this.arg3 = arg3;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitTableSwitchInsn(this.arg0, this.arg1, this.arg2, this.arg3);
      return this;
    }
  }
}
