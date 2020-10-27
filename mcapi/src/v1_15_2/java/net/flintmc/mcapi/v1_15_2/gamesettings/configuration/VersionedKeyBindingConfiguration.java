package net.flintmc.mcapi.v1_15_2.gamesettings.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.gamesettings.KeyBindMappings;
import net.flintmc.mcapi.gamesettings.KeyBinding;
import net.flintmc.mcapi.gamesettings.configuration.KeyBindingConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** 1.15.2 implementation of {@link KeyBindingConfiguration}. */
@Singleton
@Implement(value = KeyBindingConfiguration.class, version = "1.15.2")
public class VersionedKeyBindingConfiguration implements KeyBindingConfiguration {

  private final KeyBinding.Factory keyBindingFactory;

  @Inject
  private VersionedKeyBindingConfiguration(KeyBinding.Factory keyBindingFactory) {
    this.keyBindingFactory = keyBindingFactory;
  }

  /** {@inheritDoc} */
  @Override
  public KeyBinding getKeyBinding(Keybind keybind) {
    switch (keybind) {
      case SNEAK:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSneak);
      case SPRINT:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSprint);
      case LEFT:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindLeft);
      case RIGHT:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindRight);
      case BACK:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindBack);
      case FORWARD:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindForward);
      case ATTACK:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindAttack);
      case PICK_ITEM:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindPickBlock);
      case USE:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindUseItem);
      case DROP_ITEM:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindDrop);
      case HOTBAR_1:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindsHotbar[0]);
      case HOTBAR_2:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindsHotbar[1]);
      case HOTBAR_3:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindsHotbar[2]);
      case HOTBAR_4:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindsHotbar[3]);
      case HOTBAR_5:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindsHotbar[4]);
      case HOTBAR_6:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindsHotbar[5]);
      case HOTBAR_7:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindsHotbar[6]);
      case HOTBAR_8:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindsHotbar[7]);
      case HOTBAR_9:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindsHotbar[8]);
      case OPEN_INVENTORY:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindInventory);
      case SWAP_HANDS:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSwapHands);
      case LOAD_TOOLBAR:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindLoadToolbar);
      case SAVE_TOOLBAR:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSaveToolbar);
      case SHOW_PLAYER_LIST:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindPlayerList);
      case OPEN_CHAT:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindChat);
      case OPEN_COMMAND:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindCommand);
      case ADVANCEMENTS:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindAdvancements);
      case SPECTATOR_OUTLINES:
        return this.fromMinecraftObject(
            Minecraft.getInstance().gameSettings.keyBindSpectatorOutlines);
      case TAKE_SCREENSHOT:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindScreenshot);
      case SMOOTH_CAMERA:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSmoothCamera);
      case TOGGLE_FULLSCREEN:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindFullscreen);
      case TOGGLE_PERSPECTIVE:
        return this.fromMinecraftObject(
            Minecraft.getInstance().gameSettings.keyBindTogglePerspective);
      default:
        return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindJump);
    }
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

  /** {@inheritDoc} */
  @Override
  public void setKeyBindingCode(KeyBinding bindingCode, KeyBindMappings keyInputName) {
    Minecraft.getInstance()
        .gameSettings
        .setKeyBindingCode(
            this.toMinecraftObject(bindingCode),
            InputMappings.getInputByCode(keyInputName.getKey(), keyInputName.getScanCode()));
  }

  private net.minecraft.client.settings.KeyBinding toMinecraftObject(KeyBinding binding) {
    return new net.minecraft.client.settings.KeyBinding(
        binding.getKeyDescription(), binding.getKeyCode(), binding.getKeyCategory());
  }

  private KeyBinding fromMinecraftObject(net.minecraft.client.settings.KeyBinding keyBinding) {
    return this.keyBindingFactory.create(
        keyBinding.getKeyDescription(),
        keyBinding.getDefault().getKeyCode(),
        keyBinding.getKeyCategory());
  }
}
