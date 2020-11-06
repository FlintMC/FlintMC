package net.labyfy.internal.component.gamesettings.v1_15_2.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.Keybind;
import net.labyfy.component.config.annotation.implemented.ConfigImplementation;
import net.labyfy.component.gamesettings.KeyBinding;
import net.labyfy.component.gamesettings.configuration.KeyBindingConfiguration;
import net.labyfy.component.gamesettings.keybind.PhysicalKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 1.15.2 implementation of {@link KeyBindingConfiguration}.
 */
@Singleton
@ConfigImplementation(value = KeyBindingConfiguration.class, version = "1.15.2")
public class VersionedKeyBindingConfiguration implements KeyBindingConfiguration {

  // TODO this needs some changes (e.g. minecraft.KeyBinding#getDefault is being used to get the current key, but this is only the default)

  private final KeyBinding.Factory keyBindingFactory;

  @Inject
  private VersionedKeyBindingConfiguration(KeyBinding.Factory keyBindingFactory) {
    this.keyBindingFactory = keyBindingFactory;
  }

  @Override
  public String getKey(String keyDescription) {
    net.minecraft.client.settings.KeyBinding keyBinding = this.getMinecraftBinding(keyDescription);
    return keyBinding != null ? keyBinding.getDefault().getTranslationKey() : null;
  }

  @Override
  public void setKey(String keyDescription, String keyName) {
    net.minecraft.client.settings.KeyBinding keyBinding = this.getMinecraftBinding(keyDescription);
    if (keyBinding != null) {
      keyBinding.bind(InputMappings.getInputByName(keyName));
    }
  }

  @Override
  public Map<String, String> getAllKey() {
    Map<String, String> keys = new HashMap<>();
    for (net.minecraft.client.settings.KeyBinding keyBinding : Minecraft.getInstance().gameSettings.keyBindings) {
      keys.put(keyBinding.getKeyDescription(), keyBinding.getDefault().getTranslationKey());
    }
    return keys;
  }

  @Override
  public void setAllKey(Map<String, String> keys) {
    keys.forEach(this::setKey);
  }

  @Override
  public boolean hasDuplicates(PhysicalKey key) {
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
    for (net.minecraft.client.settings.KeyBinding keyBinding : Minecraft.getInstance().gameSettings.keyBindings) {
      if (keyBinding.getKeyDescription().equals(keyDescription)) {
        return keyBinding;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBinding(Keybind keybind) {
    net.minecraft.client.settings.KeyBinding keyBinding = this.getMinecraftBinding(keybind.getKey());
    return keyBinding != null ? this.fromMinecraftObject(keyBinding) : null;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<KeyBinding> getKeyBindsHotbar() {
    return Arrays
        .stream(Minecraft.getInstance().gameSettings.keyBindsHotbar)
        .map(this::fromMinecraftObject).collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<KeyBinding> getKeyBindings() {
    return Arrays
        .stream(Minecraft.getInstance().gameSettings.keyBindings)
        .map(this::fromMinecraftObject).collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setKeyBindingCode(KeyBinding bindingCode, PhysicalKey keyInputName) {
    Minecraft.getInstance().gameSettings.setKeyBindingCode(
        this.toMinecraftObject(bindingCode),
        InputMappings.getInputByCode(keyInputName.getKey(), keyInputName.getScanCode())
    );
    bindingCode.bind(keyInputName);
  }

  private net.minecraft.client.settings.KeyBinding toMinecraftObject(KeyBinding binding) {
    return new net.minecraft.client.settings.KeyBinding(binding.getKeyDescription(), binding.getKeyCode(), binding.getKeyCategory());
  }

  private KeyBinding fromMinecraftObject(net.minecraft.client.settings.KeyBinding keyBinding) {
    return this.keyBindingFactory.create(
        keyBinding.getKeyDescription(),
        keyBinding.getDefault().getKeyCode(),
        keyBinding.getKeyCategory()
    );
  }

}
