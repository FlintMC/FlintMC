package net.flintmc.mcapi.world.math;

import net.flintmc.framework.inject.primitive.InjectionHolder;

/** Represents a direction. */
public enum Direction {
  DOWN(
      "UP",
      "Down",
      "D",
      "y",
      Direction.AxisDirection.NEGATIVE,
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(0, -1, 0)),
  UP(
      "DOWN",
      "Up",
      "U",
      "y",
      Direction.AxisDirection.POSITIVE,
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(0, 1, 0)),
  NORTH(
      "SOUTH",
      "North",
      "N",
      "Z",
      Direction.AxisDirection.NEGATIVE,
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(0, 0, -1)),
  SOUTH(
      "NORTH",
      "South",
      "S",
      "Z",
      Direction.AxisDirection.POSITIVE,
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(0, 0, 1)),
  WEST(
      "EAST",
      "West",
      "W",
      "X",
      Direction.AxisDirection.NEGATIVE,
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(-1, 0, 0)),
  EAST(
      "WEST",
      "East",
      "E",
      "X",
      Direction.AxisDirection.POSITIVE,
      InjectionHolder.getInjectedInstance(Vector3D.Factory.class).create(1, 0, 0));

  private final String oppositeDirection;
  private final String name;
  private final String symbol;
  private final String axis;
  private final Direction.AxisDirection axisDirection;
  private final Vector3D directionVector;

  Direction(
      String oppositeDirection,
      String name,
      String symbol,
      String axis,
      Direction.AxisDirection axisDirection,
      Vector3D directionVector) {
    this.oppositeDirection = oppositeDirection;
    this.name = name;
    this.symbol = symbol;
    this.axis = axis;
    this.axisDirection = axisDirection;
    this.directionVector = directionVector;
  }

  /** @return The opposite direction of this direction */
  public Direction getOpposite() {
    return Direction.valueOf(this.oppositeDirection);
  }

  /** @return The english name of this direction */
  public String getName() {
    return this.name;
  }

  /** @return The name of this direction as a single char */
  public String getSymbol() {
    return this.symbol;
  }

  /** @return The name of the axis going in the same direction as this direction */
  public String getAxis() {
    return this.axis;
  }

  /** @return The direction of the axis of this direction */
  public AxisDirection getAxisDirection() {
    return this.axisDirection;
  }

  /** @return This direction as a normalized vector */
  public Vector3D getDirectionVector() {
    return this.directionVector;
  }

  /** Represents the direction of the axis of a certain horizontal direction */
  public enum AxisDirection {
    POSITIVE(1, "positive", "+"),
    NEGATIVE(-1, "negative", "-");

    private final int number;
    private final String name;
    private final String symbol;

    AxisDirection(int number, String name, String symbol) {
      this.number = number;
      this.name = name;
      this.symbol = symbol;
    }

    /** @return This direction as a normalized number */
    public int getAsNumber() {
      return this.number;
    }

    /** @return The english name of this direction */
    public String getName() {
      return this.name;
    }

    /** @return The mathematical symbol of this direction */
    public String getSymbol() {
      return this.symbol;
    }
  }
}
