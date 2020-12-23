package net.flintmc.mcapi.world.math;

import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.MathHelper;

/**
 * Represents a horizontal direction.
 */
public enum Direction {
  SOUTH(
      "NORTH",
      "South",
      "S",
      new Direction.AxisDirection[]{AxisDirection.Z_POSITIVE},
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(0, 0, 1)),
  SOUTH_WEST(
      "NORTH_EAST",
      "South-West",
      "SW",
      new Direction.AxisDirection[]{AxisDirection.X_NEGATIVE, AxisDirection.Z_POSITIVE},
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(-1, 0, 1)),
  WEST(
      "EAST",
      "West",
      "W",
      new Direction.AxisDirection[]{AxisDirection.X_NEGATIVE},
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(-1, 0, 0)),
  NORTH_WEST(
      "SOUTH_EAST",
      "North-West",
      "NW",
      new Direction.AxisDirection[]{AxisDirection.X_NEGATIVE, AxisDirection.Z_NEGATIVE},
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(-1, 0, -1)),
  NORTH(
      "SOUTH",
      "North",
      "N",
      new Direction.AxisDirection[]{AxisDirection.Z_NEGATIVE},
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(0, 0, -1)),
  NORTH_EAST(
      "SOUTH_WEST",
      "North-East",
      "NE",
      new Direction.AxisDirection[]{AxisDirection.X_POSITIVE, AxisDirection.Z_NEGATIVE},
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(1, 0, -1)),
  EAST(
      "WEST",
      "East",
      "E",
      new Direction.AxisDirection[]{AxisDirection.X_POSITIVE},
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(1, 0, 0)),
  SOUTH_EAST(
      "NORTH_WEST",
      "South-East",
      "SE",
      new Direction.AxisDirection[]{AxisDirection.X_POSITIVE, AxisDirection.Z_POSITIVE},
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(1, 0, 1));

  private static final Direction[] DIRECTIONS = Direction.values();

  private final String oppositeDirection;
  private final String name;
  private final String symbol;
  private final Direction.AxisDirection[] axisDirections;
  private final Vector3D directionVector;

  Direction(
      String oppositeDirection,
      String name,
      String symbol,
      AxisDirection[] axisDirections,
      Vector3D directionVector) {
    this.oppositeDirection = oppositeDirection;
    this.name = name;
    this.symbol = symbol;
    this.axisDirections = axisDirections;
    this.directionVector = directionVector;
  }

  /**
   * Retrieves the direction from an angle
   *
   * @param angle The angle
   * @return The direction retrieved from the angle
   */
  public static Direction fromAngle(double angle) {
    int directionIndex = MathHelper.floor((angle / 45) + 0.5) & 7;
    return DIRECTIONS[directionIndex];
  }

  /**
   * @return The opposite direction of this direction
   */
  public Direction getOpposite() {
    return Direction.valueOf(this.oppositeDirection);
  }

  /**
   * @return The english name of this direction
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return The starting chars of this direction
   */
  public String getSymbol() {
    return this.symbol;
  }

  /**
   * @return The axis directions of this direction
   */
  public AxisDirection[] getAxisDirections() {
    return this.axisDirections;
  }

  /**
   * @return This direction as a normalized vector
   */
  public Vector3D getDirectionVector() {
    return this.directionVector;
  }

  /**
   * Represents a horizontal axis direction
   */
  public enum AxisDirection {
    X_POSITIVE("X+", "X", 1),
    X_NEGATIVE("X-", "X", -1),
    Z_POSITIVE("Z+", "Z", 1),
    Z_NEGATIVE("Z-", "Z", -1);

    private final String name;
    private final String axis;
    private final int number;

    AxisDirection(String name, String axis, int number) {
      this.name = name;
      this.axis = axis;
      this.number = number;
    }

    /**
     * @return The name of this axis direction
     */
    public String getName() {
      return this.name;
    }

    /**
     * @return The axis this direction is targeting
     */
    public String getAxis() {
      return this.axis;
    }

    /**
     * @return This direction as a normalized number
     */
    public int getAsNumber() {
      return this.number;
    }
  }
}
