package net.flintmc.mcapi.v1_15_2.render.text;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.List;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.render.text.tooltip.TooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

@Singleton
@Implement(value = TooltipRenderer.class, version = "1.15.2")
public class VersionedTooltipRenderer implements TooltipRenderer {

  private final Minecraft minecraft;
  private final Screen screen;

  @Inject
  private VersionedTooltipRenderer() {
    this.minecraft = Minecraft.getInstance();
    this.screen = new Screen(new StringTextComponent("")) {};
    this.screen.init(minecraft, 0, 0);
  }

  /** {@inheritDoc} */
  @Override
  public void renderTooltip(float x, float y, String text) {
    int width = this.minecraft.getMainWindow().getScaledWidth();
    int height = this.minecraft.getMainWindow().getScaledHeight();
    List<String> lines =
        this.minecraft.fontRenderer.listFormattedStringToWidth(text, Math.min(width / 2, 200));

    this.screen.setSize(width, height);

    this.screen.renderTooltip(lines, (int) x, (int) y);
  }
}
