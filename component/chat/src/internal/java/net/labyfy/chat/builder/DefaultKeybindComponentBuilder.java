package net.labyfy.chat.builder;

import net.labyfy.chat.Keybind;
import net.labyfy.chat.component.DefaultKeybindComponent;
import net.labyfy.chat.component.KeybindComponent;
import net.labyfy.component.inject.implement.Implement;

@Implement(value = KeybindComponentBuilder.class)
public class DefaultKeybindComponentBuilder
    extends DefaultComponentBuilder<KeybindComponentBuilder, KeybindComponent>
    implements KeybindComponentBuilder {

  public DefaultKeybindComponentBuilder() {
    super(DefaultKeybindComponent::new);
  }

  @Override
  public KeybindComponentBuilder keybind(Keybind keybind) {
    super.currentComponent.keybind(keybind);
    return this;
  }

  @Override
  public Keybind keybind() {
    return super.currentComponent.keybind();
  }
}
