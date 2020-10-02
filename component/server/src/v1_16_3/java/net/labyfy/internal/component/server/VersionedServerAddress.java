package net.labyfy.internal.component.server;

import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.server.ServerAddress;

@Implement(value = ServerAddress.class, version = "1.16.3")
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
    net.minecraft.client.multiplayer.ServerAddress address = net.minecraft.client.multiplayer.ServerAddress.fromString(rawAddress);
    Preconditions.checkNotNull(address, "address");

    this.ip = address.getIP();
    this.port = address.getPort();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIP() {
    return this.ip;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPort() {
    return this.port;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.ip + ":" + this.port;
  }
}
