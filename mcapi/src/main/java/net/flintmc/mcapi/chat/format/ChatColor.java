package net.flintmc.mcapi.chat.format;

import net.flintmc.framework.stereotype.MathHelper;
import net.flintmc.mcapi.chat.exception.InvalidChatColorException;

import java.util.HashMap;
import java.util.Map;

/**
 * Colors that can be used by chat components. Since Minecraft 1.16, RGB colors can be used. This
 * will override any {@link ChatFormat} in the legacy message, so the formats should be defined
 * after the color has been defined.
 */
public class ChatColor {

  /**
   * The character in legacy chat messages which defines that the char (for RGB the six or seven
   * chars) after it defines a color.
   */
  public static final char PREFIX_CHAR = '\u00A7';

  private static final Map<String, ChatColor> DEFAULT_COLORS = new HashMap<>();
  private static final Map<Character, ChatColor> DEFAULT_COLORS_BY_CHAR = new HashMap<>();

  public static final ChatColor DARK_RED = defaultColor("DARK_RED", '4', 11141120);
  public static final ChatColor RED = defaultColor("RED", 'c', 16733525);
  public static final ChatColor GOLD = defaultColor("GOLD", '6', 16755200);
  public static final ChatColor YELLOW = defaultColor("YELLOW", 'e', 16777045);
  public static final ChatColor DARK_GREEN = defaultColor("DARK_GREEN", '2', 43520);
  public static final ChatColor GREEN = defaultColor("GREEN", 'a', 5635925);
  public static final ChatColor AQUA = defaultColor("AQUA", 'b', 5636095);
  public static final ChatColor DARK_AQUA = defaultColor("DARK_AQUA", '3', 43690);
  public static final ChatColor DARK_BLUE = defaultColor("DARK_BLUE", '1', 170);
  public static final ChatColor BLUE = defaultColor("BLUE", '9', 5592575);
  public static final ChatColor LIGHT_PURPLE = defaultColor("LIGHT_PURPLE", 'd', 16733695);
  public static final ChatColor DARK_PURPLE = defaultColor("DARK_PURPLE", '5', 11141290);
  public static final ChatColor WHITE = defaultColor("WHITE", 'f', 16777215);
  public static final ChatColor GRAY = defaultColor("GRAY", '7', 11184810);
  public static final ChatColor DARK_GRAY = defaultColor("DARK_GRAY", '8', 5592405);
  public static final ChatColor BLACK = defaultColor("BLACK", '0', 0);

  private static boolean rgbSupport = false; // TODO: set to true on 1.16+

  private final String name;
  private final String lowerName;
  private final String colorCode;
  private final int rgb;
  private final boolean defaultColor;

  private ChatColor(String name, String colorCode, int rgb) {
    this(name, colorCode, rgb, false);
  }

  private ChatColor(String name, String colorCode, int rgb, boolean defaultColor) {
    this.name = name;
    this.lowerName = name.toLowerCase();
    this.colorCode = colorCode;
    this.rgb = rgb;
    this.defaultColor = defaultColor;
  }

  private static ChatColor defaultColor(String name, char colorCode, int rgb) {
    ChatColor color = new ChatColor(name, String.valueOf(colorCode), rgb, true);
    DEFAULT_COLORS.put(name, color);
    DEFAULT_COLORS_BY_CHAR.put(colorCode, color);
    return color;
  }

  private static ChatColor of(String name, String colorCode, int rgb) {
    return new ChatColor(name, colorCode, rgb);
  }

  /**
   * Creates a new color for the given values.
   *
   * @param name The non-null name of the new color
   * @param colorChar The character which defines the color in legacy chat messages
   * @param rgb The rgb value of the new color
   * @return The new non-null color
   */
  public static ChatColor of(String name, char colorChar, int rgb) {
    return of(name, String.valueOf(colorChar), rgb);
  }

  /**
   * Retrieves the default color by the given name.
   *
   * @param name The case-sensitive non-null name of the default color
   * @return The color or {@code null} if no color that name exists
   */
  public static ChatColor getByName(String name) {
    return DEFAULT_COLORS.get(name);
  }

  /**
   * Retrieves the default color by the given char. E.g. `4` for {@link #DARK_RED}
   *
   * @param c The character of the default color
   * @return The color or {@code null} if no color with that code exists
   */
  public static ChatColor getByChar(char c) {
    return DEFAULT_COLORS_BY_CHAR.get(c);
  }

  /**
   * Parses the given {@code rawColor} either as a hex color or as a Minecraft default color
   *
   * @param rawColor The 1, 6 or 7 characters long text containing either the RGB value in the
   *     hexadecimal format (with or without the `#` prefix) or the Minecraft color codes like `4`
   *     for {@link #DARK_RED}.
   * @return The result or {@code null} if no color with the 1 length char exists
   * @throws InvalidChatColorException If the length of the input doesn't match
   * @throws InvalidChatColorException If the given hex value is no valid hexadecimal integer
   */
  public static ChatColor parse(String rawColor) {
    if (rawColor.length() != 1 && rawColor.length() != 6 && rawColor.length() != 7) {
      throw new InvalidChatColorException("Every color must be 1, 6 or 7 chars long");
    }

    if (rawColor.length() == 1) {
      return getByChar(rawColor.charAt(0));
    }
    return forRGB(rawColor);
  }

