package net.flintmc.mcapi.v1_15_2.gamesettings.configuration;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.gamesettings.configuration.ChatConfiguration;
import net.flintmc.mcapi.gamesettings.settings.ChatVisibility;
import net.flintmc.mcapi.gamesettings.settings.NarratorStatus;
import net.minecraft.client.Minecraft;

/** 1.15.2 implementation of {@link ChatConfiguration} */
@Singleton
@Implement(value = ChatConfiguration.class, version = "1.15.2")
public class VersionedChatConfiguration implements ChatConfiguration {

  /** {@inheritDoc} */
  @Override
  public double getChatScale() {
    return Minecraft.getInstance().gameSettings.chatScale;
  }

  /** {@inheritDoc} */
  @Override
  public void setChatScale(double chatScale) {
    Minecraft.getInstance().gameSettings.chatScale = chatScale;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public double getChatWidth() {
    return Minecraft.getInstance().gameSettings.chatWidth;
  }

  /** {@inheritDoc} */
  @Override
  public void setChatWidth(double chatWidth) {
    Minecraft.getInstance().gameSettings.chatWidth = chatWidth;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public double getChatHeightUnfocused() {
    return Minecraft.getInstance().gameSettings.chatHeightUnfocused;
  }

  /** {@inheritDoc} */
  @Override
  public void setChatHeightUnfocused(double chatHeightUnfocused) {
    Minecraft.getInstance().gameSettings.chatHeightUnfocused = chatHeightUnfocused;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public double getChatHeightFocused() {
    return Minecraft.getInstance().gameSettings.chatHeightFocused;
  }

  /** {@inheritDoc} */
  @Override
  public void setChatHeightFocused(double chatHeightFocused) {
    Minecraft.getInstance().gameSettings.chatHeightFocused = chatHeightFocused;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public double getChatDelay() {
    return 0;
  }

  /** {@inheritDoc} */
  @Override
  public void setChatDelay(double chatDelay) {
    // NO-OP
  }

  /** {@inheritDoc} */
  @Override
  public ChatVisibility getChatVisibility() {
    switch (Minecraft.getInstance().gameSettings.chatVisibility) {
      case FULL:
        return ChatVisibility.FULL;
      case SYSTEM:
        return ChatVisibility.SYSTEM;
      case HIDDEN:
        return ChatVisibility.HIDDEN;
      default:
        throw new IllegalStateException(
            "Unexpected value: " + Minecraft.getInstance().gameSettings.chatVisibility);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setChatVisibility(ChatVisibility chatVisibility) {
    switch (chatVisibility) {
      case FULL:
        Minecraft.getInstance().gameSettings.chatVisibility =
            net.minecraft.entity.player.ChatVisibility.FULL;
        break;
      case SYSTEM:
        Minecraft.getInstance().gameSettings.chatVisibility =
            net.minecraft.entity.player.ChatVisibility.SYSTEM;
        break;
      case HIDDEN:
        Minecraft.getInstance().gameSettings.chatVisibility =
            net.minecraft.entity.player.ChatVisibility.HIDDEN;
        break;
    }
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public double getChatOpacity() {
    return Minecraft.getInstance().gameSettings.chatOpacity;
  }

  /** {@inheritDoc} */
  @Override
  public void setChatOpacity(double chatOpacity) {
    Minecraft.getInstance().gameSettings.chatOpacity = chatOpacity;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public double getChatLineSpacing() {
    return 0;
  }

  /** {@inheritDoc} */
  @Override
  public void setChatLineSpacing(double chatLineSpacing) {
    // NO-OP
  }

  /** {@inheritDoc} */
  @Override
  public boolean isChatColor() {
    return Minecraft.getInstance().gameSettings.chatColor;
  }

  /** {@inheritDoc} */
  @Override
  public void setChatColor(boolean chatColor) {
    Minecraft.getInstance().gameSettings.chatColor = chatColor;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isChatLinks() {
    return Minecraft.getInstance().gameSettings.chatLinks;
  }

  /** {@inheritDoc} */
  @Override
  public void setChatLinks(boolean chatLinks) {
    Minecraft.getInstance().gameSettings.chatLinks = chatLinks;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isChatLinksPrompt() {
    return Minecraft.getInstance().gameSettings.chatLinksPrompt;
  }

  /** {@inheritDoc} */
  @Override
  public void setChatLinksPrompt(boolean chatLinksPrompt) {
    Minecraft.getInstance().gameSettings.chatLinksPrompt = chatLinksPrompt;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public NarratorStatus getNarrator() {
    switch (Minecraft.getInstance().gameSettings.narrator) {
      case OFF:
        return NarratorStatus.OFF;
      case ALL:
        return NarratorStatus.ALL;
      case CHAT:
        return NarratorStatus.CHAT;
      case SYSTEM:
        return NarratorStatus.SYSTEM;
      default:
        throw new IllegalStateException(
            "Unexpected value: " + Minecraft.getInstance().gameSettings.narrator);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setNarrator(NarratorStatus narrator) {
    switch (narrator) {
      case OFF:
        Minecraft.getInstance().gameSettings.narrator =
            net.minecraft.client.settings.NarratorStatus.OFF;
        Minecraft.getInstance().gameSettings.saveOptions();
        break;
      case ALL:
        Minecraft.getInstance().gameSettings.narrator =
            net.minecraft.client.settings.NarratorStatus.ALL;
        Minecraft.getInstance().gameSettings.saveOptions();
        break;
      case CHAT:
        Minecraft.getInstance().gameSettings.narrator =
            net.minecraft.client.settings.NarratorStatus.CHAT;
        Minecraft.getInstance().gameSettings.saveOptions();
        break;
      case SYSTEM:
        Minecraft.getInstance().gameSettings.narrator =
            net.minecraft.client.settings.NarratorStatus.SYSTEM;
        Minecraft.getInstance().gameSettings.saveOptions();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + narrator);
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAccessibilityTextBackground() {
    return Minecraft.getInstance().gameSettings.accessibilityTextBackground;
  }

  /** {@inheritDoc} */
  @Override
  public void setAccessibilityTextBackground(boolean accessibilityTextBackground) {
    Minecraft.getInstance().gameSettings.accessibilityTextBackground = accessibilityTextBackground;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isReducedDebugInfo() {
    return Minecraft.getInstance().gameSettings.reducedDebugInfo;
  }

  /** {@inheritDoc} */
  @Override
  public void setReducedDebugInfo(boolean reducedDebugInfo) {
    Minecraft.getInstance().gameSettings.reducedDebugInfo = reducedDebugInfo;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAutoSuggestCommands() {
    return Minecraft.getInstance().gameSettings.autoSuggestCommands;
  }

  /** {@inheritDoc} */
  @Override
  public void setAutoSuggestCommands(boolean autoSuggestCommands) {
    Minecraft.getInstance().gameSettings.autoSuggestCommands = autoSuggestCommands;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public double getAccessibilityTextBackgroundOpacity() {
    return Minecraft.getInstance().gameSettings.accessibilityTextBackgroundOpacity;
  }

  /** {@inheritDoc} */
  @Override
  public void setAccessibilityTextBackgroundOpacity(double accessibilityTextBackgroundOpacity) {
    Minecraft.getInstance().gameSettings.accessibilityTextBackgroundOpacity =
        accessibilityTextBackgroundOpacity;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isShowSubtitles() {
    return Minecraft.getInstance().gameSettings.showSubtitles;
  }

  /** {@inheritDoc} */
  @Override
  public void setShowSubtitles(boolean showSubtitles) {
    Minecraft.getInstance().gameSettings.showSubtitles = showSubtitles;
    Minecraft.getInstance().gameSettings.saveOptions();
  }
}
