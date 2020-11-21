package net.flintmc.util.mojang;

import net.flintmc.util.mojang.history.NameHistoryResolver;
import net.flintmc.util.mojang.profile.GameProfileResolver;

/**
 * This exception is being fired by the {@link GameProfileResolver} and {@link NameHistoryResolver}
 * when Mojang has rate limited our IP address.
 */
public class MojangRateLimitException extends RuntimeException {

  public static MojangRateLimitException INSTANCE = new MojangRateLimitException();

  private MojangRateLimitException() {
    super("Got rate limited by Mojang");
  }
}
