package net.flintmc.mcapi.v1_15_2.chat;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.gui.ChatLine;
import java.util.List;

@Shadow("net.minecraft.client.gui.NewChatGui")
public interface ChatGuiShadow {

  @FieldGetter("chatLines")
  List<ChatLine> getLines();

}
