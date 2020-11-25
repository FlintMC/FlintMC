package net.flintmc.mcapi.v1_15_2.server;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.ConnectedServer;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerStatusResolver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CCustomPayloadPacket;

@Singleton
@Implement(value = ConnectedServer.class, version = "1.15.2")
public class VersionedConnectedServer implements ConnectedServer {

  private final ServerStatusResolver statusResolver;
  private final ServerAddress.Factory serverAddressFactory;

  @Inject
  public VersionedConnectedServer(
      ServerStatusResolver statusResolver, ServerAddress.Factory serverAddressFactory) {
    this.statusResolver = statusResolver;
    this.serverAddressFactory = serverAddressFactory;
  }

  private SocketAddress getRawAddress() {
    ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();
    return connection != null ? connection.getNetworkManager().getRemoteAddress() : null;
  }

  /** {@inheritDoc} */
  @Override
  public ServerAddress getAddress() {
    SocketAddress address = this.getRawAddress();
    if (address instanceof InetSocketAddress) {
      InetSocketAddress inetAddress = (InetSocketAddress) address;
      return this.serverAddressFactory.create(
          inetAddress.getAddress().getHostAddress(), inetAddress.getPort());
    }

    return null;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isConnected() {
    return this.getRawAddress() instanceof InetSocketAddress;
  }

  /** {@inheritDoc} */
  @Override
  public CompletableFuture<ServerStatus> resolveStatus() {
    ServerAddress address = this.getAddress();
    Preconditions.checkState(address != null, "Not connected with any server");

    try {
      return this.statusResolver.resolveStatus(address);
    } catch (UnknownHostException exception) {
      return CompletableFuture.completedFuture(null);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void sendCustomPayload(ResourceLocation identifier, byte[] payload) {
    ClientPlayNetHandler handler = this.getConnection();

    handler.sendPacket(
        new CCustomPayloadPacket(
            identifier.getHandle(),
            new PacketBuffer(Unpooled.wrappedBuffer(payload))));
  }

  @Override
  public void retrieveCustomPayload(String identifier, byte[] payload) {
    ByteBuf buffer = Unpooled.wrappedBuffer(payload);
    System.out.println(buffer);
    System.out.println("Retrieved custom payload...");
    System.out.println("Identifier: " +  identifier);
  }

  private ClientPlayNetHandler getConnection() {
    ClientPlayNetHandler handler = Minecraft.getInstance().getConnection();
    Preconditions.checkState(handler != null, "Not connected with any server");
    return handler;
  }
}
