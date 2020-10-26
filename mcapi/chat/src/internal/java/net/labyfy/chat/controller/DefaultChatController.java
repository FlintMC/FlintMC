package net.labyfy.chat.controller;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.controller.filter.ChatFilter;
import net.labyfy.chat.controller.filter.FilterableChatMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public abstract class DefaultChatController implements ChatController {

  private static final UUID NO_SENDER = new UUID(0, 0);

  private final Collection<Chat> chats;
  private final Collection<ChatFilter> filters;

  protected DefaultChatController() {
    this.chats = new ArrayList<>();
    this.filters = new ArrayList<>();

    this.addChat(this.createChat(0)); // the default main chat
  }

  @Override
  public boolean displayChatMessage(ChatComponent component) {
    return this.displayChatMessage(component, NO_SENDER);
  }

  @Override
  public ChatFilter[] getFilters() {
    return this.filters.toArray(new ChatFilter[0]);
  }

  protected boolean processMessage(FilterableChatMessage message) {
    this.processFilters(message);
    if (!message.isAllowed()) {
      return false;
    }

    message.getTargetChat().displayChatMessage(message);
    return true;
  }

  @Override
  public void processFilters(FilterableChatMessage message) {
    for (ChatFilter filter : this.filters) {
      filter.apply(this, message);
    }
  }

  @Override
  public void addFilter(ChatFilter filter) {
    this.filters.add(filter);
  }

  @Override
  public void removeFilter(ChatFilter filter) {
    this.filters.removeIf(rm -> rm.getUniqueId().equals(filter.getUniqueId()));
  }

  @Override
  public void addChat(Chat chat) {
    for (Chat registered : this.chats) {
      if (registered.getId() == chat.getId()) {
        throw new IllegalStateException("Chat with the id " + chat.getId() + " is already registered");
      }
    }
    this.chats.add(chat);
  }

  @Override
  public Chat createChat(int id) {
    return new DefaultChat(id);
  }

  @Override
  public Chat getMainChat() {
    Chat chat = this.getChat(0);
    if (chat == null) {
      // the main chat has been removed by something
      throw new IllegalStateException("No main chat has been registered");
    }
    return chat;
  }

  @Override
  public Chat getChat(int id) {
    for (Chat chat : this.chats) {
      if (chat.getId() == id) {
        return chat;
      }
    }
    return null;
  }

  @Override
  public Chat[] getChats() {
    return this.chats.toArray(new Chat[0]);
  }
}
