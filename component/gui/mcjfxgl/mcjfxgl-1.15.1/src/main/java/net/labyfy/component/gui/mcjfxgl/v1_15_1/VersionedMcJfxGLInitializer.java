package net.labyfy.component.gui.mcjfxgl.v1_15_1;

import cuchaz.jfxgl.JFXGL;
import net.labyfy.component.gui.mcjfxgl.McJfxGLInitializer;
import net.labyfy.component.gui.mcjfxgl.McJfxGLApplication;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.client.Minecraft;

import javax.inject.Singleton;

@Singleton
@Implement(value = McJfxGLInitializer.class, version = "1.15.1")
public class VersionedMcJfxGLInitializer implements McJfxGLInitializer {

  public void initialize(McJfxGLApplication mcJfxGLApplication) {
    JFXGL.start(Minecraft.getInstance().getMainWindow().getHandle(), new String[] {}, mcJfxGLApplication);
  }

}
