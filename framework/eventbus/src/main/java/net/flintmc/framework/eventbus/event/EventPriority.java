package net.flintmc.framework.eventbus.event;

import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

/**
 * Represents any example priorities for events fired on listener methods.
 *
 * @see Subscribe
 */
public final class EventPriority {

  public static final byte FIRST = Byte.MAX_VALUE;
  public static final byte EARLY = 64;
  public static final byte NORMAL = 0;
  public static final byte LATE = -64;
  public static final byte LAST = Byte.MIN_VALUE;
}
