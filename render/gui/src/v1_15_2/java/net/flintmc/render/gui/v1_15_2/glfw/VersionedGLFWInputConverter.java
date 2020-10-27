package net.flintmc.render.gui.v1_15_2.glfw;

import net.flintmc.render.gui.event.input.InputState;
import net.flintmc.render.gui.event.input.Key;
import net.flintmc.render.gui.event.input.ModifierKey;
import net.flintmc.render.gui.event.input.MouseButton;
import org.lwjgl.glfw.GLFW;

import java.util.EnumSet;
import java.util.Set;

/**
 * Utility class to convert GLFW constants to Flint constants.
 */
public class VersionedGLFWInputConverter {
  // Static class
  private VersionedGLFWInputConverter() {
    throw new UnsupportedOperationException("GLFWInput converter is a utility class");
  }

  /**
   * Converts a GLFW key constant to a Flint key.
   *
   * @param key The GLFW key constant to convert
   * @return The converted key, or {@link Key#UNKNOWN} if the key is {@link GLFW#GLFW_KEY_UNKNOWN}
   *     or not known
   */
  public static Key glfwKeyToFlintKey(int key) {
    switch (key) {
      case GLFW.GLFW_KEY_SPACE:
        return Key.SPACE;

      case GLFW.GLFW_KEY_APOSTROPHE:
        return Key.APOSTROPHE;

      case GLFW.GLFW_KEY_COMMA:
        return Key.COMMA;

      case GLFW.GLFW_KEY_MINUS:
        return Key.MINUS;

      case GLFW.GLFW_KEY_PERIOD:
        return Key.PERIOD;

      case GLFW.GLFW_KEY_SLASH:
        return Key.SLASH;

      case GLFW.GLFW_KEY_0:
        return Key.NUM_0;

      case GLFW.GLFW_KEY_1:
        return Key.NUM_1;

      case GLFW.GLFW_KEY_2:
        return Key.NUM_2;

      case GLFW.GLFW_KEY_3:
        return Key.NUM_3;

      case GLFW.GLFW_KEY_4:
        return Key.NUM_4;

      case GLFW.GLFW_KEY_5:
        return Key.NUM_5;

      case GLFW.GLFW_KEY_6:
        return Key.NUM_6;

      case GLFW.GLFW_KEY_7:
        return Key.NUM_7;

      case GLFW.GLFW_KEY_8:
        return Key.NUM_8;

      case GLFW.GLFW_KEY_9:
        return Key.NUM_9;

      case GLFW.GLFW_KEY_SEMICOLON:
        return Key.SEMICOLON;

      case GLFW.GLFW_KEY_EQUAL:
        return Key.EQUAL;

      case GLFW.GLFW_KEY_A:
        return Key.A;

      case GLFW.GLFW_KEY_B:
        return Key.B;

      case GLFW.GLFW_KEY_C:
        return Key.C;

      case GLFW.GLFW_KEY_D:
        return Key.D;

      case GLFW.GLFW_KEY_E:
        return Key.E;

      case GLFW.GLFW_KEY_F:
        return Key.F;

      case GLFW.GLFW_KEY_G:
        return Key.G;

      case GLFW.GLFW_KEY_H:
        return Key.H;

      case GLFW.GLFW_KEY_I:
        return Key.I;

      case GLFW.GLFW_KEY_J:
        return Key.J;

      case GLFW.GLFW_KEY_K:
        return Key.K;

      case GLFW.GLFW_KEY_L:
        return Key.L;

      case GLFW.GLFW_KEY_M:
        return Key.M;

      case GLFW.GLFW_KEY_N:
        return Key.N;

      case GLFW.GLFW_KEY_O:
        return Key.O;

      case GLFW.GLFW_KEY_P:
        return Key.P;

      case GLFW.GLFW_KEY_Q:
        return Key.Q;

      case GLFW.GLFW_KEY_R:
        return Key.R;

      case GLFW.GLFW_KEY_S:
        return Key.S;

      case GLFW.GLFW_KEY_T:
        return Key.T;

      case GLFW.GLFW_KEY_U:
        return Key.U;

      case GLFW.GLFW_KEY_V:
        return Key.V;

      case GLFW.GLFW_KEY_W:
        return Key.W;

      case GLFW.GLFW_KEY_X:
        return Key.X;

      case GLFW.GLFW_KEY_Y:
        return Key.Y;

      case GLFW.GLFW_KEY_Z:
        return Key.Z;

      case GLFW.GLFW_KEY_LEFT_BRACKET:
        return Key.LEFT_BRACKET;

      case GLFW.GLFW_KEY_BACKSLASH:
        return Key.BACKSLASH;

      case GLFW.GLFW_KEY_RIGHT_BRACKET:
        return Key.RIGHT_BRACKET;

      case GLFW.GLFW_KEY_GRAVE_ACCENT:
        return Key.GRAVE_ACCENT;

      case GLFW.GLFW_KEY_WORLD_1:
        return Key.WORLD_1;

      case GLFW.GLFW_KEY_WORLD_2:
        return Key.WORLD_2;

      case GLFW.GLFW_KEY_ESCAPE:
        return Key.ESCAPE;

      case GLFW.GLFW_KEY_ENTER:
        return Key.ENTER;

      case GLFW.GLFW_KEY_TAB:
        return Key.TAB;

      case GLFW.GLFW_KEY_BACKSPACE:
        return Key.BACKSPACE;

      case GLFW.GLFW_KEY_INSERT:
        return Key.INSERT;

      case GLFW.GLFW_KEY_DELETE:
        return Key.DELETE;

      case GLFW.GLFW_KEY_RIGHT:
        return Key.RIGHT;

      case GLFW.GLFW_KEY_LEFT:
        return Key.LEFT;

      case GLFW.GLFW_KEY_DOWN:
        return Key.DOWN;

      case GLFW.GLFW_KEY_UP:
        return Key.UP;

      case GLFW.GLFW_KEY_PAGE_UP:
        return Key.PAGE_UP;

      case GLFW.GLFW_KEY_PAGE_DOWN:
        return Key.PAGE_DOWN;

      case GLFW.GLFW_KEY_HOME:
        return Key.HOME;

      case GLFW.GLFW_KEY_END:
        return Key.END;

      case GLFW.GLFW_KEY_CAPS_LOCK:
        return Key.CAPS_LOCK;

      case GLFW.GLFW_KEY_SCROLL_LOCK:
        return Key.SCROLL_LOCK;

      case GLFW.GLFW_KEY_NUM_LOCK:
        return Key.NUM_LOCK;

      case GLFW.GLFW_KEY_PRINT_SCREEN:
        return Key.PRINT_SCREEN;

      case GLFW.GLFW_KEY_PAUSE:
        return Key.PAUSE;

      case GLFW.GLFW_KEY_F1:
        return Key.F1;

      case GLFW.GLFW_KEY_F2:
        return Key.F2;

      case GLFW.GLFW_KEY_F3:
        return Key.F3;

      case GLFW.GLFW_KEY_F4:
        return Key.F4;

      case GLFW.GLFW_KEY_F5:
        return Key.F5;

      case GLFW.GLFW_KEY_F6:
        return Key.F6;

      case GLFW.GLFW_KEY_F7:
        return Key.F7;

      case GLFW.GLFW_KEY_F8:
        return Key.F8;

      case GLFW.GLFW_KEY_F9:
        return Key.F9;

      case GLFW.GLFW_KEY_F10:
        return Key.F10;

      case GLFW.GLFW_KEY_F11:
        return Key.F11;

      case GLFW.GLFW_KEY_F12:
        return Key.F12;

      case GLFW.GLFW_KEY_F13:
        return Key.F13;

      case GLFW.GLFW_KEY_F14:
        return Key.F14;

      case GLFW.GLFW_KEY_F15:
        return Key.F15;

      case GLFW.GLFW_KEY_F16:
        return Key.F16;

      case GLFW.GLFW_KEY_F17:
        return Key.F17;

      case GLFW.GLFW_KEY_F18:
        return Key.F18;

      case GLFW.GLFW_KEY_F19:
        return Key.F19;

      case GLFW.GLFW_KEY_F20:
        return Key.F20;

      case GLFW.GLFW_KEY_F21:
        return Key.F21;

      case GLFW.GLFW_KEY_F22:
        return Key.F22;

      case GLFW.GLFW_KEY_F23:
        return Key.F23;

      case GLFW.GLFW_KEY_F24:
        return Key.F24;

      case GLFW.GLFW_KEY_F25:
        return Key.F25;

      case GLFW.GLFW_KEY_KP_0:
        return Key.KP_0;

      case GLFW.GLFW_KEY_KP_1:
        return Key.KP_1;

      case GLFW.GLFW_KEY_KP_2:
        return Key.KP_2;

      case GLFW.GLFW_KEY_KP_3:
        return Key.KP_3;

      case GLFW.GLFW_KEY_KP_4:
        return Key.KP_4;

      case GLFW.GLFW_KEY_KP_5:
        return Key.KP_5;

      case GLFW.GLFW_KEY_KP_6:
        return Key.KP_6;

      case GLFW.GLFW_KEY_KP_7:
        return Key.KP_7;

      case GLFW.GLFW_KEY_KP_8:
        return Key.KP_8;

      case GLFW.GLFW_KEY_KP_9:
        return Key.KP_9;

      case GLFW.GLFW_KEY_KP_DECIMAL:
        return Key.KP_DECIMAL;

      case GLFW.GLFW_KEY_KP_DIVIDE:
        return Key.KP_DIVIDE;

      case GLFW.GLFW_KEY_KP_MULTIPLY:
        return Key.KP_MULTIPLY;

      case GLFW.GLFW_KEY_KP_SUBTRACT:
        return Key.KP_SUBTRACT;

      case GLFW.GLFW_KEY_KP_ADD:
        return Key.KP_ADD;

      case GLFW.GLFW_KEY_KP_ENTER:
        return Key.KP_ENTER;

      case GLFW.GLFW_KEY_KP_EQUAL:
        return Key.KP_EQUAL;

      case GLFW.GLFW_KEY_LEFT_SHIFT:
        return Key.LEFT_SHIFT;

      case GLFW.GLFW_KEY_LEFT_CONTROL:
        return Key.LEFT_CONTROL;

      case GLFW.GLFW_KEY_LEFT_ALT:
        return Key.LEFT_ALT;

      case GLFW.GLFW_KEY_LEFT_SUPER:
        return Key.LEFT_SUPER;

      case GLFW.GLFW_KEY_RIGHT_SHIFT:
        return Key.RIGHT_SHIFT;

      case GLFW.GLFW_KEY_RIGHT_CONTROL:
        return Key.RIGHT_CONTROL;

      case GLFW.GLFW_KEY_RIGHT_ALT:
        return Key.RIGHT_ALT;

      case GLFW.GLFW_KEY_RIGHT_SUPER:
        return Key.RIGHT_SUPER;

      case GLFW.GLFW_KEY_MENU:
        return Key.MENU;

      case GLFW.GLFW_KEY_UNKNOWN:
      default:
        return Key.UNKNOWN;
    }
  }

