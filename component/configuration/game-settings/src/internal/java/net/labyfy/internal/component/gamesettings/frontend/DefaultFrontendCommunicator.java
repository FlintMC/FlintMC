package net.labyfy.internal.component.gamesettings.frontend;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.gamesettings.frontend.FrontendCommunicator;
import net.labyfy.component.gamesettings.frontend.FrontendType;
import net.labyfy.component.gamesettings.settings.*;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.world.difficult.Difficulty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Singleton
@Implement(FrontendCommunicator.class)
public class DefaultFrontendCommunicator implements FrontendCommunicator {

  private final Multimap<String, FrontendType> configurations;
  private final JsonObject configurationObject;
  private final Map<String, String> launchArguments;

  @Inject
  private DefaultFrontendCommunicator(@Named("launchArguments") Map launchArguments, FrontendType.Factory frontedTypeFactory) {
    this.launchArguments = launchArguments;
    this.configurationObject = new JsonObject();
    this.configurations = HashMultimap.create();

    // Registers default options
    this.configurations.put("none", frontedTypeFactory.create("fov", Double.TYPE).setRange(-1D, 1D));
    this.configurations.put("none", frontedTypeFactory.create("realmsNotifications", Boolean.TYPE));
    this.configurations.put("none", frontedTypeFactory.create("difficulty", Difficulty.class));

    this.configurations.put("accessibility", frontedTypeFactory.create("toggleSprint", Boolean.TYPE));
    this.configurations.put("accessibility", frontedTypeFactory.create("toggleCrouch", Boolean.TYPE));
    this.configurations.put("accessibility", frontedTypeFactory.create("autoJump", Boolean.TYPE));

    this.configurations.put("chat", frontedTypeFactory.create("chatLinksPrompt", Boolean.TYPE));
    this.configurations.put("chat", frontedTypeFactory.create("backgroundForChatOnly", Boolean.TYPE));
    this.configurations.put("chat", frontedTypeFactory.create("chatColors", Boolean.TYPE));
    this.configurations.put("chat", frontedTypeFactory.create("autoSuggestions", Boolean.TYPE));
    this.configurations.put("chat", frontedTypeFactory.create("chatLinks", Boolean.TYPE));
    this.configurations.put("chat", frontedTypeFactory.create("reducedDebugInfo", Boolean.TYPE));
    this.configurations.put("chat", frontedTypeFactory.create("chatOpacity", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("chat", frontedTypeFactory.create("chatHeightFocused", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("chat", frontedTypeFactory.create("chatHeightUnfocused", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("chat", frontedTypeFactory.create("chatWidth", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("chat", frontedTypeFactory.create("chatScale", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("chat", frontedTypeFactory.create("textBackgroundOpacity", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("chat", frontedTypeFactory.create("chatVisibility", ChatVisibility.class));
    this.configurations.put("chat", frontedTypeFactory.create("narrator", NarratorStatus.class));
    this.configurations.put("chat", frontedTypeFactory.create("chatDelay", Double.TYPE).setRange(0D, 6D));
    this.configurations.put("chat", frontedTypeFactory.create("chatLineSpacing", Double.TYPE).setRange(0D, 6D));
    this.configurations.put("chat", frontedTypeFactory.create("chatScale", Double.TYPE).setRange(0D, 1D));

    this.configurations.put("graphics", frontedTypeFactory.create("attackIndicator", AttackIndicatorStatus.class));
    this.configurations.put("graphics", frontedTypeFactory.create("ao", AmbientOcclusionStatus.class));
    this.configurations.put("graphics", frontedTypeFactory.create("bobView", Boolean.TYPE));
    this.configurations.put("graphics", frontedTypeFactory.create("overrideHeight", Integer.TYPE));
    this.configurations.put("graphics", frontedTypeFactory.create("overrideWidth", Integer.TYPE));
    this.configurations.put("graphics", frontedTypeFactory.create("heldItemTooltips", Boolean.TYPE));
    this.configurations.put("graphics", frontedTypeFactory.create("gamma", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("graphics", frontedTypeFactory.create("biomeBlendRadius", Integer.TYPE).setRange(0D, 7D));
    this.configurations.put("graphics", frontedTypeFactory.create("forceUnicodeFont", AttackIndicatorStatus.class));
    this.configurations.put("graphics", frontedTypeFactory.create("guiScale", Integer.TYPE).setRange(0, 2));
    this.configurations.put("graphics", frontedTypeFactory.create("renderClouds", CloudOption.class));
    this.configurations.put("graphics", frontedTypeFactory.create("maxFps", Integer.TYPE).setRange(10, 260));
    this.configurations.put("graphics", frontedTypeFactory.create("glDebugVerbosity", Integer.TYPE));
    this.configurations.put("graphics", frontedTypeFactory.create("skipMultiplayerWarning", Boolean.TYPE));
    this.configurations.put("graphics", frontedTypeFactory.create("renderDistance", Integer.TYPE).setRange(2, 32));
    this.configurations.put("graphics", frontedTypeFactory.create("fullscreen", Boolean.TYPE));
    this.configurations.put("graphics", frontedTypeFactory.create("fullscreenResolution", String.class));
    this.configurations.put("graphics", frontedTypeFactory.create("entityShadows", Boolean.TYPE));
    this.configurations.put("graphics", frontedTypeFactory.create("advancedItemTooltips", Boolean.TYPE));
    this.configurations.put("graphics", frontedTypeFactory.create("particles", ParticleStatus.class));
    this.configurations.put("graphics", frontedTypeFactory.create("enableVsync", Boolean.TYPE));
    this.configurations.put("graphics", frontedTypeFactory.create("fancyGraphics", GraphicsFanciness.class));
    this.configurations.put("graphics", frontedTypeFactory.create("mipmapLevels", Integer.class).setRange(0, 4));
    this.configurations.put("graphics", frontedTypeFactory.create("entityDistanceScaling", Double.TYPE).setRange(0.5D, 5D));
    this.configurations.put("graphics", frontedTypeFactory.create("screenEffectScale", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("graphics", frontedTypeFactory.create("fovEffectScale", Double.TYPE).setRange(0D, 1D));

    this.configurations.put("mouse", frontedTypeFactory.create("touchscreen", Boolean.TYPE));
    this.configurations.put("mouse", frontedTypeFactory.create("discreteMouseScroll", Boolean.TYPE));
    this.configurations.put("mouse", frontedTypeFactory.create("mouseWheelSensitivity", Boolean.TYPE));
    this.configurations.put("mouse", frontedTypeFactory.create("rawMouseInput", Boolean.TYPE));
    this.configurations.put("mouse", frontedTypeFactory.create("mouseSensitivity", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("mouse", frontedTypeFactory.create("mouseWheelSensitivity", Double.TYPE).setRange(0.01D, 10D));
    this.configurations.put("mouse", frontedTypeFactory.create("invertYMouse", Boolean.TYPE));

    this.configurations.put("keys", frontedTypeFactory.create("pickItem", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("playerlist", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("advancements", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("sprint", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("forward", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("drop", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("back", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("attack", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("saveToolbarActivator", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("loadToolbarActivator", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("swapHands", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("fullscreen", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("chat", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("togglePerspective", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("screenshot", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("command", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("left", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("spectatorOutlines", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("sneak", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("jump", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("right", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("smoothCamera", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("use", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("hotbar.1", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("hotbar.2", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("hotbar.3", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("hotbar.4", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("hotbar.5", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("hotbar.6", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("hotbar.7", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("hotbar.8", String.class));
    this.configurations.put("keys", frontedTypeFactory.create("hotbar.9", String.class));

    this.configurations.put("skinModel", frontedTypeFactory.create("cape", Boolean.TYPE));
    this.configurations.put("skinModel", frontedTypeFactory.create("jacket", Boolean.TYPE));
    this.configurations.put("skinModel", frontedTypeFactory.create("leftSleeve", Boolean.TYPE));
    this.configurations.put("skinModel", frontedTypeFactory.create("rightSleeve", Boolean.TYPE));
    this.configurations.put("skinModel", frontedTypeFactory.create("rightPantsLeg", Boolean.TYPE));
    this.configurations.put("skinModel", frontedTypeFactory.create("leftPantsLeg", Boolean.TYPE));
    this.configurations.put("skinModel", frontedTypeFactory.create("mainHand", Hand.Side.class));

    this.configurations.put("sounds", frontedTypeFactory.create("master", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("sounds", frontedTypeFactory.create("voice", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("sounds", frontedTypeFactory.create("record", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("sounds", frontedTypeFactory.create("music", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("sounds", frontedTypeFactory.create("weather", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("sounds", frontedTypeFactory.create("block", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("sounds", frontedTypeFactory.create("player", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("sounds", frontedTypeFactory.create("neutral", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("sounds", frontedTypeFactory.create("ambient", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("sounds", frontedTypeFactory.create("hostile", Double.TYPE).setRange(0D, 1D));
    this.configurations.put("sounds", frontedTypeFactory.create("showSubtitles", Boolean.TYPE));

    this.configurations.put("resources", frontedTypeFactory.create("resourcePacks", List.class));
    this.configurations.put("resources", frontedTypeFactory.create("incompatibleResourcePacks", List.class));

    this.configurations.put("other", frontedTypeFactory.create("useNativeTransport", Boolean.TYPE));
    this.configurations.put("other", frontedTypeFactory.create("hideServerAddress", Boolean.TYPE));
    this.configurations.put("other", frontedTypeFactory.create("pauseOnLostFocus", Boolean.TYPE));
    this.configurations.put("other", frontedTypeFactory.create("lastServer", String.class));
    this.configurations.put("other", frontedTypeFactory.create("lang", String.class));
    this.configurations.put("other", frontedTypeFactory.create("tutorialStep", TutorialSteps.class));
    this.configurations.put("other", frontedTypeFactory.create("snooperEnabled", Boolean.TYPE));
    this.configurations.put("other", frontedTypeFactory.create("syncChunkWrites", Boolean.TYPE));
  }

  @Override
  public JsonObject parseOption(Map<String, String> configurations) {
    Map<String, String> fixedConfiguration = new HashMap<>();

    for (Map.Entry<String, String> entry : configurations.entrySet()) {
      StringBuilder key = new StringBuilder(entry.getKey()
              .replace("key_key.", "")
              .replace("modelPart_", "")
              .replace("soundCategory_", ""));

      if (key.toString().contains("_")) {
        String[] split = key.toString().split("_");

        key = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
          if (i == 0) {
            key.append(split[i]);
            continue;
          }

          key.append(split[i].substring(0, 1).toUpperCase()).append(split[i].substring(1));
        }
      }

      fixedConfiguration.put(key.toString(), entry.getValue());
    }

    for (Map.Entry<String, FrontendType> entry : this.configurations.entries()) {
      JsonObject configEntry = this.configurationObject.getAsJsonObject(entry.getKey());

      if (configEntry == null) {
        configEntry = new JsonObject();
      }
      FrontendType type = entry.getValue();

      String configurationValue = fixedConfiguration.get(type.getConfigurationName());

      if (configurationValue == null) {
        if (type.getType().equals(String.class)) {
          configurationValue = "";
        } else if (type.getType().equals(Double.TYPE)) {
          configurationValue = "1.0";
        } else if (type.getType().equals(Integer.TYPE)) {
          configurationValue = "1";
        } else if (type.getType().equals(Boolean.TYPE)) {
          configurationValue = "false";
        }

      }

      if (!type.getType().isEnum()) {

        if (type.getType().equals(String.class)) {
          configEntry.addProperty(type.getConfigurationName(), configurationValue);
        } else if (type.getType().equals(Double.TYPE)) {

          if (type.getMinValue() != type.getMaxValue()) {
            JsonObject object = new JsonObject();
            object.addProperty("selected", Double.parseDouble(configurationValue));
            object.addProperty("min", type.getMinValue());
            object.addProperty("max", type.getMaxValue());
            configEntry.add(type.getConfigurationName(), object);
          } else {
            configEntry.addProperty(type.getConfigurationName(), Double.parseDouble(configurationValue));
          }

        } else if (type.getType().equals(Integer.TYPE)) {

          if (type.getMin() != type.getMax()) {
            JsonObject object = new JsonObject();
            object.addProperty("selected", Integer.parseInt(configurationValue));
            object.addProperty("min", type.getMin());
            object.addProperty("max", type.getMax());
            configEntry.add(type.getConfigurationName(), object);
          } else {
            configEntry.addProperty(type.getConfigurationName(), Integer.parseInt(configurationValue));
          }

        } else if (type.getType().equals(Boolean.TYPE)) {
          configEntry.addProperty(type.getConfigurationName(), Boolean.parseBoolean(configurationValue));
        } else if (type.getType().equals(List.class)) {
          String value = configurationValue;

          if (value.contains(",")) continue;
          if (!value.startsWith("[") || !value.endsWith("]")) continue;

          value = value.replace("[", "").replace("]", "");

          String[] split = value.split(",");

          JsonArray array = new JsonArray();

          for (String s : split) {
            array.add(s);
          }

          configEntry.add(type.getConfigurationName(), array);
        }
      } else {
        JsonObject enumObject = new JsonObject();
        String value = configurationValue;

        if (type.getConfigurationName().equalsIgnoreCase("tutorialStep") || type.getConfigurationName().equalsIgnoreCase("mainHand")) {
          value = value.toUpperCase();
        } else if (type.getConfigurationName().equalsIgnoreCase("renderClouds")) {

          if (value.equalsIgnoreCase("true")) {
            value = CloudOption.FANCY.name();
          } else if (value.equalsIgnoreCase("false")) {
            value = CloudOption.OFF.name();
          } else {
            value = CloudOption.FAST.name();
          }

        } else if (type.getConfigurationName().equalsIgnoreCase("fancyGraphics")) {
          if (this.getMinorVersion(this.launchArguments.get("--game-version")) < 16) {
            boolean flag = Boolean.getBoolean(value);

            value = flag ? GraphicsFanciness.FANCY.name() : GraphicsFanciness.FAST.name();
          } else {
            value = type.getType().getEnumConstants()[Integer.parseInt(value)].toString();
          }
        } else if (isNumber(value)) {
          value = type.getType().getEnumConstants()[Integer.parseInt(value)].toString();
        } else {
          value = value.toUpperCase();
        }

        enumObject.addProperty("selected", value);
        JsonArray array = new JsonArray();

        for (Object enumConstant : type.getType().getEnumConstants()) {
          array.add(enumConstant.toString());
        }
        enumObject.add("options", array);

        configEntry.add(type.getConfigurationName(), enumObject);
      }

      this.configurationObject.add(entry.getKey(), configEntry);
    }

    return configurationObject;
  }

  @Override
  public Map<String, String> parseJson(JsonObject object) {
    Map<String, String> configurations = new HashMap<>();


    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
      for (Map.Entry<String, JsonElement> elementEntry : entry.getValue().getAsJsonObject().entrySet()) {
        if (elementEntry.getValue().isJsonPrimitive()) {
          if (entry.getKey().equalsIgnoreCase("keys")) {
            configurations.put("key_key." + elementEntry.getKey(), elementEntry.getValue().toString());
          } else if (entry.getKey().equalsIgnoreCase("sounds")) {
            configurations.put("soundCategory_" + elementEntry.getKey(), elementEntry.getValue().toString());
          } else if (entry.getKey().equalsIgnoreCase("skinModel")) {
            configurations.put("modelPart_" + this.parseOptionName(elementEntry.getKey()), elementEntry.getValue().toString());
          } else {
            if (elementEntry.getKey().startsWith("discrete")) {
              configurations.put(this.parseOptionName(elementEntry.getKey()), elementEntry.getValue().toString());
              continue;
            }
            configurations.put(elementEntry.getKey(), elementEntry.getValue().toString());
          }
        } else {
          if (!elementEntry.getValue().isJsonArray()) {
            String selected = elementEntry.getValue().getAsJsonObject().get("selected").toString().replace("\"", "");
            if (elementEntry.getKey().equalsIgnoreCase("tutorialStep") || elementEntry.getKey().equalsIgnoreCase("mainHand")) {
              configurations.put(elementEntry.getKey(), selected.toLowerCase());
            } else if (elementEntry.getKey().equalsIgnoreCase("renderClouds")) {
              CloudOption cloudOption = CloudOption.valueOf(selected);

              switch (cloudOption) {
                case OFF:
                  configurations.put(elementEntry.getKey(), "false");
                  break;
                case FAST:
                  configurations.put(elementEntry.getKey(), "fast");
                  break;
                case FANCY:
                  configurations.put(elementEntry.getKey(), "true");
                  break;
              }
            } else {
              Class<?> cls = this.getType(entry.getKey(), elementEntry.getKey());

              if (!cls.isEnum()) {
                configurations.put(elementEntry.getKey(), selected);
              } else {
                configurations.put(elementEntry.getKey(), String.valueOf(this.getOrdinal(cls, selected)));
              }

            }

          }
        }
      }
    }

    return configurations;
  }

  private int getOrdinal(Class<?> cls, String value) {
    if (!cls.isEnum()) return -1;

    for (int i = 0; i < cls.getEnumConstants().length; i++) {
      String constant = cls.getEnumConstants()[i].toString();
      if (constant.equalsIgnoreCase(value)) {
        return i;
      }
    }
    return -1;
  }

  private Class<?> getType(String category, String configurationName) {
    for (FrontendType type : this.configurations.get(category)) {
      if (type.getConfigurationName().equalsIgnoreCase(configurationName)) return type.getType();
    }
    return null;
  }

  private String parseOptionName(String name) {
    StringBuilder builder = new StringBuilder();

    for (char c : name.toCharArray()) {
      if (Character.isUpperCase(c)) {
        builder.append("_").append(Character.toLowerCase(c));
        continue;
      }
      builder.append(c);
    }

    return builder.toString();
  }

  private boolean isNumber(String value) {
    try {
      Integer.parseInt(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
