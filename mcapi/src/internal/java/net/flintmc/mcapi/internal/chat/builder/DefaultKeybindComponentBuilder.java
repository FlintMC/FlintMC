package net.flintmc.mcapi.internal.chat.builder;

import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.chat.builder.KeybindComponentBuilder;
import net.flintmc.mcapi.chat.component.KeybindComponent;
import net.flintmc.mcapi.internal.chat.component.DefaultKeybindComponent;

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
