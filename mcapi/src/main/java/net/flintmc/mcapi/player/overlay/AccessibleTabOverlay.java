package net.flintmc.mcapi.player.overlay;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;

/**
 * A shadow interface for the player tab overlay gui.
 */
@Shadow("net.minecraft.client.gui.overlay.PlayerTabOverlayGui")
public interface AccessibleTabOverlay {

  /**
   * Retrieves the tab overlay header of the client player.
   *
   * @return The tab overlay header.
   */
  @FieldGetter("header")
  Object getHeader();

  /**
   * Retrieves the tab overlay footer of the client player.
   *
   * @return The tab overlay footer.
   */
  @FieldGetter("footer")
  Object getFooter();

}
