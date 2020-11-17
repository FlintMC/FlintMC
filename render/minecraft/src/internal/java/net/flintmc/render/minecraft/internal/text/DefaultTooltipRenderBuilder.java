package net.flintmc.render.minecraft.internal.text;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.minecraft.text.tooltip.TooltipRenderBuilder;
import net.flintmc.render.minecraft.text.tooltip.TooltipRenderer;

@Singleton
@Implement(TooltipRenderBuilder.class)
public class DefaultTooltipRenderBuilder implements TooltipRenderBuilder {

  private final TooltipRenderer renderer;

  private float x;
  private float y;
  private String text;

  @Inject
  private DefaultTooltipRenderBuilder(TooltipRenderer renderer) {
    this.renderer = renderer;
  }

  private void validate() {
    Preconditions.checkArgument(
        this.x >= 0 && this.y >= 0, "X/Y positions not set/set to something < 0");
    Preconditions.checkNotNull(this.text, "Text not set/set to null");
    Preconditions.checkArgument(!this.text.isEmpty(), "Text cannot be empty");
  }

  private void reset() {
    this.x = -1;
    this.y = -1;
    this.text = null;
  }

  /** {@inheritDoc} */
  @Override
  public TooltipRenderBuilder at(float x, float y) {
    this.x = x;
    this.y = y;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public TooltipRenderBuilder text(String text) {
    this.text = text;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public void draw() {
    this.validate();

    this.renderer.renderTooltip(this.x, this.y, this.text);

    this.reset();
  }
}
