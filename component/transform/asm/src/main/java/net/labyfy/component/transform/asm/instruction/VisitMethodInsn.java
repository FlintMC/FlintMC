package net.labyfy.component.transform.asm.instruction;

import com.google.common.base.Preconditions;
import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitMethodInsn {

  void visitMethodInsn(Context context);

  class Context implements AbstractContext {
    private MethodVisitorContext methodVisitorContext;
    private int opcode;

    private Context(
        MethodVisitorContext methodVisitorContext,
        int opcode,
        String owner,
        String name,
        String desc,
        boolean itf) {
      this.methodVisitorContext = methodVisitorContext;
      this.opcode = opcode;
      this.owner = owner;
      this.name = name;
      this.desc = desc;
      this.itf = itf;
    }

    private String owner;
    private String name;
    private String desc;
    private boolean itf;

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

    public MethodVisitorContext getMethodVisitorContext() {
      return methodVisitorContext;
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

    public Context setMethodVisitorContext(MethodVisitorContext methodVisitorContext) {
      this.methodVisitorContext = methodVisitorContext;
      return this;
    }

    public boolean isItf() {
      return itf;
    }

    public Context setItf(boolean itf) {
      this.itf = itf;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitMethodInsn(
          this.opcode, this.owner, this.name, this.desc, this.itf);
      return this;
    }

    public static Context create(
        MethodVisitorContext methodVisitorContext,
        int opcode,
        String owner,
        String name,
        String desc,
        boolean itf) {
      Preconditions.checkNotNull(methodVisitorContext);
      Preconditions.checkNotNull(owner);
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(desc);
      return new Context(methodVisitorContext, opcode, owner, name, desc, itf);
    }
  }
}
