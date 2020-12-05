package net.flintmc.mcapi.v1_15_2.settings.game.configuration;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.util.InputMappings;

@Shadow("net.minecraft.client.settings.KeyBinding")
public interface ShadowKeyBinding {

  @FieldGetter("keyCode")
  InputMappings.Input getKeyCode();
}
