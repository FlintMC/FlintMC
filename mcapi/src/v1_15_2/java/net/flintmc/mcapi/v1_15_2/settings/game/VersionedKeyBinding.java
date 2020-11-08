package net.flintmc.mcapi.v1_15_2.settings.game;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.settings.game.KeyBinding;
import net.flintmc.mcapi.settings.game.keybind.PhysicalKey;
import net.minecraft.client.util.InputMappings;

/**
 * 1.15.2 implementation of {@link KeyBinding}.
 */
@Implement(value = KeyBinding.class, version = "1.15.2")
public class VersionedKeyBinding extends net.minecraft.client.settings.KeyBinding implements KeyBinding {

  private int keyCode;

  @AssistedInject
  private VersionedKeyBinding(
      @Assisted("description") String description,
      @Assisted("keyCode") int keyCode,
      @Assisted("category") String category
  ) {
    super(description, keyCode, category);
    this.keyCode = keyCode;
  }

  @Override
  public int getKeyCode() {
    return this.keyCode;
  }

  @Override
  public void bind(PhysicalKey key) {
    super.bind(InputMappings.getInputByName(key.getConfigurationName()));
    this.keyCode = key.getKey();
  }

}
