package net.flintmc.mcapi.v1_15_2.server.event.shadow;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.util.text.ITextComponent;

/**
 * Shadow implementation of the {@link net.minecraft.client.gui.screen.DisconnectedScreen} in minecraft with a public getter to
 * get the message component.
 */
@Shadow("net.minecraft.client.gui.screen.DisconnectedScreen")
public interface AccessibleDisconnectedScreen {

  /**
   * Retrieves the message that will be displayed in this screen.
   *
   * @return The non-null message of this screen
   */
  @FieldGetter("message")
  ITextComponent getMessage();

}
