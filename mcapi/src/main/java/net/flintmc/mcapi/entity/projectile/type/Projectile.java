package net.flintmc.mcapi.entity.projectile.type;

/**
 * Represents a projectile.
 */
public interface Projectile {

  /**
   * Shoots the projectile.
   *
   * @param x          The x position of the projectile.
   * @param y          The y position of the projectile.
   * @param z          The z position of the projectile.
   * @param velocity   The velocity of the projectile.
   * @param inaccuracy The inaccuracy of the projectile.
   */
  void shoot(double x, double y, double z, float velocity, float inaccuracy);
}
