package net.flintmc.render.minecraft.text.raw;

import java.util.List;

/** Renderer for simple texts in Minecraft. */
public interface FontRenderer {

  /**
   * Calculates the width of the given text when it will be rendered. Ignores all minecraft color
   * codes like "§a". If the text is bold with "§l", this will also be included in the width of the
   * chars.
   *
   * @param text The non-null text to calculate the width for
   * @return The length of the text, always >= 0
   */
  int getStringWidth(String text);

  /**
   * Calculates the width of the given character when it will be rendered. If the character is '§'
   * (which is used to define chat colors/formats in Minecraft), 0 will be returned.
   *
   * @param c The character to calculate the length for
   * @return The length of the character
   * @see #getBoldCharWidth(char)
   */
  float getCharWidth(char c);

  /**
   * Calculates the width of the given character when it will be rendered. If the character is '§'
   * (which is used to define chat colors/formats in Minecraft), 0 will be returned. This calculates
   * the width as if the char would be displayed bold ("§l") in the chat.
   *
   * @param c The character to calculate the length for
   * @return The length of the character
   * @see #getCharWidth(char)
   */
  float getBoldCharWidth(char c);

  /**
   * Draws the given text on the screen. Color codes like "§a" and formatting like "§l" will all be
   * included and also change the color of the text. If no color is set, the given rgba value will
   * be used.
   *
   * @param x The x position on the screen, may be modified by {@link StringAlignment}.
   * @param y The y position on the screen
   * @param text The non-null text to be rendered
   * @param rgba The rgba value for the text, -1 means white, this may be overridden by color codes
   *     in the text like "§a"
   * @param alignment The non-null alignment of the text on the screen
   * @param maxLineLength The max length per line (has to be >= 0), if not 0, the text may be split
   *     into multiple lines, but Minecraft will try to split it at line breaks/spaces if possible.
   * @param shadow Whether a shadow should be drawn or not, if {@code maxLineLength} is not 0, this
   *     will be ignored and a shadow will be drawn
   * @param xFactor The factor to scale the text on the x-axis, 1 to disable scaling
   * @param yFactor The factor to scale the text on the y-axis, 1 to disable scaling
   * @see StringAlignment
   */
  void drawString(
      float x,
      float y,
      String text,
      int rgba,
      StringAlignment alignment,
      int maxLineLength,
      boolean shadow,
      float xFactor,
      float yFactor);

  /**
   * Retrieves a collection with width wrapped strings.
   *
   * @param text The non-null text to be wrapped.
   * @param wrapWidth The width, when the text should be wrapped.
   * @return A collection with width wrapped strings.
   */
  List<String> listFormattedString(String text, int wrapWidth);

  /**
   * Retrieves a wrapped formatted string.
   *
   * @param text The non-null text to be wrapped.
   * @param wrapWidth The width, when the text should be wrapped.
   * @return A wrapped formatted string.
   */
  String wrapFormattedString(String text, int wrapWidth);

  /**
   * Retrieves the height of a wrapped string.
   *
   * @param text The non-null text to be wrapped.
   * @param wrapWidth The width, when the text should be wrapped.
   * @return THe height of the wrapped string.
   */
  int getStringWrappedHeight(String text, int wrapWidth);
}
