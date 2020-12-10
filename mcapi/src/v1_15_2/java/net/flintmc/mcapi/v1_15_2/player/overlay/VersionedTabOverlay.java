package net.flintmc.mcapi.v1_15_2.player.overlay;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.player.overlay.TabOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

/** 1.15.2 implementation of {@link TabOverlay} */
@Singleton
@Implement(value = TabOverlay.class, version = "1.15.2")
public class VersionedTabOverlay implements TabOverlay {

  private final MinecraftComponentMapper minecraftComponentMapper;

  @Inject
  private VersionedTabOverlay(MinecraftComponentMapper minecraftComponentMapper) {
    this.minecraftComponentMapper = minecraftComponentMapper;
  }

  /**
   * Retrieves the header of this player.
   *
   * @return The header of this player
   */
  @Override
  public ChatComponent getHeader() {
    AccessibleTabOverlay accessibleTabOverlay =
        (AccessibleTabOverlay) Minecraft.getInstance().ingameGUI.getTabList();
    return this.minecraftComponentMapper.fromMinecraft(accessibleTabOverlay.getHeader());
  }

  /**
   * Updates the header of this player.
   *
   * @param header The new header content
   */
  @Override
  public void updateHeader(ChatComponent header) {
    Minecraft.getInstance()
        .ingameGUI
        .getTabList()
        .setFooter(
            header == null
                ? null
                : (ITextComponent) this.minecraftComponentMapper.toMinecraft(header));
  }

  /**
   * Retrieves the footer of this player.
   *
   * @return The footer of this player.
   */
  @Override
  public ChatComponent getFooter() {
    AccessibleTabOverlay accessibleTabOverlay =
        (AccessibleTabOverlay) Minecraft.getInstance().ingameGUI.getTabList();
    return this.minecraftComponentMapper.fromMinecraft(accessibleTabOverlay.getFooter());
  }

  /**
   * Updates the footer of this player.
   *
   * @param footer The new footer content
   */
  @Override
  public void updateFooter(ChatComponent footer) {
    Minecraft.getInstance()
        .ingameGUI
        .getTabList()
        .setFooter(
            footer == null
                ? null
                : (ITextComponent) this.minecraftComponentMapper.toMinecraft(footer));
  }
}
