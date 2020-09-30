package net.labyfy.internal.component.gamesettings.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gamesettings.KeyBinding;
import net.labyfy.component.gamesettings.MinecraftConfiguration;
import net.labyfy.component.gamesettings.settings.*;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.PlayerClothing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.HandSide;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
@Implement(value = MinecraftConfiguration.class, version = "1.15.2")
public class VersionedMinecraftConfiguration implements MinecraftConfiguration {

  private final KeyBinding.Factory keyBindingFactory;

  @Inject
  public VersionedMinecraftConfiguration(KeyBinding.Factory keyBindingFactory) {
    this.keyBindingFactory = keyBindingFactory;
  }

  @Override
  public boolean isHideServerAddress() {
    return Minecraft.getInstance().gameSettings.hideServerAddress;
  }

  @Override
  public void setHideServerAddress(boolean hideServerAddress) {
    Minecraft.getInstance().gameSettings.hideServerAddress = hideServerAddress;
    this.saveOptions();
  }

  @Override
  public boolean isAdvancedItemTooltips() {
    return Minecraft.getInstance().gameSettings.advancedItemTooltips;
  }

  @Override
  public void setAdvancedItemTooltips(boolean advancedItemTooltips) {
    Minecraft.getInstance().gameSettings.advancedItemTooltips = advancedItemTooltips;
    this.saveOptions();
  }

  @Override
  public boolean isPauseOnLostFocus() {
    return Minecraft.getInstance().gameSettings.pauseOnLostFocus;
  }

  @Override
  public void setPauseOnLostFocus(boolean pauseOnLostFocus) {
    Minecraft.getInstance().gameSettings.pauseOnLostFocus = pauseOnLostFocus;
    this.saveOptions();
  }

  @Override
  public int getOverrideWidth() {
    return Minecraft.getInstance().gameSettings.overrideWidth;
  }

  @Override
  public void setOverrideWidth(int overrideWidth) {
    Minecraft.getInstance().gameSettings.overrideWidth = overrideWidth;
    this.saveOptions();
  }

  @Override
  public int getOverrideHeight() {
    return Minecraft.getInstance().gameSettings.overrideHeight;
  }

  @Override
  public void setOverrideHeight(int overrideHeight) {
    Minecraft.getInstance().gameSettings.overrideHeight = overrideHeight;
    this.saveOptions();
  }

  @Override
  public boolean isHeldItemTooltips() {
    return Minecraft.getInstance().gameSettings.heldItemTooltips;
  }

  @Override
  public void setHeldItemTooltips(boolean heldItemTooltips) {
    Minecraft.getInstance().gameSettings.heldItemTooltips = heldItemTooltips;
    this.saveOptions();
  }


  @Override
  public boolean isUseNativeTransport() {
    return Minecraft.getInstance().gameSettings.useNativeTransport;
  }

  @Override
  public void setUseNativeTransport(boolean useNativeTransport) {
    Minecraft.getInstance().gameSettings.useNativeTransport = useNativeTransport;
    this.saveOptions();
  }

  @Override
  public TutorialSteps getTutorialStep() {
    switch (Minecraft.getInstance().gameSettings.tutorialStep) {
      case MOVEMENT:
        return TutorialSteps.MOVEMENT;
      case FIND_TREE:
        return TutorialSteps.FIND_TREE;
      case PUNCH_TREE:
        return TutorialSteps.PUNCH_TREE;
      case OPEN_INVENTORY:
        return TutorialSteps.OPEN_INVENTORY;
      case CRAFT_PLANKS:
        return TutorialSteps.CRAFT_PLANKS;
      case NONE:
        return TutorialSteps.NONE;
      default:
        throw new IllegalStateException("Unexpected value: " + Minecraft.getInstance().gameSettings.tutorialStep);
    }
  }

