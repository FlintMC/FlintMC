package net.flintmc.mcapi.v1_15_2.resources;

import java.io.File;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;

@Shadow("net.minecraft.client.resources.SkinManager")
public interface SkinManagerShadow {

  @FieldGetter("skinCacheDir")
  File getSkinCacheDirectory();

}
