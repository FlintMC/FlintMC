package net.labyfy.internal.component.gamesettings.frontend;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gamesettings.GameSettingsParser;
import net.labyfy.component.gamesettings.KeyBindMappings;
import net.labyfy.component.gamesettings.MinecraftConfiguration;
import net.labyfy.component.gamesettings.frontend.OptionsSerializer;
import net.labyfy.component.gamesettings.frontend.FrontendOption;
import net.labyfy.component.gamesettings.settings.*;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.version.VersionHelper;
import net.labyfy.component.world.difficult.Difficulty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Default implementation of {@link OptionsSerializer}.
 */
@Singleton
@Implement(OptionsSerializer.class)
public class DefaultOptionsSerializer implements OptionsSerializer {

  private static final Pattern NUMERIC_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

  private final Multimap<String, FrontendOption> configurations;
  private final VersionHelper versionHelper;
  private final JsonObject configurationObject;
  private final EnumConstantHelper enumConstantHelper;
  private final GameSettingsParser gameSettingsParser;
  private final MinecraftConfiguration minecraftConfiguration;

  @Inject
  private DefaultOptionsSerializer(
          EnumConstantHelper enumConstantHelper,
          FrontendOption.Factory frontedTypeFactory,
          VersionHelper versionHelper,
          GameSettingsParser gameSettingsParser,
          MinecraftConfiguration minecraftConfiguration) {
    this.versionHelper = versionHelper;
    this.gameSettingsParser = gameSettingsParser;
    this.minecraftConfiguration = minecraftConfiguration;
    this.configurationObject = new JsonObject();
    this.configurations = HashMultimap.create();
    this.enumConstantHelper = enumConstantHelper;

    // Registers default options
    this.setupDefaultOption(frontedTypeFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonObject serialize(Map<String, String> configurations) {
    Map<String, String> fixedConfiguration = this.prettyConfiguration(configurations);
    for (Map.Entry<String, FrontendOption> entry : this.configurations.entries()) {
      JsonObject configEntry = this.configurationObject.getAsJsonObject(entry.getKey());

      if (configEntry == null) {
        configEntry = new JsonObject();
      }
      FrontendOption type = entry.getValue();

      String configurationValue = fixedConfiguration.get(type.getConfigurationName());

      if (configurationValue == null) {
        configurationValue = type.getDefaultValue();
      }

      // Checks if the class not an enumeration
      if (!type.getType().isEnum()) {
        // Setups the configurations for all non enumerations.
        if (type.getType().equals(String.class)) {
          configEntry.addProperty(type.getConfigurationName(), configurationValue);
        } else if (type.getType().equals(Boolean.TYPE)) {
          configEntry.addProperty(type.getConfigurationName(), Boolean.parseBoolean(configurationValue));
        } else if (type.getType().equals(List.class)) {
          this.createListOption(configEntry, type, configurationValue);
        } else if (isNumeric(configurationValue)) {
          this.createNumberOption(configEntry, type, configurationValue);
        }
      } else {
        // Setups an enumeration option
        this.createEnumOption(configEntry, type, configurationValue);
      }

      this.configurationObject.add(entry.getKey(), configEntry);
    }

    return configurationObject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> deserialize(JsonObject object) {
    Map<String, String> configurations = new HashMap<>();


    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
      for (Map.Entry<String, JsonElement> elementEntry : entry.getValue().getAsJsonObject().entrySet()) {
        if (elementEntry.getValue().isJsonPrimitive()) {
          String value = elementEntry.getValue().toString().replace("\"", "");
          if (entry.getKey().equalsIgnoreCase("keys")) {

            // When the minor version is under 13 replaces
            // the configuration names to the scan codes
            if (this.versionHelper.isUnder13()) {
              value = String.valueOf(KeyBindMappings.getScanCode(value));
            }

            configurations.put("key_key." + elementEntry.getKey(), value);
          } else if (entry.getKey().equalsIgnoreCase("sounds")) {
            configurations.put("soundCategory_" + elementEntry.getKey(), value);
          } else if (entry.getKey().equalsIgnoreCase("skinModel")) {
            configurations.put("modelPart_" + this.convertToSnakeCase(elementEntry.getKey()), value);
          } else {
            if (elementEntry.getKey().startsWith("discrete")) {
              configurations.put(this.convertToSnakeCase(elementEntry.getKey()), value);
              continue;
            }
            configurations.put(elementEntry.getKey(), value);
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

              if (cls == null) continue;

              if (!cls.isEnum()) {
                configurations.put(elementEntry.getKey(), selected);
              } else {
                configurations.put(elementEntry.getKey(), String.valueOf(this.enumConstantHelper.getOrdinal(cls, selected)));
              }

            }

          }
        }
      }
    }

    return configurations;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonObject getConfigurationObject() {
    return this.configurationObject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateConfiguration(JsonObject object) {
    this.gameSettingsParser.saveOptions(this.gameSettingsParser.getOptionsFile(), this.deserialize(object));
    this.minecraftConfiguration.saveAndReloadOptions();
  }

  /**
   * Creates an enumeration option for the `JSON` configuration. An example of what the enumeration option looks like:
   * <pre>
   *     {@code
   * "selected": "FOO",
   * "options": ["FOO", "BAR"]
   *     }
   * </pre>
   *
   * @param configEntry An entry of the configuration.
   * @param type        The type of the option.
   * @param value       The current value.
   */
  private void createEnumOption(JsonObject configEntry, FrontendOption type, String value) {
    if (type.getConfigurationName().equalsIgnoreCase("renderClouds")) {
      if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
        value = Boolean.parseBoolean(value) ? CloudOption.FANCY.name() : CloudOption.OFF.name();
      } else {
        value = CloudOption.FAST.name();
      }
    } else if (type.getConfigurationName().equalsIgnoreCase("fancyGraphics")) {
      if (this.versionHelper.isUnder16()) {
        value = Boolean.getBoolean(value) ? GraphicsFanciness.FANCY.name() : GraphicsFanciness.FAST.name();
      } else {
        value = type.getType().getEnumConstants()[Integer.parseInt(value)].toString();
      }
    } else if (isNumeric(value)) {
      value = this.enumConstantHelper.getConstantByOrdinal(type.getType(), Integer.parseInt(value));
    } else {
      value = value.toUpperCase();
    }

    // Adds the enum to the configuration entry.
    configEntry.add(
            type.getConfigurationName(),
            this.createSelectEntry(
                    value,
                    this.enumConstantHelper.getConstants(type.getType()).values().toArray()
            )
    );

  }

  /**
   * Creates an number option for the `JSON` configuration. An example of what the number options look like:
   * Integer:
   * <pre>
   * {@code
   * "selected" 2,
   * "min": 1,
   * "max": 53
   * }
   * </pre>
   * Double:
   * <pre>
   * {@code
   * "selected" 2.0,
   * "min": 0.035,
   * "max": 3.364
   * }
   * </pre>
   *
   * @param configEntry An entry of the configuration.
   * @param type        The type of the option.
   * @param value       The current value.
   */
  private void createNumberOption(JsonObject configEntry, FrontendOption type, String value) {
    if (type.getType().equals(Integer.TYPE)) {
      if (type.getMin() != type.getMax()) {
        configEntry.add(
                type.getConfigurationName(),
                this.createSelectEntry(
                        Integer.parseInt(value),
                        type.getMin(),
                        type.getMax()
                )
        );
      } else {
        configEntry.addProperty(type.getConfigurationName(), Integer.parseInt(value));
      }
    } else if (type.getType().equals(Double.TYPE)) {
      if (type.getMinValue() != type.getMaxValue()) {
        configEntry.add(
                type.getConfigurationName(),
                this.createSelectEntry(
                        Double.parseDouble(value),
                        type.getMinValue(),
                        type.getMaxValue()
                )
        );
      } else {
        configEntry.addProperty(type.getConfigurationName(), Double.parseDouble(value));
      }
    }
  }

  /**
   * Creates a list option for the `JSON` configuration. An example of how the list options look like:
   * <pre>
   * {@code
   * "fooBarList": ["foo", "bar"]
   * }
   * </pre>
   *
   * @param configEntry An entry of the configuration.
   * @param type        The type of the option.
   * @param value       The current value.
   */
  private void createListOption(JsonObject configEntry, FrontendOption type, String value) {
    if (!value.contains(",") && !(value.startsWith("[") && value.endsWith("]"))) return;

    value = value.replace("[", "").replace("]", "");

    String[] split = value.split(",");

    JsonArray array = new JsonArray();

    for (String s : split) {
      array.add(s.replace("\"", ""));
    }

    configEntry.add(type.getConfigurationName(), array);
  }

  /**
   * Creates a select entry.
   *
   * @param selected The current selected value.
   * @param array    An array with all available options.
   * @return A select entry for the configuration.
   * @see DefaultOptionsSerializer#createEnumOption(JsonObject, FrontendOption, String)
   * @see DefaultOptionsSerializer#createNumberOption(JsonObject, FrontendOption, String)
   */
  private JsonObject createSelectEntry(Object selected, Object... array) {
    JsonObject object = new JsonObject();
    if (selected instanceof String) {
      object.addProperty("selected", (String) selected);
      JsonArray jsonArray = new JsonArray();
      for (Object o : array) {
        jsonArray.add((String) o);
      }
      object.add("options", jsonArray);
    } else if (selected instanceof Number) {
      object.addProperty("selected", (Number) selected);
      object.addProperty("min", (Number) array[0]);
      object.addProperty("max", (Number) array[1]);
    } else if (selected instanceof Character) {
      object.addProperty("selected", (Character) selected);
      JsonArray jsonArray = new JsonArray();
      for (Object o : array) {
        jsonArray.add((Character) o);
      }
      object.add("options", jsonArray);

    }
    return object;
  }

  /**
   * Retrieves the class by the given category and configuration name.
   *
   * @param category          The category name.
   * @param configurationName The name of the configuration.
   * @return A class identified by the category and configuration name or {@code null}.
   */
  private Class<?> getType(String category, String configurationName) {
    for (FrontendOption type : this.configurations.get(category)) {
      if (type.getConfigurationName().equalsIgnoreCase(configurationName)) {
        return type.getType();
      }
    }
    return null;
  }

  /**
   * Converts the given name to snake case.
   *
   * @param name The name to be convert
   * @return The snake cased name.
   */
  private String convertToSnakeCase(String name) {
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

  /**
   * Whether the given value is a numeric.
   *
   * @param value The value to be checked
   * @return {@code true} if the given value a numeric, otherwise {@code false}.
   */
  private boolean isNumeric(String value) {
    if (value == null || value.isEmpty()) {
      return false;
    }

    return NUMERIC_PATTERN.matcher(value).matches();
  }

  /**
   * Removes the ugly underscores and makes any options to lower camel case.
   *
   * @param configurations The configuration that is made pretty.
   * @return A key-value system with pretty keys.
   */
  private Map<String, String> prettyConfiguration(Map<String, String> configurations) {
    Map<String, String> prettyConfiguration = new HashMap<>();

    for (Map.Entry<String, String> entry : configurations.entrySet()) {
      // When the minor version is under 13 parses the
      // scan codes to the configuration names
      if (this.versionHelper.isUnder13()) {

        try {
          int key = Integer.parseInt(entry.getValue());
          String name = KeyBindMappings.getConfigurationName(key);
          configurations.put(entry.getKey(), name);
        } catch (NumberFormatException ignored) {

        }

      }

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

      prettyConfiguration.put(key.toString(), entry.getValue());
    }
    return prettyConfiguration;
  }

  /**
   * Setups the default options.
   *
   * @param frontendOptionFactory The factory to create {@link FrontendOption}'s.
   */
  private void setupDefaultOption(FrontendOption.Factory frontendOptionFactory) {
    this.configurations.put("none", frontendOptionFactory.create("fov", Double.TYPE, "0.0").setRange(-1D, 1D));
    this.configurations.put("none", frontendOptionFactory.create("realmsNotifications", Boolean.TYPE, "true"));
    this.configurations.put("none", frontendOptionFactory.create("difficulty", Difficulty.class, "2"));

    this.configurations.put("accessibility", frontendOptionFactory.create("toggleSprint", Boolean.TYPE, "false"));
    this.configurations.put("accessibility", frontendOptionFactory.create("toggleCrouch", Boolean.TYPE, "false"));
    this.configurations.put("accessibility", frontendOptionFactory.create("autoJump", Boolean.TYPE, "true"));

    this.configurations.put("chat", frontendOptionFactory.create("chatLinksPrompt", Boolean.TYPE, "true"));
    this.configurations.put("chat", frontendOptionFactory.create("backgroundForChatOnly", Boolean.TYPE, "true"));
    this.configurations.put("chat", frontendOptionFactory.create("chatColors", Boolean.TYPE, "true"));
    this.configurations.put("chat", frontendOptionFactory.create("autoSuggestions", Boolean.TYPE, "true"));
    this.configurations.put("chat", frontendOptionFactory.create("chatLinks", Boolean.TYPE, "true"));
    this.configurations.put("chat", frontendOptionFactory.create("reducedDebugInfo", Boolean.TYPE, "false"));
    this.configurations.put("chat", frontendOptionFactory.create("chatOpacity", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("chat", frontendOptionFactory.create("chatHeightFocused", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("chat", frontendOptionFactory.create("chatHeightUnfocused", Double.TYPE, "0.44366195797920227").setRange(0D, 1D));
    this.configurations.put("chat", frontendOptionFactory.create("chatWidth", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("chat", frontendOptionFactory.create("chatScale", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("chat", frontendOptionFactory.create("textBackgroundOpacity", Double.TYPE, "0.5").setRange(0D, 1D));
    this.configurations.put("chat", frontendOptionFactory.create("chatVisibility", ChatVisibility.class, "0"));
    this.configurations.put("chat", frontendOptionFactory.create("narrator", NarratorStatus.class, "0"));
    // 1.16
    this.configurations.put("chat", frontendOptionFactory.create("chatDelay", Double.TYPE, "0.0").setRange(0D, 6D));
    this.configurations.put("chat", frontendOptionFactory.create("chatLineSpacing", Double.TYPE, "0.0").setRange(0D, 6D));

    this.configurations.put("graphics", frontendOptionFactory.create("attackIndicator", AttackIndicatorStatus.class, "1"));
    this.configurations.put("graphics", frontendOptionFactory.create("ao", AmbientOcclusionStatus.class, "2"));
    this.configurations.put("graphics", frontendOptionFactory.create("bobView", Boolean.TYPE, "true"));
    this.configurations.put("graphics", frontendOptionFactory.create("overrideHeight", Integer.TYPE, "0"));
    this.configurations.put("graphics", frontendOptionFactory.create("overrideWidth", Integer.TYPE, "0"));
    this.configurations.put("graphics", frontendOptionFactory.create("heldItemTooltips", Boolean.TYPE, "true"));
    this.configurations.put("graphics", frontendOptionFactory.create("gamma", Double.TYPE, "0.0").setRange(0D, 1D));
    this.configurations.put("graphics", frontendOptionFactory.create("biomeBlendRadius", Integer.TYPE, "2").setRange(0D, 7D));
    this.configurations.put("graphics", frontendOptionFactory.create("forceUnicodeFont", AttackIndicatorStatus.class, "false"));
    this.configurations.put("graphics", frontendOptionFactory.create("guiScale", Integer.TYPE, "0").setRange(0, 2));
    this.configurations.put("graphics", frontendOptionFactory.create("renderClouds", CloudOption.class, "true"));
    this.configurations.put("graphics", frontendOptionFactory.create("maxFps", Integer.TYPE, "120").setRange(10, 260));
    this.configurations.put("graphics", frontendOptionFactory.create("glDebugVerbosity", Integer.TYPE, "1"));
    this.configurations.put("graphics", frontendOptionFactory.create("skipMultiplayerWarning", Boolean.TYPE, "false"));
    this.configurations.put("graphics", frontendOptionFactory.create("renderDistance", Integer.TYPE, "12").setRange(2, 32));
    this.configurations.put("graphics", frontendOptionFactory.create("fullscreen", Boolean.TYPE, "false"));
    this.configurations.put("graphics", frontendOptionFactory.create("fullscreenResolution", String.class, ""));
    this.configurations.put("graphics", frontendOptionFactory.create("entityShadows", Boolean.TYPE, "true"));
    this.configurations.put("graphics", frontendOptionFactory.create("advancedItemTooltips", Boolean.TYPE, "false"));
    this.configurations.put("graphics", frontendOptionFactory.create("particles", ParticleStatus.class, "0"));
    this.configurations.put("graphics", frontendOptionFactory.create("enableVsync", Boolean.TYPE, "true"));
    this.configurations.put("graphics", frontendOptionFactory.create("fancyGraphics", GraphicsFanciness.class, "true"));
    this.configurations.put("graphics", frontendOptionFactory.create("mipmapLevels", Integer.class, "4").setRange(0, 4));
    // 1.16
    this.configurations.put("graphics", frontendOptionFactory.create("entityDistanceScaling", Double.TYPE, "1.0").setRange(0.5D, 5D));
    this.configurations.put("graphics", frontendOptionFactory.create("screenEffectScale", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("graphics", frontendOptionFactory.create("fovEffectScale", Double.TYPE, "1.0").setRange(0D, 1D));

    this.configurations.put("mouse", frontendOptionFactory.create("touchscreen", Boolean.TYPE, "false"));
    this.configurations.put("mouse", frontendOptionFactory.create("discreteMouseScroll", Boolean.TYPE, "false"));
    this.configurations.put("mouse", frontendOptionFactory.create("invertYMouse", Boolean.TYPE, "false"));
    this.configurations.put("mouse", frontendOptionFactory.create("rawMouseInput", Boolean.TYPE, "true"));
    this.configurations.put("mouse", frontendOptionFactory.create("mouseSensitivity", Double.TYPE, "0.5").setRange(0D, 1D));
    this.configurations.put("mouse", frontendOptionFactory.create("mouseWheelSensitivity", Double.TYPE, "1.0").setRange(0.01D, 10D));

    this.configurations.put("keys", frontendOptionFactory.create("pickItem", String.class, "key.mouse.middle"));
    this.configurations.put("keys", frontendOptionFactory.create("playerlist", String.class, "key.keyboard.tab"));
    this.configurations.put("keys", frontendOptionFactory.create("advancements", String.class, "key.keyboard.l"));
    this.configurations.put("keys", frontendOptionFactory.create("sprint", String.class, "key.keyboard.left.control"));
    this.configurations.put("keys", frontendOptionFactory.create("forward", String.class, "key.keyboard.w"));
    this.configurations.put("keys", frontendOptionFactory.create("drop", String.class, "key.keyboard.q"));
    this.configurations.put("keys", frontendOptionFactory.create("back", String.class, "key.keyboard.s"));
    this.configurations.put("keys", frontendOptionFactory.create("attack", String.class, "key.mouse.left"));
    this.configurations.put("keys", frontendOptionFactory.create("saveToolbarActivator", String.class, "key.keyboard.c"));
    this.configurations.put("keys", frontendOptionFactory.create("loadToolbarActivator", String.class, "key.keyboard.x"));
    this.configurations.put("keys", frontendOptionFactory.create("swapHands", String.class, "key.keyboard.f"));
    this.configurations.put("keys", frontendOptionFactory.create("fullscreen", String.class, "key.keyboard.f12"));
    this.configurations.put("keys", frontendOptionFactory.create("chat", String.class, "key.keyboard.t"));
    this.configurations.put("keys", frontendOptionFactory.create("togglePerspective", String.class, "key.keyboard.f5"));
    this.configurations.put("keys", frontendOptionFactory.create("screenshot", String.class, "key.keyboard.f2"));
    this.configurations.put("keys", frontendOptionFactory.create("command", String.class, "key.keyboard.slash"));
    this.configurations.put("keys", frontendOptionFactory.create("left", String.class, "key.keyboard.a"));
    this.configurations.put("keys", frontendOptionFactory.create("spectatorOutlines", String.class, "key.keyboard.unknown"));
    this.configurations.put("keys", frontendOptionFactory.create("sneak", String.class, "key.keyboard.left.shift"));
    this.configurations.put("keys", frontendOptionFactory.create("jump", String.class, "key.keyboard.space"));
    this.configurations.put("keys", frontendOptionFactory.create("right", String.class, "key.keyboard.d"));
    this.configurations.put("keys", frontendOptionFactory.create("smoothCamera", String.class, "key.keyboard.unknown"));
    this.configurations.put("keys", frontendOptionFactory.create("use", String.class, "key.mouse.right"));
    this.configurations.put("keys", frontendOptionFactory.create("hotbar.1", String.class, "key.keyboard.1"));
    this.configurations.put("keys", frontendOptionFactory.create("hotbar.2", String.class, "key.keyboard.2"));
    this.configurations.put("keys", frontendOptionFactory.create("hotbar.3", String.class, "key.keyboard.3"));
    this.configurations.put("keys", frontendOptionFactory.create("hotbar.4", String.class, "key.keyboard.4"));
    this.configurations.put("keys", frontendOptionFactory.create("hotbar.5", String.class, "key.keyboard.5"));
    this.configurations.put("keys", frontendOptionFactory.create("hotbar.6", String.class, "key.keyboard.6"));
    this.configurations.put("keys", frontendOptionFactory.create("hotbar.7", String.class, "key.keyboard.7"));
    this.configurations.put("keys", frontendOptionFactory.create("hotbar.8", String.class, "key.keyboard.8"));
    this.configurations.put("keys", frontendOptionFactory.create("hotbar.9", String.class, "key.keyboard.9"));

    this.configurations.put("skinModel", frontendOptionFactory.create("cape", Boolean.TYPE, "true"));
    this.configurations.put("skinModel", frontendOptionFactory.create("jacket", Boolean.TYPE, "true"));
    this.configurations.put("skinModel", frontendOptionFactory.create("leftSleeve", Boolean.TYPE, "true"));
    this.configurations.put("skinModel", frontendOptionFactory.create("rightSleeve", Boolean.TYPE, "true"));
    this.configurations.put("skinModel", frontendOptionFactory.create("rightPantsLeg", Boolean.TYPE, "true"));
    this.configurations.put("skinModel", frontendOptionFactory.create("leftPantsLeg", Boolean.TYPE, "true"));
    this.configurations.put("skinModel", frontendOptionFactory.create("mainHand", Hand.Side.class, "right"));

    this.configurations.put("sounds", frontendOptionFactory.create("master", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("sounds", frontendOptionFactory.create("voice", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("sounds", frontendOptionFactory.create("record", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("sounds", frontendOptionFactory.create("music", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("sounds", frontendOptionFactory.create("weather", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("sounds", frontendOptionFactory.create("block", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("sounds", frontendOptionFactory.create("player", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("sounds", frontendOptionFactory.create("neutral", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("sounds", frontendOptionFactory.create("ambient", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("sounds", frontendOptionFactory.create("hostile", Double.TYPE, "1.0").setRange(0D, 1D));
    this.configurations.put("sounds", frontendOptionFactory.create("showSubtitles", Boolean.TYPE, "false"));

    this.configurations.put("resources", frontendOptionFactory.create("resourcePacks", List.class, "[]"));
    this.configurations.put("resources", frontendOptionFactory.create("incompatibleResourcePacks", List.class, "[]"));

    this.configurations.put("other", frontendOptionFactory.create("useNativeTransport", Boolean.TYPE, "true"));
    this.configurations.put("other", frontendOptionFactory.create("hideServerAddress", Boolean.TYPE, "false"));
    this.configurations.put("other", frontendOptionFactory.create("pauseOnLostFocus", Boolean.TYPE, "false"));
    this.configurations.put("other", frontendOptionFactory.create("lastServer", String.class, ""));
    this.configurations.put("other", frontendOptionFactory.create("lang", String.class, "en_US"));
    this.configurations.put("other", frontendOptionFactory.create("tutorialStep", TutorialSteps.class, "movement"));
    this.configurations.put("other", frontendOptionFactory.create("snooperEnabled", Boolean.TYPE, "false"));
    // 1.16
    this.configurations.put("other", frontendOptionFactory.create("syncChunkWrites", Boolean.TYPE, "true"));
  }

}
