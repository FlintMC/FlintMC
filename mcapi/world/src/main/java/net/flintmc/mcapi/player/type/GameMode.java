package net.flintmc.mcapi.player.type;

/**
 * An enumeration of all available game modes
 */
public enum GameMode {

  /**
   * Players have to gather their materials to build, craft items
   * and tools and gain experience points.
   */
  SURVIVAL,
  /**
   * Player has access to an infinite amount of almost all blocks
   * and items available, and can destroy them instantly.
   */
  CREATIVE,
  /**
   * Players can interact with objects as levers and buttons,
   * and can interact with mobs.
   * <br>
   * However, they can break blocks only with tools having a <b>CanDestroy</b>
   * data tag, and place blocks only if the block they are holding has a
   * <b>CanPlaceOn</b> data tag.
   */
  ADVENTURE,
  /**
   * Players can clip through blocks, enter the perspective of other entities
   * by left-clicking on them, and are invisible to all players and mobs except
   * for other spectator.
   */
  SPECTATOR;

  /**
   * Retrieves the game mode identifier
   *
   * @return The game mode identifier
   */
  public int getGameMode() {
    return this.ordinal();
  }

  /**
   * Retrieves the game mode name
   *
   * @return The game mode name
   */
  public String getName() {
    return this.name().toLowerCase();
  }

  /**
   * Whether the game mode has limited interactions.
   *
   * @return {@code true} if the game mode is adventure or spectator, otherwise {@code false}
   */
  public boolean hasLimitedInteraction() {
    return this == ADVENTURE || this == SPECTATOR;
  }

  /**
   * Whether the game mode is <b>creative</b>.
   *
   * @return {@code true} if the game mode is creative, otherwise {@code false}
   */
  public boolean isCreative() {
    return this == CREATIVE;
  }

  /**
   * Whether the game mode is <b>survival</b> or <b>adventure</b>.
   *
   * @return {@code true} if the game mode is survival or adventure, otherwise {@code false}
   */
  public boolean isSurvivalOrAdventure() {
    return this == SURVIVAL || this == ADVENTURE;
  }

}
