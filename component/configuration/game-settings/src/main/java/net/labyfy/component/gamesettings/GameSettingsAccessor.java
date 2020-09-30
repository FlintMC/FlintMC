package net.labyfy.component.gamesettings;

import net.labyfy.component.transform.shadow.FieldGetter;
import net.labyfy.component.transform.shadow.Shadow;

import java.io.File;

@Shadow("net.minecraft.client.GameSettings")
public interface GameSettingsAccessor {

  @FieldGetter("optionsFile")
  File getOptionsFile();
}
