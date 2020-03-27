package net.labyfy.component.game.info;

import javax.vecmath.Vector2f;

public interface MinecraftWindow {

  long getHandle();

  Vector2f getDimensions();

  Vector2f getScaledDimensions();

  int getScaleFactor();

  float getWidth();

  float getHeight();

  float getScaledWidth();

  float getScaledHeight();
}
