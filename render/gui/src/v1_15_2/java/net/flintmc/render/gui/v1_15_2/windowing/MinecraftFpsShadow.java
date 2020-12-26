package net.flintmc.render.gui.v1_15_2.windowing;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;

@Shadow("net.minecraft.client.Minecraft")
public interface MinecraftFpsShadow {

  @FieldGetter("debugFPS")
  int getFPS();

}
