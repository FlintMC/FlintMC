package net.flintmc.mcapi.v1_15_2.server;

import com.google.common.base.Preconditions;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerAddress;

import java.net.InetSocketAddress;

@Implement(value = ServerAddress.class, version = "1.15.2")
public class VersionedServerAddress implements ServerAddress {

  private final String ip;
  private final int port;

  @AssistedInject
  public VersionedServerAddress(@Assisted("ip") String ip, @Assisted("port") int port) {
    this.ip = ip;
    this.port = port;
  }

  @AssistedInject
  public VersionedServerAddress(@Assisted("rawAddress") String rawAddress) {
    net.minecraft.client.multiplayer.ServerAddress address =
        net.minecraft.client.multiplayer.ServerAddress.fromString(rawAddress);
    Preconditions.checkNotNull(address, "address");

    this.ip = address.getIP();
    this.port = address.getPort();
  }

  @AssistedInject
  public VersionedServerAddress(@Assisted("socketAddress") InetSocketAddress address) {
    this(address.getAddress().getHostName(), address.getPort());
  }

  /** {@inheritDoc} */
  @Override
  public String getIP() {
    return this.ip;
  }

  /** {@inheritDoc} */
  @Override
  public int getPort() {
    return this.port;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return this.ip + ":" + this.port;
  }
}
