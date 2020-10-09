package net.labyfy.component.player.overlay;

import net.labyfy.component.transform.shadow.FieldGetter;
import net.labyfy.component.transform.shadow.Shadow;

@Shadow("net.minecraft.client.gui.overlay.PlayerTabOverlayGui")
public interface TabOverlayAccessor {

  @FieldGetter("header")
  Object getHeader();

  @FieldGetter("footer")
  Object getFooter();

}
