package net.flintmc.mcapi.server.payload;

import net.flintmc.mcapi.server.buffer.PacketBuffer;

/**
 * Represents a listener that listens to payload channels.
 */
public interface PayloadChannelListener {

  /**
   * Listens to a payload channel that the {@link PayloadChannel} annotation has.
   *
   * @param buffer The payload of the heard channel.
   */
  void listen(PacketBuffer buffer);

  /**
   * A factory class for {@link PayloadChannelListener}.
   */
  interface Factory {

    /**
     * Creates a new {@link PayloadChannel}.
     *
     * @return A created payload channel.
     */
    PayloadChannelListener create();
  }
}
