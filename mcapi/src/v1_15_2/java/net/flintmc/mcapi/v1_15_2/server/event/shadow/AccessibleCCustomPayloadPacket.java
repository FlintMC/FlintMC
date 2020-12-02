package net.flintmc.mcapi.v1_15_2.server.event.shadow;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

/**
 * Shadow implementation of the {@link net.minecraft.network.play.client.CCustomPayloadPacket} with public getters for
 * the channel name and data.
 */
@Shadow("net.minecraft.network.play.client.CCustomPayloadPacket")
public interface AccessibleCCustomPayloadPacket {

  /**
   * Retrieves the custom payload channel name of this packet.
   *
   * @return The non-null channel name
   */
  @FieldGetter("channel")
  ResourceLocation getChannelName();

  /**
   * Retrieves the custom payload data of this packet.
   *
   * @return The non-null data
   */
  @FieldGetter("data")
  PacketBuffer getData();

}
