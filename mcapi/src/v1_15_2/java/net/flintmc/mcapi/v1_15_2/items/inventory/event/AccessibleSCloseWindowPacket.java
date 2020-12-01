package net.flintmc.mcapi.v1_15_2.items.inventory.event;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;

@Shadow("net.minecraft.network.play.server.SCloseWindowPacket")
public interface AccessibleSCloseWindowPacket {

  @FieldGetter("windowId")
  int getWindowId();

}
