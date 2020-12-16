package net.flintmc.framework.stereotype;

/** A helper for mathematical operations. */
public final class MathHelper {

  /**
   * Squares a number.
   *
   * @param number The number to be squared.
   * @return The squared number.
   */
  public static double square(double number) {
    return number * number;
  }

  /**
   * Restricts a number to be within a specified range.
   *
   * @param number The number to be restricted.
   * @param minimum The minimum desired value.
   * @param maximum The maximum desired value.
   * @return A value between {@code minimum} and {@code maximum}.
   */
  public static double clamp(double number, double minimum, double maximum) {
    return number < minimum ? minimum : Math.min(number, maximum);
  }
}
