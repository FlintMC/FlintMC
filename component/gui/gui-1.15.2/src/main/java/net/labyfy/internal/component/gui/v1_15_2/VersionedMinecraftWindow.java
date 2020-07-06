package net.labyfy.internal.component.gui.v1_15_2;

import net.labyfy.component.gui.MinecraftWindow;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.minecraft.client.Minecraft;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Implement(value = MinecraftWindow.class, version = "1.15.2")
public class VersionedMinecraftWindow implements MinecraftWindow {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private VersionedMinecraftWindow(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getHandle() {
    return Minecraft.getInstance().getMainWindow().getHandle();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getScaleFactor() {
    return (int) Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getWidth() {
    return Minecraft.getInstance().getMainWindow().getWidth();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHeight() {
    return Minecraft.getInstance().getMainWindow().getHeight();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getScaledWidth() {
    return Minecraft.getInstance().getMainWindow().getScaledWidth();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getScaledHeight() {
    return Minecraft.getInstance().getMainWindow().getScaledHeight();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFramebufferWidth() {
    return Minecraft.getInstance().getFramebuffer().framebufferWidth;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFramebufferHeight() {
    return Minecraft.getInstance().getFramebuffer().framebufferHeight;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFPS() {
    return this.classMappingProvider
        .get("net.minecraft.client.Minecraft")
        .getField("debugFPS")
        .getValue(null);
  }

  public boolean isIngame() {
    return Minecraft.getInstance().world != null;
  }
}
