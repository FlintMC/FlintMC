package net.labyfy.internal.component.player.v1_15_2.overlay;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.overlay.TabOverlay;
import net.labyfy.component.player.overlay.AccessibleTabOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

/**
 * 1.15.2 implementation of {@link TabOverlay}
 */
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
    AccessibleTabOverlay accessibleTabOverlay = (AccessibleTabOverlay) Minecraft.getInstance().ingameGUI.getTabList();
    return this.minecraftComponentMapper.fromMinecraft(accessibleTabOverlay.getHeader());
  }

  /**
   * Updates the header of this player.
   *
   * @param header The new header content
   */
  @Override
  public void updateHeader(ChatComponent header) {
    Minecraft.getInstance().ingameGUI.getTabList().setFooter(
            (ITextComponent) this.minecraftComponentMapper.toMinecraft(header)
    );
  }

  /**
   * Retrieves the footer of this player.
   *
   * @return The footer of this player.
   */
  @Override
  public ChatComponent getFooter() {
    AccessibleTabOverlay accessibleTabOverlay = (AccessibleTabOverlay) Minecraft.getInstance().ingameGUI.getTabList();
    return this.minecraftComponentMapper.fromMinecraft(accessibleTabOverlay.getFooter());
  }

  /**
   * Updates the footer of this player.
   *
   * @param footer The new footer content
   */
  @Override
  public void updateFooter(ChatComponent footer) {
    Minecraft.getInstance().ingameGUI.getTabList().setFooter(
            (ITextComponent) this.minecraftComponentMapper.toMinecraft(footer)
    );
  }

}
