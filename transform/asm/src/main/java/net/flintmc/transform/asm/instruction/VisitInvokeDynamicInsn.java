package net.flintmc.transform.asm.instruction;

import net.flintmc.transform.asm.AbstractContext;
import net.flintmc.transform.asm.MethodVisitorContext;
import org.objectweb.asm.Handle;

@FunctionalInterface
public interface VisitInvokeDynamicInsn {
  void visitInvokeDynamicInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private String name;
    private String descriptor;
    private Handle bootstrapMethodHandle;
    private Object[] bootstrapMethodArguments;

    private Context(
        MethodVisitorContext methodVisitorContext,
        String name,
        String descriptor,
        Handle bootstrapMethodHandle,
        Object... bootstrapMethodArguments) {
      this.methodVisitorContext = methodVisitorContext;
      this.name = name;
      this.descriptor = descriptor;
      this.bootstrapMethodHandle = bootstrapMethodHandle;
      this.bootstrapMethodArguments = bootstrapMethodArguments;
    }

    public static Context of(
        MethodVisitorContext methodVisitorContext,
        String name,
        String descriptor,
        Handle bootstrapMethodHandle,
        Object... bootstrapMethodArguments) {
      return new Context(methodVisitorContext, name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    public String getName() {
      return this.name;
    }

    public Context setName(String name) {
      this.name = name;
      return this;
    }

    public String getDescriptor() {
      return this.descriptor;
    }

    public Context setDescriptor(String descriptor) {
      this.descriptor = descriptor;
      return this;
    }

    public Handle getBootstrapMethodHandle() {
      return this.bootstrapMethodHandle;
    }

    public Context setBootstrapMethodHandle(Handle bootstrapMethodHandle) {
      this.bootstrapMethodHandle = bootstrapMethodHandle;
      return this;
    }

    public Object[] getBootstrapMethodArguments() {
      return this.bootstrapMethodArguments;
    }

    public Context setBootstrapMethodArguments(Object[] bootstrapMethodArguments) {
      this.bootstrapMethodArguments = bootstrapMethodArguments;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitInvokeDynamicInsn(this.name, this.descriptor, this.bootstrapMethodHandle, this.bootstrapMethodArguments);
      return this;
    }
  }
}
