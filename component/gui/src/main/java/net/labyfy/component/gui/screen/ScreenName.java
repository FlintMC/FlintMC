package net.labyfy.component.gui.screen;

import java.util.Objects;

public final class ScreenName {
  public static final String MAIN_MENU = "main_menu";
  public static final String RESOURCE_LOAD = "resource_load";
  public static final String OPTIONS = "options";
  public static final String MULTIPLAYER = "multiplayer";
  public static final String SINGLEPLAYER = "singleplayer";

  private final Type type;
  private final String identifier;

  private ScreenName(Type type, String identifier) {
    this.type = type;
    this.identifier = identifier;
  }

  public static ScreenName typed(Type type, String identifier) {
    return new ScreenName(type, identifier);
  }

  public static ScreenName minecraft(String identifier) {
    return new ScreenName(Type.FROM_MINECRAFT, identifier);
  }

  public static ScreenName unknown(String clazz) {
    return new ScreenName(Type.UNKNOWN, clazz);
  }

  public String getIdentifier() {
    return identifier;
  }

  public Type getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ScreenName that = (ScreenName) o;
    return type == that.type &&
        Objects.equals(identifier, that.identifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, identifier);
  }

  public enum Type {
    FROM_MINECRAFT,
    UNKNOWN
  }
}
