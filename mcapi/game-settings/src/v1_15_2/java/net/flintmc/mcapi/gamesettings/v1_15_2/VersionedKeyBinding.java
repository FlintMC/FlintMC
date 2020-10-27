package net.flintmc.mcapi.gamesettings.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.mcapi.gamesettings.KeyBindMappings;
import net.flintmc.mcapi.gamesettings.KeyBinding;
import net.flintmc.framework.inject.implement.Implement;
import net.minecraft.client.util.InputMappings;

/**
 * 1.15.2 implementation of {@link KeyBinding}.
 */
@Implement(value = KeyBinding.class, version = "1.15.2")
public class VersionedKeyBinding extends net.minecraft.client.settings.KeyBinding implements KeyBinding {

  private final int keyCode;

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
  public void bind(KeyBindMappings keyBind) {
    this.bind(InputMappings.getInputByName(keyBind.getConfigurationName()));
  }

}
