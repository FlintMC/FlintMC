package net.labyfy.component.transform.asm.instruction;

import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;
import org.objectweb.asm.Label;

@FunctionalInterface
public interface VisitJumpInsn {
  void visitJumpInsn(Context context);

    class Context implements AbstractContext {
        private final MethodVisitorContext methodVisitorContext;
        private int opcode;
        private Label label;

        private Context(
                MethodVisitorContext methodVisitorContext, int opcode, Label label) {
            this.methodVisitorContext = methodVisitorContext;
            this.opcode = opcode;
            this.label = label;
        }

        public static Context of(
                MethodVisitorContext methodVisitorContext, int opcode, Label label) {
            return new Context(methodVisitorContext, opcode, label);
        }

        public int getOpcode() {
            return this.opcode;
        }

        public Context setOpcode(int opcode) {
            this.opcode = opcode;
            return this;
        }

        public Label getLabel() {
            return this.label;
        }

        public Context setLabel(Label label) {
            this.label = label;
            return this;
        }

        public Context write() {
            this.methodVisitorContext.svisitJumpInsn(this.opcode, this.label);
            return this;
        }
    }
}
