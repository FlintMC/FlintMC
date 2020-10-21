package net.labyfy.chat.event;

import net.labyfy.component.eventbus.event.subscribe.Subscribe;

import javax.inject.Singleton;

@Singleton
public class ChatListener {

  @Subscribe
  private void blub(ChatSendEvent event) {
    System.out.println("Yay " + event.getMessage());
  }
}
