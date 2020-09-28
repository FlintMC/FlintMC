package net.labyfy.component.gamesettings.settings;

public enum ChatVisibility {

  FULL("options.chat.visibility.full"),
  SYSTEM("options.chat.visibility.system"),
  HIDDEN("options.chat.visibility.hidden");

  private final String resourceKey;

  ChatVisibility(String resourceKey) {
    this.resourceKey = resourceKey;
  }

  public String getResourceKey() {
    return resourceKey;
  }
}