  /**
   * Parses the given {@code rgbHex} string as a hex value and creates a new color with the parsed
   * value.
   *
   * @param rgbHex The 6 (or with a `#` as a prefix 7) characters long text containing the RGB value
   *     in the hexadecimal format
   * @return The non-null result
   * @throws InvalidChatColorException If the length of the input doesn't match
   * @throws InvalidChatColorException If the input is no valid hexadecimal integer
   * @see #forRGB(int)
   */
  public static ChatColor forRGB(String rgbHex) {
    if ((rgbHex.length() != 7 && rgbHex.charAt(0) != '#') && rgbHex.length() != 6) {
      throw new InvalidChatColorException(
          "RGB hex value has to be exactly 6 characters (7 with #) long");
    }
    if (rgbHex.charAt(0) == '#') {
      rgbHex = rgbHex.substring(1);
    }

    try {
      return forRGB("#" + rgbHex, Integer.parseInt(rgbHex, 16));
    } catch (NumberFormatException exception) {
      throw new InvalidChatColorException("Invalid hex value", exception);
    }
  }

  /**
   * Creates a new color with the given RGB value. If {@link #hasRgbSupport()} is {@code false},
   * this method will find the nearest matching color by using {@link #findNearestDefaultColor()}.
   *
   * @param rgb The rgb value without an alpha definition
   * @return The non-null result
   */
  public static ChatColor forRGB(int rgb) {
    return forRGB("#" + Integer.toHexString(rgb), rgb);
  }

  private static ChatColor forRGB(String hex, int rgb) {
    ChatColor color = of("RGB" + hex, hex, rgb);

    if (hasRgbSupport()) {
      return color;
    }

    return color.findNearestDefaultColor();
  }

  /**
   * Retrieves whether RGB support is enabled or not.
   *
   * @return Whether RGB is supported or not
   */
  public static boolean hasRgbSupport() {
    return ChatColor.rgbSupport;
  }

  /** Enables/Disables the RGB support for colors */
  public static void setRgbSupport(boolean rgbSupport) {
    ChatColor.rgbSupport = rgbSupport;
  }

  /**
   * Finds the nearest matching color to this color out of all available default colors.
   *
   * @return The non-null color
   */
  public ChatColor findNearestDefaultColor() {
    if (this.isDefaultColor()) {
      return this;
    }

    ChatColor nearestColor = null;
    double lowestDistanceSq = Double.MAX_VALUE;

    for (ChatColor defaultColor : DEFAULT_COLORS.values()) {
      double distanceSq = this.distanceSquared(defaultColor);
      if (distanceSq < lowestDistanceSq) {
        nearestColor = defaultColor;
        lowestDistanceSq = distanceSq;
      }
    }

    return nearestColor != null ? nearestColor : WHITE;
  }

  /**
   * Retrieves the name of this color, usually in the upper case.
   *
   * @return The non-null name of this color
   */
  public String getName() {
    return this.name;
  }

  /**
   * Retrieves the name of this color in the lower case.
   *
   * @return The non-null name of this color
   */
  public String getLowerName() {
    return this.lowerName;
  }

  /**
   * Retrieves the code for the definition of this color in the legacy chat message
   *
   * @return The non-null code of this color
   */
  public String getColorCode() {
    return this.colorCode;
  }

  /** Retrieves the rgb value of this color. */
  public int getRgb() {
    return this.rgb;
  }

  /**
   * Retrieves whether this color is a default color by Minecraft or a custom color with a custom
   * RGB value.
   */
  public boolean isDefaultColor() {
    return this.defaultColor;
  }

  /**
   * Retrieves the red content out of the RGB of this color.
   *
   * @return the red content as an int
   */
  public int getRed() {
    return (this.rgb >> 16) & 0xFF;
  }

  /**
   * Retrieves the green content out of the RGB of this color.
   *
   * @return the green content as an int
   */
  public int getGreen() {
    return (this.rgb >> 8) & 0xFF;
  }

  /**
   * Retrieves the blue content out of the RGB of this color.
   *
   * @return the blue content as an int
   */
  public int getBlue() {
    return this.rgb & 0xFF;
  }

  /**
   * Calculates the squared distance between this color and the given {@code target} color.
   *
   * @return The distance squared
   */
  public double distanceSquared(ChatColor target) {
    return MathHelper.square(target.getRed() - this.getRed())
        + MathHelper.square(target.getGreen() - this.getGreen())
        + MathHelper.square(target.getBlue() - this.getBlue());
  }

  /**
   * Calculates the square root of the squared distance between this color and the given {@code
   * target} color.
   *
   * @return The distance
   * @see #distanceSquared(ChatColor)
   */
  public double distance(ChatColor target) {
    return Math.sqrt(this.distanceSquared(target));
  }

  /**
   * Transforms this color into text for the legacy chat message.
   *
   * @return The non-null definition for this color in the legacy message
   */
  public String toPlainText() {
    return PREFIX_CHAR + this.colorCode;
  }

  /** @see #toPlainText() */
  @Override
  public String toString() {
    return this.toPlainText();
  }
}
