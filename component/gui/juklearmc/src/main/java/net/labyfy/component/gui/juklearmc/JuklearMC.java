package net.labyfy.component.gui.juklearmc;

import net.janrupf.juklear.JuklearContext;
import net.janrupf.juklear.font.JuklearFontAtlas;

/**
 * Juklear management system providing an advanced GUI on top of minecraft.
 */
public interface JuklearMC {
  /**
   * Retrieves the context the Juklear engine is using.
   *
   * @return The context used by Juklear
   */
  JuklearContext getContext();

  JuklearFontAtlas getFontAtlas();
}