  /**
   * Converts a GLFW modifier bitfield to a Flint modifier key set.
   *
   * @param modifiers The GLFW bitfield to convert
   * @return A set containing the Flint modifier key equivalents of the GLFW bitfield
   */
  public static Set<ModifierKey> glfwModifierToFlintModifier(int modifiers) {
    Set<ModifierKey> flintModifiers = EnumSet.noneOf(ModifierKey.class);

    if ((modifiers & GLFW.GLFW_MOD_SHIFT) != 0) {
      flintModifiers.add(ModifierKey.SHIFT);
    }

    if ((modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
      flintModifiers.add(ModifierKey.CONTROL);
    }

    if ((modifiers & GLFW.GLFW_MOD_ALT) != 0) {
      flintModifiers.add(ModifierKey.ALT);
    }

    if ((modifiers & GLFW.GLFW_MOD_SUPER) != 0) {
      flintModifiers.add(ModifierKey.SUPER);
    }

    return flintModifiers;
  }

  /**
   * Converts a GLFW mouse button constant to a Flint mouse button,
   *
   * @param button The GLFW constant to convert
   * @return The converted mouse button, or {@link MouseButton#UNKNOWN} if the button is not known.
   */
  public static MouseButton glfwMouseButtonToFlintMouseButton(int button) {
    switch (button) {
      case GLFW.GLFW_MOUSE_BUTTON_LEFT:
        return MouseButton.LEFT;

      case GLFW.GLFW_MOUSE_BUTTON_MIDDLE:
        return MouseButton.MIDDLE;

      case GLFW.GLFW_MOUSE_BUTTON_RIGHT:
        return MouseButton.RIGHT;

      case GLFW.GLFW_MOUSE_BUTTON_4:
        return MouseButton.EXTENDED_4;

      case GLFW.GLFW_MOUSE_BUTTON_5:
        return MouseButton.EXTENDED_5;

      case GLFW.GLFW_MOUSE_BUTTON_6:
        return MouseButton.EXTENDED_6;

      case GLFW.GLFW_MOUSE_BUTTON_7:
        return MouseButton.EXTENDED_7;

      case GLFW.GLFW_MOUSE_BUTTON_8:
        return MouseButton.EXTENDED_8;

      default:
        return MouseButton.UNKNOWN;
    }
  }

  /**
   * Converts a GLFW action constant to a Flint input state.
   *
   * @param state The GLFW constant to convert
   * @return The converted mouse input state
   * @throws IllegalArgumentException If {@code state} is not one of {@link GLFW#GLFW_PRESS}, {@link
   *     GLFW#GLFW_RELEASE} or {@link GLFW#GLFW_REPEAT}
   */
  public static InputState glfwActionToFlintInputState(int state) {
    switch (state) {
      case GLFW.GLFW_PRESS:
        return InputState.PRESS;

      case GLFW.GLFW_RELEASE:
        return InputState.RELEASE;

      case GLFW.GLFW_REPEAT:
        return InputState.REPEAT;

      default:
        throw new IllegalArgumentException(state + " is not a valid GLFW mouse button state");
    }
  }
}
