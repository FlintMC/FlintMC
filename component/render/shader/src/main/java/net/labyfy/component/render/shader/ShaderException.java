package net.labyfy.component.render.shader;

public class ShaderException extends RuntimeException {

  public ShaderException(String message, Throwable err) {
    super(message, err);
  }

  public ShaderException(String message) {
    super(message);
  }
}
