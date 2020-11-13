package net.flintmc.render.minecraft.text.raw;

/** Alignment of the text in the {@link FontRenderer}. */
public enum StringAlignment {

  /** The given text will be displayed directly on the given x and y coordinates. */
  LEFT,

  /**
   * The given text will be displayed half of its length to the right from the given x and y
   * coordinates which means that it is centered from the given coordinates.
   */
  CENTER,

  /**
   * The given text will be displayed left from the given coordinates, x and y being the most right
   * point of the rendered text.
   */
  RIGHT
}
