package net.labyfy.chat.component;

import net.labyfy.chat.Keybind;

import java.util.function.Function;

public class DefaultKeybindComponent extends DefaultChatComponent implements KeybindComponent {

  public static Function<Keybind, String> nameFunction = Keybind::getKey;

  private Keybind keybind;

  @Override
  public Keybind keybind() {
    return this.keybind;
  }

  @Override
  public void keybind(Keybind keybind) {
    this.keybind = keybind;
  }

  @Override
  public String getUnformattedText() {
    if (nameFunction == null) {
      nameFunction = Keybind::getKey;
    }

    return this.keybind == null ? "null" : nameFunction.apply(this.keybind);
  }

  @Override
  protected DefaultChatComponent createCopy() {
    DefaultKeybindComponent component = new DefaultKeybindComponent();
    component.keybind = this.keybind;
    return component;
  }

  @Override
  protected boolean isSpecificEmpty() {
    return this.keybind == null;
  }
}
