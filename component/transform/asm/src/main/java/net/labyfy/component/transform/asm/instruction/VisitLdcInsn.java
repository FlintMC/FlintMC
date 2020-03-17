package net.labyfy.component.transform.asm.instruction;

import com.google.common.base.Preconditions;
import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitLdcInsn {
  void visitLdcInsn(Context context);

  class Context implements AbstractContext{
    private MethodVisitorContext methodVisitorContext;
    private Object cst;

    private Context(MethodVisitorContext methodVisitorContext, Object cst) {
      this.methodVisitorContext = methodVisitorContext;
      this.cst = cst;
    }

    public MethodVisitorContext getMethodVisitorContext() {
      return methodVisitorContext;
    }

    public Context setMethodVisitorContext(
            MethodVisitorContext methodVisitorContext) {
      this.methodVisitorContext = methodVisitorContext;
      return this;
    }

    public Object getCst() {
      return cst;
    }

    public Context setCst(Object cst) {
      this.cst = cst;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitLdcInsn(this.cst);
      return this;
    }

    public static Context create(
            MethodVisitorContext methodVisitorContext, Object cst) {
      Preconditions.checkNotNull(methodVisitorContext);
      return new Context(methodVisitorContext, cst);
    }
  }

}
