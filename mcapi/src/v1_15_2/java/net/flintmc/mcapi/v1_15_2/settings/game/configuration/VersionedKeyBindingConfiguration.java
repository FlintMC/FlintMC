package net.flintmc.mcapi.v1_15_2.settings.game.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.settings.game.KeyBinding;
import net.flintmc.mcapi.settings.game.configuration.KeyBindingConfiguration;
import net.flintmc.render.gui.input.Key;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 1.15.2 implementation of {@link KeyBindingConfiguration}. */
@Singleton
@ConfigImplementation(value = KeyBindingConfiguration.class, version = "1.15.2")
public class VersionedKeyBindingConfiguration implements KeyBindingConfiguration {

  private final KeyBinding.Factory keyBindingFactory;

  @Inject
  private VersionedKeyBindingConfiguration(KeyBinding.Factory keyBindingFactory) {
    this.keyBindingFactory = keyBindingFactory;
  }

  @Override
  public Key getKey(String keyDescription) {
    net.minecraft.client.settings.KeyBinding keyBinding = this.getMinecraftBinding(keyDescription);
    return keyBinding != null
        ? Key.getByConfigurationName(
            ((ShadowKeyBinding) keyBinding).getKeyCode().getTranslationKey())
        : null;
  }

  @Override
  public void setKey(String keyDescription, Key key) {
    net.minecraft.client.settings.KeyBinding keyBinding = this.getMinecraftBinding(keyDescription);
    if (keyBinding != null) {
      keyBinding.bind(
          key == null
              ? InputMappings.INPUT_INVALID
              : InputMappings.getInputByName(key.getConfigurationName()));
      Minecraft.getInstance().gameSettings.saveOptions();
    }
  }

  @Override
  public Map<String, Key> getAllKey() {
    Map<String, Key> keys = new HashMap<>();
    for (net.minecraft.client.settings.KeyBinding keyBinding :
        Minecraft.getInstance().gameSettings.keyBindings) {
      keys.put(
          keyBinding.getKeyDescription(),
          Key.getByConfigurationName(
              ((ShadowKeyBinding) keyBinding).getKeyCode().getTranslationKey()));
    }
    return keys;
  }

  @Override
  public void setAllKey(Map<String, Key> keys) {
    keys.forEach(this::setKey);
  }

  @Override
  public boolean hasDuplicates(Key key) {
    boolean found = false;
    for (KeyBinding keyBinding : this.getKeyBindings()) {
      if (keyBinding.getKeyCode() == key.getKey()) {
        if (found) {
          return true;
        }
        found = true;
      }
    }
    for (KeyBinding keyBinding : this.getKeyBindsHotbar()) {
      if (keyBinding.getKeyCode() == key.getKey()) {
        if (found) {
          return true;
        }

        found = true;
      }
    }
    return false;
  }

  private net.minecraft.client.settings.KeyBinding getMinecraftBinding(String keyDescription) {
    for (net.minecraft.client.settings.KeyBinding keyBinding :
        Minecraft.getInstance().gameSettings.keyBindings) {
      if (keyBinding.getKeyDescription().equals(keyDescription)) {
        return keyBinding;
      }
    }

    return null;
  }

  /** {@inheritDoc} */
  @Override
  public KeyBinding getKeyBinding(Keybind keybind) {
    net.minecraft.client.settings.KeyBinding keyBinding =
        this.getMinecraftBinding(keybind.getKey());
    return keyBinding != null ? this.fromMinecraftObject(keyBinding) : null;
  }

  /** {@inheritDoc} */
  @Override
  public List<KeyBinding> getKeyBindsHotbar() {
    return Arrays.stream(Minecraft.getInstance().gameSettings.keyBindsHotbar)
        .map(this::fromMinecraftObject)
        .collect(Collectors.toList());
  }

  /** {@inheritDoc} */
  @Override
  public List<KeyBinding> getKeyBindings() {
    return Arrays.stream(Minecraft.getInstance().gameSettings.keyBindings)
        .map(this::fromMinecraftObject)
        .collect(Collectors.toList());
  }

  private KeyBinding fromMinecraftObject(net.minecraft.client.settings.KeyBinding keyBinding) {
    return this.keyBindingFactory.create(
        keyBinding.getKeyDescription(),
        ((ShadowKeyBinding) keyBinding).getKeyCode().getKeyCode(),
        keyBinding.getKeyCategory());
  }
}
