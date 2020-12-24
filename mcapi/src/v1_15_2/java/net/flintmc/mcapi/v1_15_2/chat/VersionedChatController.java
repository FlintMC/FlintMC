package net.flintmc.mcapi.v1_15_2.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.ChatController;
import net.flintmc.mcapi.chat.ChatLocation;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

@Singleton
@Implement(value = ChatController.class, version = "1.15.2")
public class VersionedChatController implements ChatController {

  private static final int MAX_MESSAGES = 100;
  private static final int MAX_INPUT_LENGTH = 256;

  private final MinecraftComponentMapper componentMapper;

  @Inject
  private VersionedChatController(MinecraftComponentMapper componentMapper) {
    this.componentMapper = componentMapper;
  }

  @Override
  public boolean dispatchChatInput(String message) {
    if (message.length() >= this.getChatInputLimit()) {
      // the message is longer than the maximum allowed, servers would kick the player when sending
      // this
      message = message.substring(0, this.getChatInputLimit());
    }

    Minecraft.getInstance().player.sendChatMessage(message);
    Minecraft.getInstance().ingameGUI.getChatGUI().addToSentMessages(message);
    return true;
  }

  @Override
  public int getChatInputLimit() {
    return MAX_INPUT_LENGTH;
  }

  @Override
  public void displayChatMessage(ChatLocation location, ChatComponent component) {
    if (Minecraft.getInstance().ingameGUI == null) {
      return;
    }

    ChatType type;

    switch (location) {
      case CHAT:
        type = ChatType.CHAT;
        break;
      case ACTION_BAR:
        type = ChatType.GAME_INFO;
        break;
      case SYSTEM:
        type = ChatType.SYSTEM;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + location);
    }

    ITextComponent mapped = (ITextComponent) this.componentMapper.toMinecraft(component);
    Minecraft.getInstance().ingameGUI.addChatMessage(type, mapped);
  }

  @Override
  public List<String> getInputHistory() {
    if (Minecraft.getInstance().ingameGUI == null) {
      return new ArrayList<>();
    }
    return Minecraft.getInstance().ingameGUI.getChatGUI().getSentMessages();
  }

  @Override
  public List<ChatComponent> getReceivedMessages() {
    ChatGuiShadow shadow = (ChatGuiShadow) Minecraft.getInstance().ingameGUI.getChatGUI();
    List<ChatComponent> components = new ArrayList<>(shadow.getLines().size());

    for (ChatLine line : shadow.getLines()) {
      components.add(this.componentMapper.fromMinecraft(line.getChatComponent()));
    }

    return components;
  }

  @Override
  public int getMaxMessages() {
    return MAX_MESSAGES;
  }
}
