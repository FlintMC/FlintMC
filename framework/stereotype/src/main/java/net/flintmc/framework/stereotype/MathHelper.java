package net.flintmc.framework.stereotype;

public class MathHelper {

  public static double square(double a) {
    return a * a;
  }

  public static double clamp(double number, double minimum, double maximum) {
    if(number < minimum) {
      return minimum;
    } else {
      return Math.min(number, maximum);
    }
  }
}
