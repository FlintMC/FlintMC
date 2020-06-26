package net.labyfy.component.transform.asm.instruction;

import com.google.common.base.Preconditions;
import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;
import org.objectweb.asm.Label;

@FunctionalInterface
public interface VisitLocalVariable {

  void visitLocalVariable(Context context);

  class Context implements AbstractContext {

    private final MethodVisitorContext methodVisitorContext;
    private String name;
    private String desc;
    private String signature;
    private Label start;
    private Label end;
    private int index;

    private Context(
        MethodVisitorContext methodVisitorContext,
        String name,
        String desc,
        String signature,
        Label start,
        Label end,
        int index) {
      this.methodVisitorContext = methodVisitorContext;
      this.name = name;
      this.desc = desc;
      this.signature = signature;
      this.start = start;
      this.end = end;
      this.index = index;
    }

    public static Context of(
        MethodVisitorContext methodVisitorContext,
        String name,
        String desc,
        String signature,
        Label start,
        Label end,
        int index) {
      Preconditions.checkNotNull(methodVisitorContext);
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(desc);
      Preconditions.checkNotNull(signature);
      Preconditions.checkNotNull(start);
      Preconditions.checkNotNull(end);
      return new Context(methodVisitorContext, name, desc, signature, start, end, index);
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

    public String getSignature() {
      return signature;
    }

    public Context setSignature(String signature) {
      this.signature = signature;
      return this;
    }

    public Label getStart() {
      return start;
    }

    public Context setStart(Label start) {
      this.start = start;
      return this;
    }

    public Label getEnd() {
      return end;
    }

    public Context setEnd(Label end) {
      this.end = end;
      return this;
    }

    public int getIndex() {
      return index;
    }

    public Context setIndex(int index) {
      this.index = index;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitLocalVariable(
          this.name, this.desc, this.signature, this.start, this.end, this.index);
      return this;
    }
  }
}
