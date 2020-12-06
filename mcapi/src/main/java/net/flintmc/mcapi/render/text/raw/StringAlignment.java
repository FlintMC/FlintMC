package net.flintmc.mcapi.render.text.raw;

/** Alignment of the text in the {@link FontRenderer}. */
public enum StringAlignment {

  /** The given text will be displayed directly on the given x and y coordinates. */
  LEFT,

  /**
   * The given text will be displayed half of its length to the left from the given x and y
   * coordinates, x being the exact center of the text.
   */
  CENTER,

  /**
   * The given text will be displayed left from the given coordinates, x and y being the most right
   * point of the rendered text.
   */
  RIGHT
}
