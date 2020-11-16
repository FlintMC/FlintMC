package net.flintmc.render.minecraft.v1_15_2.text;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.minecraft.text.raw.FontRenderer;
import net.flintmc.render.minecraft.text.raw.StringAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.fonts.Font;

@Singleton
@Implement(value = FontRenderer.class, version = "1.15.2")
public class VersionedFontRenderer implements FontRenderer {

  private final Minecraft minecraft;

  @Inject
  private VersionedFontRenderer() {
    this.minecraft = Minecraft.getInstance();
  }

  @Override
  public int getStringWidth(String text) {
    return this.minecraft.fontRenderer.getStringWidth(text);
  }

  @Override
  public float getCharWidth(char c) {
    return this.minecraft.fontRenderer.getCharWidth(c);
  }

  @Override
  public float getBoldCharWidth(char c) {
    Font font = ((ShadowFontRenderer) this.minecraft.fontRenderer).getFont();
    return font.findGlyph(c).getAdvance(true);
  }

  @Override
  public void drawString(
      float x,
      float y,
      String text,
      int rgba,
      StringAlignment alignment,
      int maxLineLength,
      boolean shadow,
      float xFactor,
      float yFactor) {
    boolean scale = xFactor != 1 || yFactor != 1;
    if (scale) {
      RenderSystem.scalef(xFactor, yFactor, 1F);
    }

    switch (alignment) {
      case RIGHT:
        x -= this.getStringWidth(text);
        break;

      case CENTER:
        x += (float) this.getStringWidth(text) / 2F;

      default:
      case LEFT:
        break;
    }

    if (maxLineLength != 0) {
      this.minecraft.fontRenderer.drawSplitString(text, (int) x, (int) y, maxLineLength, rgba);
    } else {
      if (shadow) {
        this.minecraft.fontRenderer.drawStringWithShadow(text, x, y, rgba);
      } else {
        this.minecraft.fontRenderer.drawString(text, x, y, rgba);
      }
    }

    if (scale) {
      RenderSystem.scalef(1F / xFactor, 1F / yFactor, 1F);
    }
  }
}
