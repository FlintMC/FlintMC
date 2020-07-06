package net.labyfy.internal.component.gui.juklearmc.style;

import net.janrupf.juklear.Juklear;
import net.janrupf.juklear.JuklearContext;
import net.janrupf.juklear.style.JuklearColor;
import net.janrupf.juklear.style.JuklearStyle;

/**
 * This class holds the defaults for the Juklear style. It will probably go away once
 * a style system is in place.
 */
// TODO: Remove when style system is in place
public class DefaultLabyModStyle {
  public static final int BRIGHT_BLUE = 0x3BB4FF;
  public static final int DARK_BLUE = 0x041027;
  public static final int PINK = 0xFF3B7A;

  public static final int WHITE = 0xFFFFFFFF;

  public static final int LIGHT_OVERLAY = 0xFFFFFF5E;
  public static final int DARK_OVERLAY = 0x0610275B;

  public static void apply(JuklearContext context) {
    JuklearStyle style = context.getStyle();
    Juklear juklear = context.getJuklear();
    style.getWindow().getFixedBackground().setAsColor(juklear, rgb(juklear, DARK_BLUE));
    style.getButton().getNormal().setAsColor(juklear, rgba(juklear, 100, 100, 100, 102));
    style.getButton().getHover().setAsColor(juklear, rgba(juklear, 120, 120, 120, 153));
    style.getButton().getBorder().set(2.0f);
    style.getButton().getRounding().set(0f);
    style.getButton().getBorderColor().setRGBA(rgba(180, 180, 180, 102));
    style.getButton().getTextNormal().setRGBA(WHITE);
    style.getButton().getTextHover().setRGBA(WHITE);
    style.getButton().getTextActive().setRGBA(WHITE);
  }

  public static JuklearColor rgb(Juklear juklear, int rgb) {
    return new JuklearColor(
        juklear,
        ((rgb >>> 16) & 0xFF),
        ((rgb >>> 8) & 0xFF),
        (rgb & 0xFF)
    );
  }

  public static JuklearColor rgb(Juklear juklear, int r, int g, int b) {
    return new JuklearColor(juklear, r, g, b);
  }

  public static JuklearColor rgba(Juklear juklear, int rgba) {
    return new JuklearColor(
        juklear,
        (rgba >>> 24) & 0xFF,
        (rgba >>> 16) & 0xFF,
        (rgba >>> 8) & 0xFF,
        rgba & 0xFF
    );
  }

  public static JuklearColor rgba(Juklear juklear, int r, int g, int b, int a) {
    return new JuklearColor(juklear, r, g, b, a);
  }

  public static int rgba(int r, int g, int b, int a) {
    return (r << 24) | (g << 16) | (b << 8) | a;
  }
}
