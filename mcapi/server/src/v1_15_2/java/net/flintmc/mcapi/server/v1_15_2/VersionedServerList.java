package net.flintmc.mcapi.server.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerList;

/** {@inheritDoc} */
@Singleton
@Implement(value = net.flintmc.mcapi.server.ServerList.class, version = "1.15.2")
public class VersionedServerList implements net.flintmc.mcapi.server.ServerList {

  private final ServerList mcServerList;
  private final ServerData.Factory serverDataFactory;
  private final ServerAddress.Factory serverAddressFactory;

  @Inject
  private VersionedServerList(
      ServerData.Factory serverDataFactory, ServerAddress.Factory serverAddressFactory) {
    this.serverDataFactory = serverDataFactory;
    this.serverAddressFactory = serverAddressFactory;
    this.mcServerList = new ServerList(Minecraft.getInstance());
    this.mcServerList.loadServerList();
  }

  /** {@inheritDoc} */
  @Override
  public void saveServerList() {
    this.mcServerList.saveServerList();
  }

  /** {@inheritDoc} */
  @Override
  public ServerData getServer(int index) {
    net.minecraft.client.multiplayer.ServerData data = this.mcServerList.getServerData(index);
    return this.serverDataFactory.create(
        data.serverName,
        this.serverAddressFactory.parse(data.serverIP),
        ServerData.ResourceMode.valueOf(data.getResourceMode().name()));
  }

  /** {@inheritDoc} */
  @Override
  public int size() {
    return this.mcServerList.countServers();
  }

  /** {@inheritDoc} */
  @Override
  public void updateServerData(int index, ServerData server) {
    net.minecraft.client.multiplayer.ServerData data = this.mcServerList.getServerData(index);
    data.serverIP = server.getServerAddress().getIP() + ":" + server.getServerAddress().getPort();
    data.serverName = server.getName();
    data.setResourceMode(
        net.minecraft.client.multiplayer.ServerData.ServerResourceMode.valueOf(
            server.getResourceMode().name()));
  }

  /** {@inheritDoc} */
  @Override
  public void addServer(ServerData server) {
    this.mcServerList.addServerData(createNMSServerData(server));
  }

  /** {@inheritDoc} */
  @Override
  public void addServer(int index, ServerData server) {
    ((ServerListShadow) this.mcServerList)
        .getServerDataList()
        .add(index, createNMSServerData(server));
  }

  /** {@inheritDoc} */
  private net.minecraft.client.multiplayer.ServerData createNMSServerData(ServerData server) {
    String ip = server.getServerAddress().getIP() + ":" + server.getServerAddress().getPort();
    net.minecraft.client.multiplayer.ServerData data =
        new net.minecraft.client.multiplayer.ServerData(server.getName(), ip, false);
    data.setResourceMode(
        net.minecraft.client.multiplayer.ServerData.ServerResourceMode.valueOf(
            server.getResourceMode().name()));
    return data;
  }

  /** {@inheritDoc} */
  @Override
  public void removeServer(int index) {
    ((ServerListShadow) this.mcServerList).getServerDataList().remove(index);
  }
}
