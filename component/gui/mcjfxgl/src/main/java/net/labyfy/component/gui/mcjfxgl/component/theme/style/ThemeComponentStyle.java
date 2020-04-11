package net.labyfy.component.gui.mcjfxgl.component.theme.style;

import com.google.common.collect.Sets;
import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.Script;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLControl;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class ThemeComponentStyle {

  private final Class<? extends McJfxGLControl> target;
  private final Function<Control, Skin<Control>> applyFunction;
  private final Collection<String> styleSheets;
  private final Collection<String> styleClasses;

  private ThemeComponentStyle(
          Class<? extends McJfxGLControl> target,
          Function<Control, Skin<Control>> applyFunction,
          Collection<String> styleSheets,
          Collection<String> styleClasses) {
    this.target = target;
    this.applyFunction = applyFunction;
    this.styleSheets = styleSheets;
    this.styleClasses = styleClasses;
  }

  public Collection<String> getStyleSheets() {
    return styleSheets;
  }

  public Collection<String> getStyleClasses() {
    return styleClasses;
  }

  public Class<? extends McJfxGLControl> getTarget() {
    return target;
  }

  public Function<Control, Skin<Control>> getApplyFunction() {
    return applyFunction;
  }

  public String toString() {
    return "ThemeComponentStyle{" + "target=" + target + ", applyFunction=" + applyFunction + '}';
  }

  public abstract static class Handle extends Script {

    private final Collection<String> styleSheets = Sets.newHashSet();
    private final Collection<String> styleClasses = Sets.newHashSet();
    private Function<Control, Skin<Control>> applyFunction = control -> null;

    protected Handle() {
    }

    protected Handle(Binding binding) {
      super(binding);
    }

    public void stylesheet(String style) {
      this.styleSheets.add(style);
    }

    public void styleclass(String clazz) {
      this.styleClasses.add(clazz);
    }

    public void apply(Closure<Skin<Control>> closure) {
      this.applyFunction = closure::call;
    }

    public ThemeComponentStyle toModel() {
      return new ThemeComponentStyle(
              (Class<? extends McJfxGLControl>) this.getBinding().getVariable("target"),
              applyFunction,
              Collections.unmodifiableCollection(styleSheets),
              Collections.unmodifiableCollection(styleClasses));
    }
  }
}
