package net.flintmc.transform.asm.instruction;

import net.flintmc.transform.asm.AbstractContext;
import net.flintmc.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitMultiANewArrayInsn {
  void visitMultiANewArrayInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private String descriptor;
    private int numDimensions;

    private Context(MethodVisitorContext methodVisitorContext, String descriptor, int numDimensions) {
      this.methodVisitorContext = methodVisitorContext;
      this.descriptor = descriptor;
      this.numDimensions = numDimensions;
    }

    public static Context of(
        MethodVisitorContext methodVisitorContext, String descriptor, int numDimensions) {
      return new Context(methodVisitorContext, descriptor, numDimensions);
    }

    public String getDescriptor() {
      return this.descriptor;
    }

    public Context setDescriptor(String descriptor) {
      this.descriptor = descriptor;
      return this;
    }

    public int getNumDimensions() {
      return this.numDimensions;
    }

    public Context setNumDimensions(int numDimensions) {
      this.numDimensions = numDimensions;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitMultiANewArrayInsn(this.descriptor, this.numDimensions);
      return this;
    }
  }
}
