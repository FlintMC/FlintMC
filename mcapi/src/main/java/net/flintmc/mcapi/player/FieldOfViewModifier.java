package net.flintmc.mcapi.player;

/**
 * Represents the fov modifier of the player.
 */
public interface FieldOfViewModifier {

  /**
   * Retrieves the fov modifier of the player.
   *
   * @param fov The fov of the player.
   * @return The new modified fov.
   */
  float fieldOfView(float fov);
}
