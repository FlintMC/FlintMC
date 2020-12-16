package net.flintmc.mcapi.server.payload;

import com.google.common.collect.Multimap;
import net.flintmc.mcapi.resources.ResourceLocation;

/** Represents a service that registers or unregisters payload channels. */
public interface PayloadChannelService {

  /**
   * Registers a new payload channel listener.
   *
   * @param namespace The namespace of the payload channel.
   * @param path The path of the payload channel.
   * @param payloadChannelListener The listener for the payload channel.
   */
  void registerPayloadChannel(
      String namespace, String path, PayloadChannelListener payloadChannelListener);

  /**
   * Registers a new payload channel listener.
   *
   * @param resourceLocation The channel identifier for the payload channel.
   * @param payloadChannelListener The payload channel listener.
   */
  void registerPayloadChannel(
      ResourceLocation resourceLocation, PayloadChannelListener payloadChannelListener);

  /**
   * Unregister all payload channel listeners bound to the given resource location.
   *
   * @param resourceLocation The channel identifier of the payload channel listeners to be removed.
   */
  void unregisterPayloadChannels(ResourceLocation resourceLocation);

  /**
   * Unregister the given payload channel listener.
   *
   * @param payloadChannelListener The payload channel listener to be removed.
   */
  void unregisterPayloadChannel(PayloadChannelListener payloadChannelListener);

  /**
   * Unregister the the payload channel.
   *
   * @param resourceLocation The channel identifier of the payload channel.
   * @param payloadChannelListener The payload channel listener to be removed.
   */
  void unregisterPayloadChannel(
      ResourceLocation resourceLocation, PayloadChannelListener payloadChannelListener);

  /**
   * Whether the payload channel should be listened to.
   *
   * @param channelIdentifier The identifier of the payload channel.
   * @param buffer The non-null Minecraft packet buffer.
   * @return {@code true} if the payload channel should be listened to, otherwise {@code false}.
   */
  boolean shouldListen(String channelIdentifier, Object buffer);

  /**
   * Retrieves a key-value system with all registered payload channels.
   *
   * @return A key-value system with all registered payload channels.
   */
  Multimap<ResourceLocation, PayloadChannelListener> getPayloadChannels();
}
