package net.labyfy.component.gui.mcjfxgl.component.theme.style;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.Script;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import net.labyfy.component.gui.mcjfxgl.component.control.McJfxGLControlBase;

import java.util.function.Function;

public class ThemeComponentStyle {

  private final Class<? extends McJfxGLControlBase> target;
  private final Function<Control, Skin<Control>> applyFunction;

  private ThemeComponentStyle(
          Class<? extends McJfxGLControlBase> target,
          Function<Control, Skin<Control>> applyFunction) {
    this.target = target;
    this.applyFunction = applyFunction;
  }

  public Class<? extends McJfxGLControlBase> getTarget() {
    return target;
  }

  public Function<Control, Skin<Control>> getApplyFunction() {
    return applyFunction;
  }

  public String toString() {
    return "ThemeComponentStyle{" + "target=" + target + ", applyFunction=" + applyFunction + '}';
  }

  public abstract static class Handle extends Script {

    private Function<Control, Skin<Control>> applyFunction = control -> null;

    protected Handle() {}

    protected Handle(Binding binding) {
      super(binding);
    }

    public void apply(Closure<Skin<Control>> closure) {
      this.applyFunction = closure::call;
    }

    public ThemeComponentStyle toModel() {
      return new ThemeComponentStyle(
              (Class<? extends McJfxGLControlBase>) this.getBinding().getVariable("target"),
              applyFunction);
    }
  }
}