  @Override
  public void setTutorialStep(TutorialSteps tutorialStep) {
    switch (tutorialStep) {
      case MOVEMENT:
        Minecraft.getInstance().gameSettings.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.MOVEMENT;
        this.saveOptions();
        break;
      case FIND_TREE:
        Minecraft.getInstance().gameSettings.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.FIND_TREE;
        this.saveOptions();
        break;
      case PUNCH_TREE:
        Minecraft.getInstance().gameSettings.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.PUNCH_TREE;
        this.saveOptions();
        break;
      case OPEN_INVENTORY:
        Minecraft.getInstance().gameSettings.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.OPEN_INVENTORY;
        this.saveOptions();
        break;
      case CRAFT_PLANKS:
        Minecraft.getInstance().gameSettings.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.CRAFT_PLANKS;
        this.saveOptions();
        break;
      case NONE:
        Minecraft.getInstance().gameSettings.tutorialStep = net.minecraft.client.tutorial.TutorialSteps.NONE;
        this.saveOptions();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + tutorialStep);
    }
  }

  @Override
  public int getGlDebugVerbosity() {
    return Minecraft.getInstance().gameSettings.glDebugVerbosity;
  }

  @Override
  public void setGlDebugVerbosity(int glDebugVerbosity) {
    Minecraft.getInstance().gameSettings.glDebugVerbosity = glDebugVerbosity;
    this.saveOptions();
  }

  @Override
  public boolean isForceUnicodeFont() {
    return Minecraft.getInstance().gameSettings.forceUnicodeFont;
  }

  @Override
  public void setForceUnicodeFont(boolean forceUnicodeFont) {
    Minecraft.getInstance().gameSettings.forceUnicodeFont = forceUnicodeFont;
    this.saveOptions();
  }

  @Override
  public boolean isRealmsNotifications() {
    return Minecraft.getInstance().gameSettings.realmsNotifications;
  }

  @Override
  public void setRealmsNotifications(boolean realmsNotifications) {
    Minecraft.getInstance().gameSettings.realmsNotifications = realmsNotifications;
    this.saveOptions();
  }

  @Override
  public boolean isSnooper() {
    return Minecraft.getInstance().gameSettings.snooper;
  }

  @Override
  public void setSnooper(boolean snooper) {
    Minecraft.getInstance().gameSettings.snooper = snooper;
    this.saveOptions();
  }

  @Override
  public boolean isSkipMultiplayerWarning() {
    return Minecraft.getInstance().gameSettings.field_230152_Z_;
  }

  @Override
  public void setSkipMultiplayerWarning(boolean skipMultiplayerWarning) {
    Minecraft.getInstance().gameSettings.field_230152_Z_ = skipMultiplayerWarning;
    this.saveOptions();
  }

  @Override
  public Object getDifficulty() {
    return null;
  }

  @Override
  public void setDifficulty(Object difficulty) {

    this.saveOptions();
  }

  @Override
  public boolean isHideGUI() {
    return Minecraft.getInstance().gameSettings.hideGUI;
  }

  @Override
  public void setHideGUI(boolean hideGUI) {
    Minecraft.getInstance().gameSettings.hideGUI = hideGUI;
    this.saveOptions();
  }

  @Override
  public PointOfView getPointOfView() {
    switch (Minecraft.getInstance().gameSettings.thirdPersonView) {
      case 0:
        return PointOfView.FIRST_PERSON;
      case 1:
        return PointOfView.THIRD_PERSON_BACK;
      case 2:
        return PointOfView.THIRD_PERSON_FRONT;
      default:
        throw new IllegalStateException("Unexpected value: " + Minecraft.getInstance().gameSettings.thirdPersonView);
    }
  }

  @Override
  public void setPointOfView(PointOfView pointOfView) {
    switch (pointOfView) {
      case FIRST_PERSON:
        Minecraft.getInstance().gameSettings.thirdPersonView = 0;
        this.saveOptions();
        break;
      case THIRD_PERSON_BACK:
        Minecraft.getInstance().gameSettings.thirdPersonView = 1;
        this.saveOptions();
        break;
      case THIRD_PERSON_FRONT:
        Minecraft.getInstance().gameSettings.thirdPersonView = 2;
        this.saveOptions();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + pointOfView);
    }
  }

  @Override
  public boolean isShowDebugInfo() {
    return Minecraft.getInstance().gameSettings.showDebugInfo;
  }

  @Override
  public void setShowDebugInfo(boolean showDebugInfo) {
    Minecraft.getInstance().gameSettings.showDebugInfo = showDebugInfo;
    this.saveOptions();
  }

  @Override
  public boolean isShowDebugProfilerChart() {
    return Minecraft.getInstance().gameSettings.showDebugProfilerChart;
  }

  @Override
  public void setShowDebugProfilerChart(boolean showDebugProfilerChart) {
    Minecraft.getInstance().gameSettings.showDebugProfilerChart = showDebugProfilerChart;
    this.saveOptions();
  }

  @Override
  public boolean isShowLagometer() {
    return Minecraft.getInstance().gameSettings.showLagometer;
  }

  @Override
  public void setShowLagometer(boolean showLagometer) {
    Minecraft.getInstance().gameSettings.showLagometer = showLagometer;
    this.saveOptions();
  }

  @Override
  public String getLastServer() {
    return Minecraft.getInstance().gameSettings.lastServer;
  }

  @Override
  public void setLastServer(String lastServer) {
    Minecraft.getInstance().gameSettings.lastServer = lastServer;
    this.saveOptions();
  }

  @Override
  public boolean isSmoothCamera() {
    return Minecraft.getInstance().gameSettings.smoothCamera;
  }

  @Override
  public void setSmoothCamera(boolean smoothCamera) {
    Minecraft.getInstance().gameSettings.smoothCamera = smoothCamera;
    this.saveOptions();
  }

  @Override
  public String getLanguage() {
    return Minecraft.getInstance().gameSettings.language;
  }

  @Override
  public void setLanguage(String language) {
    Minecraft.getInstance().gameSettings.language = language;
    this.saveOptions();
  }

  @Override
  public boolean isChunkSyncWrites() {
    return false;
  }

  @Override
  public void setChunkSyncWrites(boolean chunkSyncWrites) {
    // NO-OP
  }

  @Override
  public double getChatScale() {
    return Minecraft.getInstance().gameSettings.chatScale;
  }

  @Override
  public void setChatScale(double chatScale) {
    Minecraft.getInstance().gameSettings.chatScale = chatScale;
    this.saveOptions();
  }

  @Override
  public double getChatWidth() {
    return Minecraft.getInstance().gameSettings.chatWidth;
  }

  @Override
  public void setChatWidth(double chatWidth) {
    Minecraft.getInstance().gameSettings.chatWidth = chatWidth;
    this.saveOptions();
  }

  @Override
  public double getChatHeightUnfocused() {
    return Minecraft.getInstance().gameSettings.chatHeightUnfocused;
  }

  @Override
  public void setChatHeightUnfocused(double chatHeightUnfocused) {
    Minecraft.getInstance().gameSettings.chatHeightUnfocused = chatHeightUnfocused;
    this.saveOptions();
  }

  @Override
  public double getChatHeightFocused() {
    return Minecraft.getInstance().gameSettings.chatHeightFocused;
  }

  @Override
  public void setChatHeightFocused(double chatHeightFocused) {
    Minecraft.getInstance().gameSettings.chatHeightFocused = chatHeightFocused;
    this.saveOptions();
  }

  @Override
  public double getChatDelay() {
    return 0;
  }

  @Override
  public void setChatDelay(double chatDelay) {
    // NO-OP
  }

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
        throw new IllegalStateException("Unexpected value: " + Minecraft.getInstance().gameSettings.chatVisibility);
    }
  }

  @Override
  public void setChatVisibility(ChatVisibility chatVisibility) {
    switch (chatVisibility) {
      case FULL:
        Minecraft.getInstance().gameSettings.chatVisibility = net.minecraft.entity.player.ChatVisibility.FULL;
        this.saveOptions();
        break;
      case SYSTEM:
        Minecraft.getInstance().gameSettings.chatVisibility = net.minecraft.entity.player.ChatVisibility.SYSTEM;
        this.saveOptions();
        break;
      case HIDDEN:
        Minecraft.getInstance().gameSettings.chatVisibility = net.minecraft.entity.player.ChatVisibility.HIDDEN;
        this.saveOptions();
        break;
    }
  }

  @Override
  public double getChatOpacity() {
    return Minecraft.getInstance().gameSettings.chatOpacity;
  }

  @Override
  public void setChatOpacity(double chatOpacity) {
    Minecraft.getInstance().gameSettings.chatOpacity = chatOpacity;
    this.saveOptions();
  }

  @Override
  public double getChatLineSpacing() {
    return 0;
  }

  @Override
  public void setChatLineSpacing(double chatLineSpacing) {
    // NO-OP
  }

  @Override
  public boolean isChatColor() {
    return Minecraft.getInstance().gameSettings.chatColor;
  }

  @Override
  public void setChatColor(boolean chatColor) {
    Minecraft.getInstance().gameSettings.chatColor = chatColor;
    this.saveOptions();
  }

  @Override
  public boolean isChatLinks() {
    return Minecraft.getInstance().gameSettings.chatLinks;
  }

  @Override
  public void setChatLinks(boolean chatLinks) {
    Minecraft.getInstance().gameSettings.chatLinks = chatLinks;
    this.saveOptions();
  }

  @Override
  public boolean isChatLinksPrompt() {
    return Minecraft.getInstance().gameSettings.chatLinksPrompt;
  }

  @Override
  public void setChatLinksPrompt(boolean chatLinksPrompt) {
    Minecraft.getInstance().gameSettings.chatLinksPrompt = chatLinksPrompt;
    this.saveOptions();
  }

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
        throw new IllegalStateException("Unexpected value: " + Minecraft.getInstance().gameSettings.narrator);
    }
  }

  @Override
  public void setNarrator(NarratorStatus narrator) {
    switch (narrator) {
      case OFF:
        Minecraft.getInstance().gameSettings.narrator = net.minecraft.client.settings.NarratorStatus.OFF;
        this.saveOptions();
        break;
      case ALL:
        Minecraft.getInstance().gameSettings.narrator = net.minecraft.client.settings.NarratorStatus.ALL;
        this.saveOptions();
        break;
      case CHAT:
        Minecraft.getInstance().gameSettings.narrator = net.minecraft.client.settings.NarratorStatus.CHAT;
        this.saveOptions();
        break;
      case SYSTEM:
        Minecraft.getInstance().gameSettings.narrator = net.minecraft.client.settings.NarratorStatus.SYSTEM;
        this.saveOptions();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + narrator);
    }
  }

  @Override
  public boolean isAccessibilityTextBackground() {
    return Minecraft.getInstance().gameSettings.accessibilityTextBackground;
  }

  @Override
  public void setAccessibilityTextBackground(boolean accessibilityTextBackground) {
    Minecraft.getInstance().gameSettings.accessibilityTextBackground = accessibilityTextBackground;
    this.saveOptions();
  }

  @Override
  public boolean isReducedDebugInfo() {
    return Minecraft.getInstance().gameSettings.reducedDebugInfo;
  }

  @Override
  public void setReducedDebugInfo(boolean reducedDebugInfo) {
    Minecraft.getInstance().gameSettings.reducedDebugInfo = reducedDebugInfo;
    this.saveOptions();
  }

  @Override
  public boolean isAutoSuggestCommands() {
    return Minecraft.getInstance().gameSettings.autoSuggestCommands;
  }

  @Override
  public void setAutoSuggestCommands(boolean autoSuggestCommands) {
    Minecraft.getInstance().gameSettings.autoSuggestCommands = autoSuggestCommands;
    this.saveOptions();
  }

  @Override
  public double getAccessibilityTextBackgroundOpacity() {
    return Minecraft.getInstance().gameSettings.accessibilityTextBackgroundOpacity;
  }

  @Override
  public void setAccessibilityTextBackgroundOpacity(double accessibilityTextBackgroundOpacity) {
    Minecraft.getInstance().gameSettings.accessibilityTextBackgroundOpacity = accessibilityTextBackgroundOpacity;
    this.saveOptions();
  }

  @Override
  public KeyBinding getKeyBindForward() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindForward);
  }

  @Override
  public KeyBinding getKeyBindLeft() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindLeft);
  }

  @Override
  public KeyBinding getKeyBindBack() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindBack);
  }

  @Override
  public KeyBinding getKeyBindRight() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindRight);
  }

  @Override
  public KeyBinding getKeyBindJump() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindJump);
  }

  @Override
  public KeyBinding getKeyBindSneak() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSneak);
  }

  @Override
  public KeyBinding getKeyBindSprint() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSprint);
  }

  @Override
  public KeyBinding getKeyBindInventory() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindInventory);
  }

  @Override
  public KeyBinding getKeyBindSwapHands() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSwapHands);
  }

  @Override
  public KeyBinding getKeyBindDrop() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindDrop);
  }

  @Override
  public KeyBinding getKeyBindUseItem() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindUseItem);
  }

  @Override
  public KeyBinding getKeyBindAttack() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindAttack);
  }

  @Override
  public KeyBinding getKeyBindPickBlock() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindPickBlock);
  }

  @Override
  public KeyBinding getKeyBindChat() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindChat);
  }

  @Override
  public KeyBinding getKeyBindPlayerList() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindPlayerList);
  }

  @Override
  public KeyBinding getKeyBindCommand() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindCommand);
  }

  @Override
  public KeyBinding getKeyBindScreenshot() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindScreenshot);
  }

  @Override
  public KeyBinding getKeyBindTogglePerspective() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindTogglePerspective);
  }

  @Override
  public KeyBinding getKeyBindSmoothCamera() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSmoothCamera);
  }

  @Override
  public KeyBinding getKeyBindFullscreen() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindFullscreen);
  }

  @Override
  public KeyBinding getKeyBindSpectatorOutlines() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSpectatorOutlines);
  }

  @Override
  public KeyBinding getKeyBindAdvancements() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindAdvancements);
  }

  @Override
  public List<KeyBinding> getKeyBindsHotbar() {
    List<KeyBinding> keyBindings;

    net.minecraft.client.settings.KeyBinding[] keyBindsHotbar = Minecraft.getInstance().gameSettings.keyBindsHotbar;
    keyBindings = Arrays.stream(keyBindsHotbar).map(this::fromMinecraftObject).collect(Collectors.toList());

    return keyBindings;
  }

  @Override
  public KeyBinding getKeyBindSaveToolbar() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindSaveToolbar);
  }

  @Override
  public KeyBinding getKeyBindLoadToolbar() {
    return this.fromMinecraftObject(Minecraft.getInstance().gameSettings.keyBindLoadToolbar);
  }

  @Override
  public List<KeyBinding> getKeyBindings() {
    List<KeyBinding> keyBindings;

    net.minecraft.client.settings.KeyBinding[] keyBinds = Minecraft.getInstance().gameSettings.keyBindings;
    keyBindings = Arrays.stream(keyBinds).map(this::fromMinecraftObject).collect(Collectors.toList());

    return keyBindings;
  }

  @Override
  public void setKeyBindingCode(KeyBinding bindingCode, String keyInputName) {
    Minecraft.getInstance().gameSettings.setKeyBindingCode(
            this.toMinecraftObject(bindingCode),
            InputMappings.getInputByName(keyInputName)
    );
  }

  @Override
  public double getMouseSensitivity() {
    return Minecraft.getInstance().gameSettings.mouseSensitivity;
  }

  @Override
  public void setMouseSensitivity(double mouseSensitivity) {
    Minecraft.getInstance().gameSettings.mouseSensitivity = mouseSensitivity;
    this.saveOptions();
  }


  @Override
  public double getMouseWheelSensitivity() {
    return Minecraft.getInstance().gameSettings.mouseWheelSensitivity;
  }

  @Override
  public void setMouseWheelSensitivity(double mouseWheelSensitivity) {
    Minecraft.getInstance().gameSettings.mouseWheelSensitivity = mouseWheelSensitivity;
    this.saveOptions();
  }

  @Override
  public boolean isRawMouseInput() {
    return Minecraft.getInstance().gameSettings.rawMouseInput;
  }

  @Override
  public void setRawMouseInput(boolean rawMouseInput) {
    Minecraft.getInstance().gameSettings.rawMouseInput = rawMouseInput;
    this.saveOptions();
  }

  @Override
  public boolean isInvertMouse() {
    return Minecraft.getInstance().gameSettings.invertMouse;
  }

  @Override
  public void setInvertMouse(boolean invertMouse) {
    Minecraft.getInstance().gameSettings.invertMouse = invertMouse;
    this.saveOptions();
  }

  @Override
  public boolean isDiscreteMouseScroll() {
    return Minecraft.getInstance().gameSettings.discreteMouseScroll;
  }

  @Override
  public void setDiscreteMouseScroll(boolean discreteMouseScroll) {
    Minecraft.getInstance().gameSettings.discreteMouseScroll = discreteMouseScroll;
    this.saveOptions();
  }

  @Override
  public boolean isTouchscreen() {
    return Minecraft.getInstance().gameSettings.touchscreen;
  }

  @Override
  public void setTouchscreen(boolean touchscreen) {
    Minecraft.getInstance().gameSettings.touchscreen = touchscreen;
    this.saveOptions();
  }

  @Override
  public boolean isToggleCrouch() {
    return Minecraft.getInstance().gameSettings.toggleCrouch;
  }

  @Override
  public void setToggleCrouch(boolean toggleCrouch) {
    Minecraft.getInstance().gameSettings.toggleCrouch = toggleCrouch;
    this.saveOptions();
  }

  @Override
  public boolean isToggleSprint() {
    return Minecraft.getInstance().gameSettings.toggleSprint;
  }

  @Override
  public void setToggleSprint(boolean toggleSprint) {
    Minecraft.getInstance().gameSettings.toggleSprint = toggleSprint;
    this.saveOptions();
  }

  @Override
  public boolean isAutoJump() {
    return Minecraft.getInstance().gameSettings.autoJump;
  }

  @Override
  public void setAutoJump(boolean autoJump) {
    Minecraft.getInstance().gameSettings.autoJump = autoJump;
    this.saveOptions();
  }


  @Override
  public int getRenderDistanceChunks() {
    return Minecraft.getInstance().gameSettings.renderDistanceChunks;
  }

  @Override
  public void setRenderDistanceChunks(int renderDistanceChunks) {
    Minecraft.getInstance().gameSettings.renderDistanceChunks = renderDistanceChunks;
    this.saveOptions();
  }

  @Override
  public float getEntityDistanceScaling() {
    return 0;
  }

  @Override
  public void setEntityDistanceScaling(float entityDistanceScaling) {
    // NO-OP
  }

  @Override
  public int getFramerateLimit() {
    return Minecraft.getInstance().gameSettings.framerateLimit;
  }

  @Override
  public void setFramerateLimit(int framerateLimit) {
    Minecraft.getInstance().gameSettings.framerateLimit = framerateLimit;
    this.saveOptions();
  }

  @Override
  public CloudOption getCloudOption() {
    switch (Minecraft.getInstance().gameSettings.cloudOption) {
      case OFF:
        return CloudOption.OFF;
      case FAST:
        return CloudOption.FAST;
      case FANCY:
        return CloudOption.FANCY;
      default:
        throw new IllegalStateException("Unexpected value: " + Minecraft.getInstance().gameSettings.cloudOption);
    }
  }

  @Override
  public void setCloudOption(CloudOption cloudOption) {
    switch (cloudOption) {
      case OFF:
        Minecraft.getInstance().gameSettings.cloudOption = net.minecraft.client.settings.CloudOption.OFF;
        this.saveOptions();
        break;
      case FAST:
        Minecraft.getInstance().gameSettings.cloudOption = net.minecraft.client.settings.CloudOption.FAST;
        this.saveOptions();
        break;
      case FANCY:
        Minecraft.getInstance().gameSettings.cloudOption = net.minecraft.client.settings.CloudOption.FANCY;
        this.saveOptions();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + cloudOption);
    }
  }

  @Override
  public GraphicsFanciness getGraphicsFanciness() {
    return Minecraft.getInstance().gameSettings.fancyGraphics ? GraphicsFanciness.FANCY : GraphicsFanciness.FAST;
  }

  @Override
  public void setGraphicsFanciness(GraphicsFanciness fancyGraphics) {
    switch (fancyGraphics) {
      case FAST:
        Minecraft.getInstance().gameSettings.fancyGraphics = false;
        this.saveOptions();
        break;
      case FANCY:
      case FABULOUS:
        Minecraft.getInstance().gameSettings.fancyGraphics = true;
        this.saveOptions();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + fancyGraphics);
    }
  }

  @Override
  public AmbientOcclusionStatus getAmbientOcclusionStatus() {
    switch (Minecraft.getInstance().gameSettings.ambientOcclusionStatus) {
      case OFF:
        return AmbientOcclusionStatus.OFF;
      case MIN:
        return AmbientOcclusionStatus.MIN;
      case MAX:
        return AmbientOcclusionStatus.MAX;
      default:
        throw new IllegalStateException("Unexpected value: " + Minecraft.getInstance().gameSettings.ambientOcclusionStatus);
    }
  }

  @Override
  public void setAmbientOcclusionStatus(AmbientOcclusionStatus ambientOcclusionStatus) {
    switch (ambientOcclusionStatus) {
      case OFF:
        Minecraft.getInstance().gameSettings.ambientOcclusionStatus = net.minecraft.client.settings.AmbientOcclusionStatus.OFF;
        this.saveOptions();
        break;
      case MIN:
        Minecraft.getInstance().gameSettings.ambientOcclusionStatus = net.minecraft.client.settings.AmbientOcclusionStatus.MIN;
        this.saveOptions();
        break;
      case MAX:
        Minecraft.getInstance().gameSettings.ambientOcclusionStatus = net.minecraft.client.settings.AmbientOcclusionStatus.MAX;
        this.saveOptions();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + ambientOcclusionStatus);
    }
  }

  @Override
  public int getMipmapLevels() {
    return Minecraft.getInstance().gameSettings.mipmapLevels;
  }

  @Override
  public void setMipmapLevels(int mipmapLevels) {
    Minecraft.getInstance().gameSettings.mipmapLevels = mipmapLevels;
    this.saveOptions();
  }

  @Override
  public AttackIndicatorStatus getAttackIndicator() {
    switch (Minecraft.getInstance().gameSettings.attackIndicator) {
      case OFF:
        return AttackIndicatorStatus.OFF;
      case CROSSHAIR:
        return AttackIndicatorStatus.CROSSHAIR;
      case HOTBAR:
        return AttackIndicatorStatus.HOTBAR;
      default:
        throw new IllegalStateException("Unexpected value: " + Minecraft.getInstance().gameSettings.attackIndicator);
    }
  }

  @Override
  public void setAttackIndicator(AttackIndicatorStatus attackIndicator) {
    switch (attackIndicator) {
      case OFF:
        Minecraft.getInstance().gameSettings.attackIndicator = net.minecraft.client.settings.AttackIndicatorStatus.OFF;
        this.saveOptions();
        break;
      case CROSSHAIR:
        Minecraft.getInstance().gameSettings.attackIndicator = net.minecraft.client.settings.AttackIndicatorStatus.CROSSHAIR;
        this.saveOptions();
        break;
      case HOTBAR:
        Minecraft.getInstance().gameSettings.attackIndicator = net.minecraft.client.settings.AttackIndicatorStatus.HOTBAR;
        this.saveOptions();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + attackIndicator);
    }
  }

  @Override
  public int getBiomeBlendRadius() {
    return Minecraft.getInstance().gameSettings.biomeBlendRadius;
  }

  @Override
  public void setBiomeBlendRadius(int biomeBlendRadius) {
    Minecraft.getInstance().gameSettings.biomeBlendRadius = biomeBlendRadius;
    this.saveOptions();
  }

  @Override
  public boolean isVsync() {
    return Minecraft.getInstance().gameSettings.vsync;
  }

  @Override
  public void setVsync(boolean vsync) {
    Minecraft.getInstance().gameSettings.vsync = vsync;
    this.saveOptions();
  }

  @Override
  public boolean isFullscreen() {
    return Minecraft.getInstance().gameSettings.fullscreen;
  }

  @Override
  public void setFullscreen(boolean fullscreen) {
    Minecraft.getInstance().gameSettings.fullscreen = fullscreen;
    this.saveOptions();
  }

  @Override
  public boolean isViewBobbing() {
    return Minecraft.getInstance().gameSettings.viewBobbing;
  }

  @Override
  public void setViewBobbing(boolean viewBobbing) {
    Minecraft.getInstance().gameSettings.viewBobbing = viewBobbing;
    this.saveOptions();
  }

  @Override
  public double getFov() {
    return Minecraft.getInstance().gameSettings.fov;
  }

  @Override
  public void setFov(double fov) {
    Minecraft.getInstance().gameSettings.fov = fov;
    this.saveOptions();
  }

  @Override
  public float getScreenEffectScale() {
    return 0;
  }

  @Override
  public void setScreenEffectScale(float screenEffectScale) {
    // NO-OP
  }

  @Override
  public float getFovEffectScale() {
    return 0;
  }

  @Override
  public void setFovEffectScale(float fovEffectScale) {
    // NO-OP
  }

  @Override
  public double getGamma() {
    return Minecraft.getInstance().gameSettings.gamma;
  }

  @Override
  public void setGamma(double gamma) {
    Minecraft.getInstance().gameSettings.gamma = gamma;
    this.saveOptions();
  }

  @Override
  public int getGuiScale() {
    return Minecraft.getInstance().gameSettings.guiScale;
  }

  @Override
  public void setGuiScale(int guiScale) {
    Minecraft.getInstance().gameSettings.guiScale = guiScale;
    this.saveOptions();
  }

  @Override
  public ParticleStatus getParticles() {
    switch (Minecraft.getInstance().gameSettings.particles) {
      case ALL:
        return ParticleStatus.ALL;
      case DECREASED:
        return ParticleStatus.DECREASED;
      case MINIMAL:
        return ParticleStatus.MINIMAL;
      default:
        throw new IllegalStateException("Unexpected value: " + Minecraft.getInstance().gameSettings.particles);
    }
  }

  @Override
  public void setParticles(ParticleStatus particles) {
    switch (particles) {
      case ALL:
        Minecraft.getInstance().gameSettings.particles = net.minecraft.client.settings.ParticleStatus.ALL;
        this.saveOptions();
        break;
      case DECREASED:
        Minecraft.getInstance().gameSettings.particles = net.minecraft.client.settings.ParticleStatus.DECREASED;
        this.saveOptions();
        break;
      case MINIMAL:
        Minecraft.getInstance().gameSettings.particles = net.minecraft.client.settings.ParticleStatus.MINIMAL;
        this.saveOptions();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + particles);
    }
  }

  @Override
  public boolean isEntityShadows() {
    return Minecraft.getInstance().gameSettings.entityShadows;
  }

  @Override
  public void setEntityShadows(boolean entityShadows) {
    Minecraft.getInstance().gameSettings.entityShadows = entityShadows;
    this.saveOptions();
  }

  @Override
  public String getFullscreenResolution() {
    return Minecraft.getInstance().gameSettings.fullscreenResolution;
  }

  @Override
  public void setFullscreenResolution(String fullscreenResolution) {
    Minecraft.getInstance().gameSettings.fullscreenResolution = fullscreenResolution;
    this.saveOptions();
  }

  @Override
  public List<String> getResourcePacks() {
    return Minecraft.getInstance().gameSettings.resourcePacks;
  }

  @Override
  public void setResourcePacks(List<String> resourcePacks) {
    Minecraft.getInstance().gameSettings.resourcePacks = resourcePacks;
    this.saveOptions();
  }

  @Override
  public List<String> getIncompatibleResourcePacks() {
    return Minecraft.getInstance().gameSettings.incompatibleResourcePacks;
  }

  @Override
  public void setIncompatibleResourcePacks(List<String> incompatibleResourcePacks) {
    Minecraft.getInstance().gameSettings.incompatibleResourcePacks = incompatibleResourcePacks;
    this.saveOptions();
  }


  @Override
  public boolean isShowSubtitles() {
    return Minecraft.getInstance().gameSettings.showSubtitles;
  }

  @Override
  public void setShowSubtitles(boolean showSubtitles) {
    Minecraft.getInstance().gameSettings.showSubtitles = showSubtitles;
    this.saveOptions();
  }

  @Override
  public float getMasterSoundVolume() {
    return Minecraft.getInstance().gameSettings.getSoundLevel(net.minecraft.util.SoundCategory.MASTER);
  }

  @Override
  public void setMasterSoundVolume(float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(net.minecraft.util.SoundCategory.MASTER, volume);
    this.saveOptions();
  }

  @Override
  public float getMusicSoundVolume() {
    return Minecraft.getInstance().gameSettings.getSoundLevel(net.minecraft.util.SoundCategory.MUSIC);
  }

  @Override
  public void setMusicSoundVolume(float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(net.minecraft.util.SoundCategory.MUSIC, volume);
    this.saveOptions();
  }

  @Override
  public float getRecordSoundVolume() {
    return Minecraft.getInstance().gameSettings.getSoundLevel(net.minecraft.util.SoundCategory.RECORDS);
  }

  @Override
  public void setRecordSoundVolume(float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(net.minecraft.util.SoundCategory.RECORDS, volume);
    this.saveOptions();
  }

  @Override
  public float getWeatherSoundVolume() {
    return Minecraft.getInstance().gameSettings.getSoundLevel(net.minecraft.util.SoundCategory.WEATHER);
  }

  @Override
  public void setWeatherSoundVolume(float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(net.minecraft.util.SoundCategory.WEATHER, volume);
    this.saveOptions();
  }

  @Override
  public float getBlockSoundVolume() {
    return Minecraft.getInstance().gameSettings.getSoundLevel(net.minecraft.util.SoundCategory.BLOCKS);
  }

  @Override
  public void setBlockSoundVolume(float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(net.minecraft.util.SoundCategory.BLOCKS, volume);
    this.saveOptions();
  }

  @Override
  public float getHostileSoundVolume() {
    return Minecraft.getInstance().gameSettings.getSoundLevel(net.minecraft.util.SoundCategory.HOSTILE);
  }

  @Override
  public void setHostileSoundVolume(float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(net.minecraft.util.SoundCategory.HOSTILE, volume);
    this.saveOptions();
  }

  @Override
  public float getNeutralSoundVolume() {
    return Minecraft.getInstance().gameSettings.getSoundLevel(net.minecraft.util.SoundCategory.NEUTRAL);
  }

  @Override
  public void setNeutralSoundVolume(float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(net.minecraft.util.SoundCategory.NEUTRAL, volume);
    this.saveOptions();
  }

  @Override
  public float getPlayerSoundVolume() {
    return Minecraft.getInstance().gameSettings.getSoundLevel(net.minecraft.util.SoundCategory.PLAYERS);
  }

  @Override
  public void setPlayerSoundVolume(float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(net.minecraft.util.SoundCategory.PLAYERS, volume);
    this.saveOptions();
  }

  @Override
  public float getAmbientSoundVolume() {
    return Minecraft.getInstance().gameSettings.getSoundLevel(net.minecraft.util.SoundCategory.AMBIENT);
  }

  @Override
  public void setAmbientSoundVolume(float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(net.minecraft.util.SoundCategory.AMBIENT, volume);
    this.saveOptions();
  }

  @Override
  public float getVoiceSoundVolume() {
    return Minecraft.getInstance().gameSettings.getSoundLevel(net.minecraft.util.SoundCategory.VOICE);
  }

  @Override
  public void setVoiceSoundVolume(float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(net.minecraft.util.SoundCategory.VOICE, volume);
    this.saveOptions();
  }

  @Override
  public Set<PlayerClothing> getSetModelParts() {
    return Minecraft.getInstance().gameSettings
            .getModelParts()
            .stream()
            .map(this::fromMinecraftObject)
            .collect(Collectors.toSet());
  }

  @Override
  public void setModelClothingEnabled(PlayerClothing clothing, boolean enable) {
    Minecraft.getInstance().gameSettings.setModelPartEnabled(this.toMinecraftObject(clothing), enable);
    this.saveOptions();
  }

  @Override
  public void switchModelClothingEnabled(PlayerClothing clothing) {
    Minecraft.getInstance().gameSettings.switchModelPartEnabled(this.toMinecraftObject(clothing));
    this.saveOptions();
  }

  @Override
  public Hand.Side getMainHand() {
    switch (Minecraft.getInstance().gameSettings.mainHand) {
      case LEFT:
        return Hand.Side.LEFT;
      case RIGHT:
        return Hand.Side.RIGHT;
      default:
        throw new IllegalStateException("Unexpected value: " + Minecraft.getInstance().gameSettings.mainHand);
    }
  }

  @Override
  public void setMainHand(Hand.Side mainHand) {
    switch (mainHand) {
      case LEFT:
        Minecraft.getInstance().gameSettings.mainHand = HandSide.LEFT;
        this.saveOptions();
        break;
      case RIGHT:
        Minecraft.getInstance().gameSettings.mainHand = HandSide.RIGHT;
        this.saveOptions();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + mainHand);
    }
  }

  private net.minecraft.client.settings.KeyBinding toMinecraftObject(KeyBinding binding) {
    return new net.minecraft.client.settings.KeyBinding(binding.getKeyDescription(), binding.getKeyCode(), binding.getKeyCategory());
  }

  private KeyBinding fromMinecraftObject(net.minecraft.client.settings.KeyBinding keyBinding) {
    return this.keyBindingFactory.create(
            keyBinding.getKeyDescription(),
            keyBinding.getDefault().getKeyCode(),
            keyBinding.getKeyCategory()
    );
  }

  private PlayerClothing fromMinecraftObject(PlayerModelPart playerModelPart) {
    switch (playerModelPart) {
      case CAPE:
        return PlayerClothing.CLOAK;
      case JACKET:
        return PlayerClothing.JACKET;
      case LEFT_SLEEVE:
        return PlayerClothing.LEFT_SLEEVE;
      case RIGHT_SLEEVE:
        return PlayerClothing.RIGHT_SLEEVE;
      case LEFT_PANTS_LEG:
        return PlayerClothing.LEFT_PANTS_LEG;
      case RIGHT_PANTS_LEG:
        return PlayerClothing.RIGHT_PANTS_LEG;
      case HAT:
        return PlayerClothing.HAT;
      default:
        throw new IllegalStateException("Unexpected value: " + playerModelPart);
    }
  }

  private PlayerModelPart toMinecraftObject(PlayerClothing clothing) {
    switch (clothing) {
      case CLOAK:
        return PlayerModelPart.CAPE;
      case JACKET:
        return PlayerModelPart.JACKET;
      case LEFT_SLEEVE:
        return PlayerModelPart.LEFT_SLEEVE;
      case RIGHT_SLEEVE:
        return PlayerModelPart.RIGHT_SLEEVE;
      case LEFT_PANTS_LEG:
        return PlayerModelPart.LEFT_PANTS_LEG;
      case RIGHT_PANTS_LEG:
        return PlayerModelPart.RIGHT_PANTS_LEG;
      case HAT:
        return PlayerModelPart.HAT;
      default:
        throw new IllegalStateException("Unexpected value: " + clothing);
    }
  }

  private void saveOptions() {
    Minecraft.getInstance().gameSettings.saveOptions();
  }

}
