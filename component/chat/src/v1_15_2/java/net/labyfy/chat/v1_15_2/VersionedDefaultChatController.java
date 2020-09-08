package net.labyfy.chat.v1_15_2;

import com.google.inject.Singleton;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.controller.ChatController;
import net.labyfy.chat.controller.DefaultChatController;
import net.labyfy.chat.controller.DefaultFilterableChatMessage;
import net.labyfy.chat.controller.filter.FilterableChatMessage;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
@Implement(value = ChatController.class, version = "1.15.2")
public class VersionedDefaultChatController extends DefaultChatController {

  @Override
  public boolean dispatchChatInput(String message) {
    if (message.length() >= this.getChatInputLimit()) {
      // the message is longer than the maximum allowed, servers would kick the player when sending this
      message = message.substring(0, this.getChatInputLimit());
    }

    Minecraft.getInstance().player.sendChatMessage(message);
    return true;
  }

  @Override
  public int getChatInputLimit() {
    return 256;
  }

  @Override
  public boolean displayChatMessage(ChatComponent component, UUID senderUniqueId) {
    if (Minecraft.getInstance().ingameGUI == null) {
      return false;
    }

    FilterableChatMessage message = new DefaultFilterableChatMessage(component, this.getMainChat(), senderUniqueId);
    return super.processMessage(message);
  }

  @Override
  public List<String> getInputHistory() {
    if (Minecraft.getInstance().ingameGUI == null) {
      return new ArrayList<>();
    }
    return Minecraft.getInstance().ingameGUI.getChatGUI().getSentMessages();
  }
}
