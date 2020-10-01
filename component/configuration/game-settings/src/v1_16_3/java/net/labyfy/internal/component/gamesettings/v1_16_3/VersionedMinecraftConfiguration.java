package net.labyfy.internal.component.gamesettings.v1_16_3;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gamesettings.MinecraftConfiguration;
import net.labyfy.component.gamesettings.configuration.*;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.client.Minecraft;

/**
 * 1.16.3 implementation of {@link MinecraftConfiguration}.
 */
@Singleton
@Implement(value = MinecraftConfiguration.class, version = "1.16.3")
public class VersionedMinecraftConfiguration implements MinecraftConfiguration {

  private final AccessibilityConfiguration accessibilityConfiguration;
  private final ChatConfiguration chatConfiguration;
  private final DebugConfiguration debugConfiguration;
  private final GraphicConfiguration graphicConfiguration;
  private final KeyBindingConfiguration keyBindingConfiguration;
  private final MouseConfiguration mouseConfiguration;
  private final ResourcePackConfiguration resourcePackConfiguration;
  private final SkinConfiguration skinConfiguration;
  private final SoundConfiguration soundConfiguration;

  @Inject
  private VersionedMinecraftConfiguration(
          AccessibilityConfiguration accessibilityConfiguration,
          ChatConfiguration chatConfiguration,
          DebugConfiguration debugConfiguration,
          GraphicConfiguration graphicConfiguration,
          KeyBindingConfiguration keyBindingConfiguration,
          MouseConfiguration mouseConfiguration,
          ResourcePackConfiguration resourcePackConfiguration,
          SkinConfiguration skinConfiguration,
          SoundConfiguration soundConfiguration
  ) {
    this.accessibilityConfiguration = accessibilityConfiguration;
    this.chatConfiguration = chatConfiguration;
    this.debugConfiguration = debugConfiguration;
    this.graphicConfiguration = graphicConfiguration;
    this.keyBindingConfiguration = keyBindingConfiguration;
    this.mouseConfiguration = mouseConfiguration;
    this.resourcePackConfiguration = resourcePackConfiguration;
    this.skinConfiguration = skinConfiguration;
    this.soundConfiguration = soundConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AccessibilityConfiguration getAccessibilityConfiguration() {
    return this.accessibilityConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatConfiguration getChatConfiguration() {
    return this.chatConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DebugConfiguration getDebugConfiguration() {
    return this.debugConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GraphicConfiguration getGraphicConfiguration() {
    return this.graphicConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBindingConfiguration getKeyBindingConfiguration() {
    return this.keyBindingConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MouseConfiguration getMouseConfiguration() {
    return this.mouseConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourcePackConfiguration getResourcePackConfiguration() {
    return this.resourcePackConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SkinConfiguration getSkinConfiguration() {
    return this.skinConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SoundConfiguration getSoundConfiguration() {
    return this.soundConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRealmsNotifications() {
    return Minecraft.getInstance().gameSettings.realmsNotifications;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRealmsNotifications(boolean realmsNotifications) {
    Minecraft.getInstance().gameSettings.realmsNotifications = realmsNotifications;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getDifficulty() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDifficulty(Object difficulty) {
    Minecraft.getInstance().gameSettings.saveOptions();
  }


}
