package net.flintmc.util.mojang;

public class MojangRateLimitException extends RuntimeException {

  public static MojangRateLimitException INSTANCE = new MojangRateLimitException();

  private MojangRateLimitException() {
    super("Got rate limited by Mojang");
  }
}
