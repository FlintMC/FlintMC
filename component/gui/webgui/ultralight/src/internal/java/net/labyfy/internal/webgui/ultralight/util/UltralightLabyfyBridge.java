package net.labyfy.internal.webgui.ultralight.util;

import net.labyfy.component.gui.event.input.Key;
import net.labyfy.component.gui.event.input.ModifierKey;
import net.labymedia.ultralight.input.UltralightInputModifier;
import net.labymedia.ultralight.input.UltralightKey;

import java.util.Set;

/**
 * Utility class for methods translating between Labyfy and Ultralight
 */
public class UltralightLabyfyBridge {
  private UltralightLabyfyBridge() {
    throw new UnsupportedOperationException("Tried to instantiate UltralightLabyfyBridge");
  }

  /**
   * Translates a Labyfy {@link Key} to an {@link UltralightKey}.
   *
   * @param key The key to translate
   * @return The translated key
   */
  public static UltralightKey labyfyToUltralightKey(Key key) {
    switch(key) {
      case SPACE:
        return UltralightKey.SPACE;
      case APOSTROPHE:
        return UltralightKey.OEM_7;
      case COMMA:
        return UltralightKey.OEM_COMMA;
      case MINUS:
        return UltralightKey.OEM_MINUS;
      case PERIOD:
        return UltralightKey.OEM_PERIOD;
      case SLASH:
        return UltralightKey.OEM_2;
      case NUM_0:
        return UltralightKey.NUM_0;
      case NUM_1:
        return UltralightKey.NUM_1;
      case NUM_2:
        return UltralightKey.NUM_2;
      case NUM_3:
        return UltralightKey.NUM_3;
      case NUM_4:
        return UltralightKey.NUM_4;
      case NUM_5:
        return UltralightKey.NUM_5;
      case NUM_6:
        return UltralightKey.NUM_6;
      case NUM_7:
        return UltralightKey.NUM_7;
      case NUM_8:
        return UltralightKey.NUM_8;
      case NUM_9:
        return UltralightKey.NUM_9;
      case SEMICOLON:
        return UltralightKey.OEM_1;
      case EQUAL:
      case KP_EQUAL:
        return UltralightKey.OEM_PLUS;
      case A:
        return UltralightKey.A;
      case B:
        return UltralightKey.B;
      case C:
        return UltralightKey.C;
      case D:
        return UltralightKey.D;
      case E:
        return UltralightKey.E;
      case F:
        return UltralightKey.F;
      case G:
        return UltralightKey.G;
      case H:
        return UltralightKey.H;
      case I:
        return UltralightKey.I;
      case J:
        return UltralightKey.J;
      case K:
        return UltralightKey.K;
      case L:
        return UltralightKey.L;
      case M:
        return UltralightKey.M;
      case N:
        return UltralightKey.N;
      case O:
        return UltralightKey.O;
      case P:
        return UltralightKey.P;
      case Q:
        return UltralightKey.Q;
      case R:
        return UltralightKey.R;
      case S:
        return UltralightKey.S;
      case T:
        return UltralightKey.T;
      case U:
        return UltralightKey.U;
      case V:
        return UltralightKey.V;
      case W:
        return UltralightKey.W;
      case X:
        return UltralightKey.X;
      case Y:
        return UltralightKey.Y;
      case Z:
        return UltralightKey.Z;
      case LEFT_BRACKET:
        return UltralightKey.OEM_4;
      case BACKSLASH:
        return UltralightKey.OEM_5;
      case RIGHT_BRACKET:
        return UltralightKey.OEM_6;
      case GRAVE_ACCENT:
        return UltralightKey.OEM_3;
      case ESCAPE:
        return UltralightKey.ESCAPE;
      case ENTER:
      case KP_ENTER:
        return UltralightKey.RETURN;
      case TAB:
        return UltralightKey.TAB;
      case BACKSPACE:
        return UltralightKey.BACK;
      case INSERT:
        return UltralightKey.INSERT;
      case DELETE:
        return UltralightKey.DELETE;
      case RIGHT:
        return UltralightKey.RIGHT;
      case LEFT:
        return UltralightKey.LEFT;
      case DOWN:
        return UltralightKey.DOWN;
      case UP:
        return UltralightKey.UP;
      case PAGE_UP:
        return UltralightKey.PRIOR;
      case PAGE_DOWN:
        return UltralightKey.NEXT;
      case HOME:
        return UltralightKey.HOME;
      case END:
        return UltralightKey.END;
      case CAPS_LOCK:
        return UltralightKey.CAPITAL;
      case SCROLL_LOCK:
        return UltralightKey.SCROLL;
      case NUM_LOCK:
        return UltralightKey.NUMLOCK;
      case PRINT_SCREEN:
        return UltralightKey.SNAPSHOT;
      case PAUSE:
        return UltralightKey.PAUSE;
      case F1:
        return UltralightKey.F1;
      case F2:
        return UltralightKey.F2;
      case F3:
        return UltralightKey.F3;
      case F4:
        return UltralightKey.F4;
      case F5:
        return UltralightKey.F5;
      case F6:
        return UltralightKey.F6;
      case F7:
        return UltralightKey.F7;
      case F8:
        return UltralightKey.F8;
      case F9:
        return UltralightKey.F9;
      case F10:
        return UltralightKey.F10;
      case F11:
        return UltralightKey.F11;
      case F12:
        return UltralightKey.F12;
      case F13:
        return UltralightKey.F13;
      case F14:
        return UltralightKey.F14;
      case F15:
        return UltralightKey.F15;
      case F16:
        return UltralightKey.F16;
      case F17:
        return UltralightKey.F17;
      case F18:
        return UltralightKey.F18;
      case F19:
        return UltralightKey.F19;
      case F20:
        return UltralightKey.F20;
      case F21:
        return UltralightKey.F21;
      case F22:
        return UltralightKey.F22;
      case F23:
        return UltralightKey.F23;
      case F24:
        return UltralightKey.F24;
      case KP_0:
        return UltralightKey.NUMPAD0;
      case KP_1:
        return UltralightKey.NUMPAD1;
      case KP_2:
        return UltralightKey.NUMPAD2;
      case KP_3:
        return UltralightKey.NUMPAD3;
      case KP_4:
        return UltralightKey.NUMPAD4;
      case KP_5:
        return UltralightKey.NUMPAD5;
      case KP_6:
        return UltralightKey.NUMPAD6;
      case KP_7:
        return UltralightKey.NUMPAD7;
      case KP_8:
        return UltralightKey.NUMPAD8;
      case KP_9:
        return UltralightKey.NUMPAD9;
      case KP_DECIMAL:
        return UltralightKey.DECIMAL;
      case KP_DIVIDE:
        return UltralightKey.DIVIDE;
      case KP_MULTIPLY:
        return UltralightKey.MULTIPLY;
      case KP_SUBTRACT:
        return UltralightKey.SUBTRACT;
      case KP_ADD:
        return UltralightKey.ADD;
      case LEFT_SHIFT:
      case RIGHT_SHIFT:
        return UltralightKey.SHIFT;
      case LEFT_CONTROL:
      case RIGHT_CONTROL:
        return UltralightKey.CONTROL;
      case LEFT_ALT:
      case RIGHT_ALT:
        return UltralightKey.MENU;
      case LEFT_SUPER:
        return UltralightKey.LWIN;
      case RIGHT_SUPER:
        return UltralightKey.RWIN;
      default:
        return UltralightKey.UNKNOWN;
    }
  }

  /**
   * Translates a Labyfy {@link ModifierKey} into an Ultralight {@link UltralightInputModifier} bitfield.
   *
   * @param keys The set of modifier keys to translate
   * @return The translated bitfield
   */
  public static int labyfyToUltralightModifierKeys(Set<ModifierKey> keys) {
    int ultralightModifiers = 0;

    if(keys.contains(ModifierKey.SHIFT)) {
      ultralightModifiers |= UltralightInputModifier.SHIFT_KEY;
    }

    if(keys.contains(ModifierKey.CONTROL)) {
      ultralightModifiers |= UltralightInputModifier.CTRL_KEY;
    }

    if(keys.contains(ModifierKey.ALT)) {
      ultralightModifiers |= UltralightInputModifier.ALT_KEY;
    }

    if(keys.contains(ModifierKey.SUPER)) {
      ultralightModifiers |= UltralightInputModifier.META_KEY;
    }

    return ultralightModifiers;
  }
}
