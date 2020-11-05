package net.labyfy.internal.component.gamesettings.modifier;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.Description;
import net.labyfy.component.settings.annotation.ui.DisplayName;
import net.labyfy.component.settings.options.dropdown.Selection;

import java.lang.annotation.Annotation;

public class DefaultSelection implements Selection {

  private final String value;

  public DefaultSelection(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return value;
  }

  @Override
  public DisplayName display() {
    return new DisplayName() {
      @Override
      public Component[] value() {
        return new Component[0];
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return DisplayName.class;
      }
    };
  }

  @Override
  public Description description() {
    return new Description() {
      @Override
      public Component[] value() {
        return new Component[0];
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return Description.class;
      }
    };
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return Selection.class;
  }
}
