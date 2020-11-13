package net.flintmc.render.minecraft.geometric;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.minecraft.client.gui.AbstractGui;

@Singleton
@Implement(value = RectangleRenderer.class, version = "1.15.2")
public class VersionedRectangleRenderer implements RectangleRenderer {
  @Override
  public void drawRectBorder(int x, int y, int width, int height, int rgba, int borderWidth) {
    int left = x;
    int top = y;
    int right = left + width;
    int bottom = y + height;

    AbstractGui.fill(left, top, left + borderWidth, bottom, rgba); // left
    AbstractGui.fill(right - borderWidth, top, right, bottom, rgba); // right
    AbstractGui.fill(left, top, right, top + borderWidth, rgba); // top
    AbstractGui.fill(left, bottom - borderWidth, right, bottom, rgba); // bottom
  }

  @Override
  public void drawFilledRect(int x, int y, int width, int height, int rgba) {
    AbstractGui.fill(x, y, x + width, y + height, rgba);
  }
}
