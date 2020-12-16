package net.flintmc.mcapi.v1_15_2.server;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.network.NetworkManager;

/**
 * Shadow implementation of the {@link ConnectingScreen} in minecraft with a public getter to get
 * the network manager.
 */
@Shadow("net.minecraft.client.gui.screen.ConnectingScreen")
public interface ConnectingScreenShadow {

  /**
   * Retrieves the network manager of this screen which is used to connect with a server.
   *
   * @return The non-null network manager of this screen
   */
  @FieldGetter("networkManager")
  NetworkManager getNetworkManager();
}
