package net.labyfy.component.gui.v1_15_2;

import net.labyfy.component.gui.MinecraftWindow;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.minecraft.client.Minecraft;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Implement(value = MinecraftWindow.class, version = "1.15.2")
public class LabyMinecraftWindow implements MinecraftWindow {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private LabyMinecraftWindow(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  @Override
  public long getHandle() {
    return Minecraft.getInstance().getMainWindow().getHandle();
  }

  @Override
  public int getScaleFactor() {
    return (int) Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
  }

  @Override
  public float getWidth() {
    return Minecraft.getInstance().getMainWindow().getWidth();
  }

  @Override
  public float getHeight() {
    return Minecraft.getInstance().getMainWindow().getHeight();
  }

  @Override
  public float getScaledWidth() {
    return Minecraft.getInstance().getMainWindow().getScaledWidth();
  }

  @Override
  public float getScaledHeight() {
    return Minecraft.getInstance().getMainWindow().getScaledHeight();
  }

  @Override
  public int getFramebufferWidth() {
    return Minecraft.getInstance().getFramebuffer().framebufferWidth;
  }

  @Override
  public int getFramebufferHeight() {
    return Minecraft.getInstance().getFramebuffer().framebufferHeight;
  }

  @Override
  public int getFPS() {
    return this.classMappingProvider
        .get("net.minecraft.client.Minecraft")
        .getField("debugFPS")
        .getValue(null);
  }
}
