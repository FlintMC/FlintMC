package net.labyfy.component.transform.asm.instruction;

import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;

@FunctionalInterface
public interface VisitTypeInsn {
  void visitTypeInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int opcode;
    private String type;

    private Context(MethodVisitorContext methodVisitorContext, int opcode, String type) {
      this.methodVisitorContext = methodVisitorContext;
      this.opcode = opcode;
      this.type = type;
    }

    public static Context of(
            MethodVisitorContext methodVisitorContext, int opcode, String type) {
      return new Context(methodVisitorContext, opcode, type);
    }

    public int getOpcode() {
      return this.opcode;
    }

    public Context setOpcode(int opcode) {
      this.opcode = opcode;
      return this;
    }

    public String getType() {
      return this.type;
    }

    public Context setType(String type) {
      this.type = type;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitTypeInsn(this.opcode, this.type);
      return this;
    }
  }
}
