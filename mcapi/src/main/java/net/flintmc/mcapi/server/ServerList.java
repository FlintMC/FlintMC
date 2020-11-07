package net.flintmc.mcapi.server;

/** Represents the minecraft server list. */
public interface ServerList {

  /** Saves the server list to disk. */
  void saveServerList();

  /**
   * Gets a server list entry.
   *
   * @param index the index of the entry to get
   * @return the server list entry
   */
  ServerData getServer(int index);

  /** @return the size of the server list */
  int size();

  /**
   * Updates the content of the entry at a given index to tbe provided data.
   *
   * @param index the index of the server list entry to update
   * @param server the data to update the entry from
   */
  void updateServerData(int index, ServerData server);

  /**
   * Adds a server to the server list
   *
   * @param server the data describing the server to add
   */
  void addServer(ServerData server);

  /**
   * Adds a server to the server list at a given index.
   *
   * @param index the index at which to add the server to the list
   * @param server the data describing the server to add
   */
  void addServer(int index, ServerData server);

  /**
   * Removes a server from the server list.
   *
   * @param index the index of the server to remove.
   */
  void removeServer(int index);
}
