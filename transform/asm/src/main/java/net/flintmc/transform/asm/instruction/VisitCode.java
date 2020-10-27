package net.flintmc.transform.asm.instruction;

import com.google.common.base.Preconditions;
import net.flintmc.transform.asm.AbstractContext;
import net.flintmc.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitCode {

  void visitCode(Context context);

  class Context implements AbstractContext {

    private final MethodVisitorContext methodVisitorContext;

    private Context(MethodVisitorContext methodVisitorContext) {
      this.methodVisitorContext = methodVisitorContext;
    }

    public static Context of(MethodVisitorContext methodVisitorContext) {
      Preconditions.checkNotNull(methodVisitorContext);
      return new Context(methodVisitorContext);
    }

    public Context write() {
      this.methodVisitorContext.svisitCode();
      return this;
    }
  }
}
