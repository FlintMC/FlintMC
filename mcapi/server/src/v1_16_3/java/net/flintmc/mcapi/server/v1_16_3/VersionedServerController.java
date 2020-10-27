package net.flintmc.mcapi.server.v1_16_3;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ConnectedServer;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.*;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.util.text.TranslationTextComponent;

@Singleton
@Implement(value = ServerController.class, version = "1.16.3")
public class VersionedServerController implements ServerController {

  private final ConnectedServer connectedServer;

  @Inject
  public VersionedServerController(ConnectedServer connectedServer) {
    this.connectedServer = connectedServer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isConnected() {
    return this.connectedServer.isConnected();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConnectedServer getConnectedServer() {
    return this.isConnected() ? this.connectedServer : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disconnectFromServer() {
    if (Minecraft.getInstance().world != null) {
      // rendering has to be called from Minecraft's main thread
      Minecraft.getInstance().execute(this::disconnectNow);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectToServer(ServerAddress address) {
    // rendering has to be called from Minecraft's main thread
    Minecraft.getInstance().execute(() -> {
      this.disconnectNow();

      Screen currentScreen = Minecraft.getInstance().currentScreen;
      Minecraft.getInstance().displayGuiScreen(new ConnectingScreen(currentScreen, Minecraft.getInstance(), address.getIP(), address.getPort()));
    });
  }

  private void disconnectNow() {
    if (Minecraft.getInstance().world == null) {
      return;
    }

    boolean integrated = Minecraft.getInstance().isIntegratedServerRunning();
    boolean realms = Minecraft.getInstance().isConnectedToRealms();
    Minecraft.getInstance().world.sendQuittingDisconnectingPacket();

    if (integrated) {
      Minecraft.getInstance().unloadWorld(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
    } else {
      Minecraft.getInstance().unloadWorld();
    }

    if (integrated) {
      Minecraft.getInstance().displayGuiScreen(new MainMenuScreen());
    } else if (realms) {
      RealmsBridgeScreen bridge = new RealmsBridgeScreen();
      bridge.func_231394_a_(new MainMenuScreen());
    } else {
      Minecraft.getInstance().displayGuiScreen(new MultiplayerScreen(new MainMenuScreen()));
    }
  }
}
