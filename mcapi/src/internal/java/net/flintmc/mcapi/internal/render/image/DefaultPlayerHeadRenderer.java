package net.flintmc.mcapi.internal.render.image;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.render.image.ImageRenderer;
import net.flintmc.mcapi.render.image.head.PlayerHeadRenderer;

@Singleton
@Implement(PlayerHeadRenderer.class)
public class DefaultPlayerHeadRenderer implements PlayerHeadRenderer {

  private final ImageRenderer imageRenderer;

  @Inject
  private DefaultPlayerHeadRenderer(ImageRenderer imageRenderer) {
    this.imageRenderer = imageRenderer;
  }

  /** {@inheritDoc} */
  @Override
  public void drawPlayerHead(float x, float y, float size, NetworkPlayerInfo info) {
    this.imageRenderer.bindTexture(info.getSkinLocation());
    this.imageRenderer.drawPartImage(
        x, y, 8, 8, 8, null, 8, 8, 64, 64, size, size, 255, 255, 255, 255);
  }
}
