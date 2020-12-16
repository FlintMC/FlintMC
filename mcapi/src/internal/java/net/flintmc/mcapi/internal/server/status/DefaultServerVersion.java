package net.flintmc.mcapi.internal.server.status;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.status.ServerVersion;

@Implement(ServerVersion.class)
public class DefaultServerVersion implements ServerVersion {

  private final String name;
  private final int protocolVersion;
  private final boolean compatible;

  @AssistedInject
  public DefaultServerVersion(
      @Assisted("name") String name,
      @Assisted("protocolVersion") int protocolVersion,
      @Assisted("compatible") boolean compatible) {
    this.name = name;
    this.protocolVersion = protocolVersion;
    this.compatible = compatible;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public int getProtocolVersion() {
    return this.protocolVersion;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCompatible() {
    return this.compatible;
  }
}
