package net.labyfy.component.transform.asm.instruction;

import net.labyfy.component.transform.asm.AbstractContext;
import net.labyfy.component.transform.asm.MethodVisitorContext;
import org.objectweb.asm.Label;

@FunctionalInterface
public interface VisitLookupSwitchInsn {
  void visitLookupSwitchInsn(Context context);

  class Context implements AbstractContext {
    private final MethodVisitorContext methodVisitorContext;
    private Label dflt;
    private int[] keys;
    private Label[] labels;

    private Context(
        MethodVisitorContext methodVisitorContext,
        Label dflt,
        int[] keys,
        Label... labels) {
      this.methodVisitorContext = methodVisitorContext;
      this.dflt = dflt;
      this.keys = keys;
      this.labels = labels;
    }

    public static Context of(
        MethodVisitorContext methodVisitorContext,
        Label dflt,
        int[] keys,
        Label... labels) {
      return new Context(methodVisitorContext, dflt, keys, labels);
    }

    public Label getDflt() {
      return this.dflt;
    }

    public Context setDflt(Label dflt) {
      this.dflt = dflt;
      return this;
    }

    public int[] getKeys() {
      return this.keys;
    }

    public Context setKeys(int[] keys) {
      this.keys = keys;
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
      this.methodVisitorContext.svisitLookupSwitchInsn(this.dflt, this.keys, this.labels);
      return this;
    }
  }
}
