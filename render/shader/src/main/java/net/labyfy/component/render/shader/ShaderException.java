package net.labyfy.component.render.shader;

/** Represents an exception that happened while loading, linking or using a shader program. */
public class ShaderException extends Exception {

  /**
   * @param message A message that describes the fault
   * @param err The fault that caused this exception
   */
  public ShaderException(String message, Throwable err) {
    super(message, err);
  }

  /** @param message A message that describes the fault */
  public ShaderException(String message) {
    super(message);
  }
}
