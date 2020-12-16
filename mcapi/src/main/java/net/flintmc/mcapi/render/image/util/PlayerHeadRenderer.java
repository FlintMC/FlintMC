package net.flintmc.mcapi.render.image.util;

import net.flintmc.mcapi.player.network.NetworkPlayerInfo;

/** Renderer for the head texture of players that are online on the current server. */
public interface PlayerHeadRenderer {

  /**
   * Draws the head of the given info at the given position with the given size.
   *
   * @param x The x position of the top-left corner of the head to be rendered on the screen
   * @param y The y position of the top-left corner of the head to be rendered on the screen
   * @param size The width and height in pixels of the head to be rendered on the screen
   * @param info The non-null info containing the resource location to the skin of the player
   */
  void drawPlayerHead(float x, float y, float size, NetworkPlayerInfo info);
}
