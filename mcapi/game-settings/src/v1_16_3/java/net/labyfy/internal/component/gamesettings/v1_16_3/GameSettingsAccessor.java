package net.labyfy.internal.component.gamesettings.v1_16_3;

import net.labyfy.component.transform.shadow.FieldGetter;
import net.labyfy.component.transform.shadow.Shadow;

import java.io.File;

/**
 * A shadow class for the GameSettings.
 */
@Shadow("net.minecraft.client.GameSettings")
public interface GameSettingsAccessor {

  /**
   * Retrieves the option file.
   *
   * @return The option file.
   */
  @FieldGetter("optionsFile")
  File getOptionsFile();
}
