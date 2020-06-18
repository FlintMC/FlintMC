package net.labyfy.component.gui;

public interface MinecraftWindow {

  long getHandle();

  int getScaleFactor();

  float getWidth();

  float getHeight();

  float getScaledWidth();

  float getScaledHeight();

  int getFramebufferWidth();

  int getFramebufferHeight();

  int getFPS();

}
