package net.labyfy.component.transform.asm.instruction;

import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;
import org.objectweb.asm.Label;

@FunctionalInterface
public interface VisitTableSwitchInsn {
  void visitTableSwitchInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private int min;
    private int max;
    private Label dflt;
    private Label[] labels;

    private Context(
            MethodVisitorContext methodVisitorContext,
            int min,
            int max,
            Label dflt,
            Label... labels) {
      this.methodVisitorContext = methodVisitorContext;
      this.min = min;
      this.max = max;
      this.dflt = dflt;
      this.labels = labels;
    }

    public static Context of(
            MethodVisitorContext methodVisitorContext,
            int min,
            int max,
            Label dflt,
            Label... labels) {
      return new Context(methodVisitorContext, min, max, dflt, labels);
    }

    public int getMin() {
      return this.min;
    }

    public Context setMin(int min) {
      this.min = min;
      return this;
    }

    public int getMax() {
      return this.max;
    }

    public Context setMax(int max) {
      this.max = max;
      return this;
    }

    public Label getDflt() {
      return this.dflt;
    }

    public Context setDflt(Label dflt) {
      this.dflt = dflt;
      return this;
    }

    public Label[] getLabels() {
      return this.labels;
    }

    public Context setLabels(Label[] labels) {
      this.labels = labels;
      return this;
    }

    public Context write() {
      this.methodVisitorContext.svisitTableSwitchInsn(this.min, this.max, this.dflt, this.labels);
      return this;
    }
  }
}
