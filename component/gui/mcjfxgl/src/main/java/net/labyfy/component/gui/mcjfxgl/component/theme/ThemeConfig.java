package net.labyfy.component.gui.mcjfxgl.component.theme;

import groovy.lang.Binding;
import groovy.lang.Closure;

public class ThemeConfig {

  private Info info;

  private ThemeConfig(Info info) {
    this.info = info;
  }

  public Info getInfo() {
    return info;
  }

  public String toString() {
    return "ThemeConfig{" + "info=" + info + '}';
  }

  public static class Info {

    private final String name;
    private final String description;

    private Info(String name, String description) {
      this.name = name;
      this.description = description;
    }

    public String getName() {
      return name;
    }

    public String getDescription() {
      return description;
    }

    public String toString() {
      return "Info{" + "name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }

    public static class Handle {

      private String name;
      private String description;

      public Info toModel() {
        return new Info(name, description);
      }
    }
  }

  public abstract static class Handle extends groovy.lang.Script {

    private final Info.Handle info = new Info.Handle();

    protected Handle() {}

    protected Handle(Binding binding) {
      super(binding);
    }

    public void info(Closure<?> closure) {
      closure.rehydrate(info, info, info).call();
    }

    public ThemeConfig toModel() {
      return new ThemeConfig(info.toModel());
    }
  }
}
