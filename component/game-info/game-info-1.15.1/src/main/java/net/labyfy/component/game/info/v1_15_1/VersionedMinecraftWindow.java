package net.labyfy.component.game.info.v1_15_1;

import net.labyfy.component.game.info.MinecraftWindow;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.minecraft.client.Minecraft;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.vecmath.Vector2f;

@Singleton
@Implement(value = MinecraftWindow.class, version = "1.15.1")
public class VersionedMinecraftWindow implements MinecraftWindow {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private VersionedMinecraftWindow(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  public long getHandle() {
    return Minecraft.getInstance().getMainWindow().getHandle();
  }

  public Vector2f getDimensions() {
    return new Vector2f(this.getWidth(), this.getHeight());
  }

  public Vector2f getScaledDimensions() {
    return new Vector2f(this.getScaledWidth(), this.getScaledHeight());
  }

  public int getScaleFactor() {
    return (int) Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
  }

  public float getWidth() {
    return Minecraft.getInstance().getMainWindow().getWidth();
  }

  public float getHeight() {
    return Minecraft.getInstance().getMainWindow().getHeight();
  }

  public float getScaledWidth() {
    return Minecraft.getInstance().getMainWindow().getScaledWidth();
  }

  public float getScaledHeight() {
    return Minecraft.getInstance().getMainWindow().getScaledHeight();
  }

  public int getFPS() {
    return this.classMappingProvider
        .get("net.minecraft.client.Minecraft")
        .getField("debugFPS")
        .getValue(null);
  }
}
