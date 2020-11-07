package net.flintmc.transform.asm.instruction;

import com.google.common.base.Preconditions;
import net.flintmc.transform.asm.AbstractContext;
import net.flintmc.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitFieldInsn {

  void visitFieldInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int opcode;
    private String owner;
    private String name;
    private String desc;

    private Context(
        MethodVisitorContext methodVisitorContext,
        int opcode,
        String owner,
        String name,
        String desc) {
      this.methodVisitorContext = methodVisitorContext;
      this.opcode = opcode;
      this.owner = owner;
      this.name = name;
      this.desc = desc;
    }

    public static Context of(
        MethodVisitorContext methodVisitorContext,
        int opcode,
        String owner,
        String name,
        String desc) {
      Preconditions.checkNotNull(methodVisitorContext);
      Preconditions.checkNotNull(owner);
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(desc);
      return new Context(methodVisitorContext, opcode, owner, name, desc);
    }

    public Context write() {
      this.methodVisitorContext.svisitFieldInsn(this.opcode, this.owner, this.name, this.desc);
      return this;
    }

    public MethodVisitorContext getMethodVisitorContext() {
      return methodVisitorContext;
    }

    public int getOpcode() {
      return opcode;
    }

    public Context setOpcode(int opcode) {
      this.opcode = opcode;
      return this;
    }

    public String getOwner() {
      return owner;
    }

    public Context setOwner(String owner) {
      this.owner = owner;
      return this;
    }

    public String getName() {
      return name;
    }

    public Context setName(String name) {
      this.name = name;
      return this;
    }

    public String getDesc() {
      return desc;
    }

    public Context setDesc(String desc) {
      this.desc = desc;
      return this;
    }
  }
}
