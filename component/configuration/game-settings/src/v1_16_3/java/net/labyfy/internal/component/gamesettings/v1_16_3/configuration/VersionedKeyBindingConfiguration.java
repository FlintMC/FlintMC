package net.labyfy.internal.component.gamesettings.v1_16_3.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gamesettings.KeyBindMappings;
import net.labyfy.component.gamesettings.KeyBinding;
import net.labyfy.component.gamesettings.configuration.KeyBindingConfiguration;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 1.16.3 implementation of {@link KeyBindingConfiguration}.
 */
@Singleton
@Implement(value = KeyBindingConfiguration.class, version = "1.16.3")
public class VersionedKeyBindingConfiguration implements KeyBindingConfiguration {

  private final KeyBinding.Factory keyBindingFactory;

  @Inject
  private VersionedKeyBindingConfiguration(KeyBinding.Factory keyBindingFactory) {
    this.keyBindingFactory = keyBindingFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindForward() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindForward);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindLeft() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindLeft);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindBack() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindBack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindRight() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindRight);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindJump() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindJump);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindSneak() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSneak);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindSprint() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSprint);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindInventory() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindInventory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindSwapHands() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSwapHands);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindDrop() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindDrop);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindUseItem() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindUseItem);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindAttack() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindAttack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindPickBlock() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindPickBlock);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindChat() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindChat);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindPlayerList() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindPlayerList);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindCommand() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindScreenshot() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindScreenshot);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindTogglePerspective() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindTogglePerspective);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindSmoothCamera() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSmoothCamera);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindFullscreen() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindFullscreen);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindSpectatorOutlines() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSpectatorOutlines);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindAdvancements() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindAdvancements);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<KeyBinding> getKeyBindsHotbar() {
    List<KeyBinding> keyBindings;

    net.minecraft.client.settings.KeyBinding[] keyBindsHotbar = Minecraft.getInstance().gameSettings.keyBindsHotbar;
    keyBindings = Arrays.stream(keyBindsHotbar).map(this::fromMinecraftObject).collect(Collectors.toList());

    return keyBindings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindSaveToolbar() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSaveToolbar);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBindLoadToolbar() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindLoadToolbar);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<KeyBinding> getKeyBindings() {
    List<KeyBinding> keyBindings;

    net.minecraft.client.settings.KeyBinding[] keyBinds = Minecraft.getInstance().gameSettings.keyBindings;
    keyBindings = Arrays.stream(keyBinds).map(this::fromMinecraftObject).collect(Collectors.toList());

    return keyBindings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setKeyBindingCode(KeyBinding bindingCode, KeyBindMappings keyInputName) {
    Minecraft.getInstance().gameSettings.setKeyBindingCode(
            this.toMinecraftObject(bindingCode),
            InputMappings.getInputByCode(keyInputName.getKey(), keyInputName.getScanCode())
    );
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
