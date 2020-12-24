/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.render.gui.v1_15_2.glfw;

import net.flintmc.render.gui.input.InputState;
import net.flintmc.render.gui.input.ModifierKey;
import net.flintmc.render.gui.input.Key;
import org.lwjgl.glfw.GLFW;

import java.util.EnumSet;
import java.util.Set;

/** Utility class to convert GLFW constants to Flint constants. */
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
        return Key.KEYBOARD_0;

      case GLFW.GLFW_KEY_1:
        return Key.KEYBOARD_1;

      case GLFW.GLFW_KEY_2:
        return Key.KEYBOARD_2;

      case GLFW.GLFW_KEY_3:
        return Key.KEYBOARD_3;

      case GLFW.GLFW_KEY_4:
        return Key.KEYBOARD_4;

      case GLFW.GLFW_KEY_5:
        return Key.KEYBOARD_5;

      case GLFW.GLFW_KEY_6:
        return Key.KEYBOARD_6;

      case GLFW.GLFW_KEY_7:
        return Key.KEYBOARD_7;

      case GLFW.GLFW_KEY_8:
        return Key.KEYBOARD_8;

      case GLFW.GLFW_KEY_9:
        return Key.KEYBOARD_9;

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
        return Key.NUMPAD_LOCK;

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
        return Key.NUMPAD_0;

      case GLFW.GLFW_KEY_KP_1:
        return Key.NUMPAD_1;

      case GLFW.GLFW_KEY_KP_2:
        return Key.NUMPAD_2;

      case GLFW.GLFW_KEY_KP_3:
        return Key.NUMPAD_3;

      case GLFW.GLFW_KEY_KP_4:
        return Key.NUMPAD_4;

      case GLFW.GLFW_KEY_KP_5:
        return Key.NUMPAD_5;

      case GLFW.GLFW_KEY_KP_6:
        return Key.NUMPAD_6;

      case GLFW.GLFW_KEY_KP_7:
        return Key.NUMPAD_7;

      case GLFW.GLFW_KEY_KP_8:
        return Key.NUMPAD_8;

      case GLFW.GLFW_KEY_KP_9:
        return Key.NUMPAD_9;

      case GLFW.GLFW_KEY_KP_DECIMAL:
        return Key.NUMPAD_DECIMAL;

      case GLFW.GLFW_KEY_KP_DIVIDE:
        return Key.NUMPAD_DIVIDE;

      case GLFW.GLFW_KEY_KP_MULTIPLY:
        return Key.NUMPAD_MULTIPLY;

      case GLFW.GLFW_KEY_KP_SUBTRACT:
        return Key.NUMPAD_SUBTRACT;

      case GLFW.GLFW_KEY_KP_ADD:
        return Key.NUMPAD_ADD;

      case GLFW.GLFW_KEY_KP_ENTER:
        return Key.NUMPAD_ENTER;

      case GLFW.GLFW_KEY_KP_EQUAL:
        return Key.NUMPAD_EQUAL;

      case GLFW.GLFW_KEY_LEFT_SHIFT:
        return Key.LEFT_SHIFT;

      case GLFW.GLFW_KEY_LEFT_CONTROL:
        return Key.LEFT_CONTROL;

      case GLFW.GLFW_KEY_LEFT_ALT:
        return Key.LEFT_ALT;

      case GLFW.GLFW_KEY_LEFT_SUPER:
        return Key.LEFT_WIN;

      case GLFW.GLFW_KEY_RIGHT_SHIFT:
        return Key.RIGHT_SHIFT;

      case GLFW.GLFW_KEY_RIGHT_CONTROL:
        return Key.RIGHT_CONTROL;

      case GLFW.GLFW_KEY_RIGHT_ALT:
        return Key.RIGHT_ALT;

      case GLFW.GLFW_KEY_RIGHT_SUPER:
        return Key.RIGHT_WIN;

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
   * @return The converted mouse button, or {@link Key#UNKNOWN} if the button is not known.
   */
  public static Key glfwMouseButtonToFlintKey(int button) {
    switch (button) {
      case GLFW.GLFW_MOUSE_BUTTON_LEFT:
        return Key.MOUSE_LEFT;

      case GLFW.GLFW_MOUSE_BUTTON_MIDDLE:
        return Key.MOUSE_MIDDLE;

      case GLFW.GLFW_MOUSE_BUTTON_RIGHT:
        return Key.MOUSE_RIGHT;

      case GLFW.GLFW_MOUSE_BUTTON_4:
        return Key.MOUSE_4;

      case GLFW.GLFW_MOUSE_BUTTON_5:
        return Key.MOUSE_5;

      case GLFW.GLFW_MOUSE_BUTTON_6:
        return Key.MOUSE_6;

      case GLFW.GLFW_MOUSE_BUTTON_7:
        return Key.MOUSE_7;

      case GLFW.GLFW_MOUSE_BUTTON_8:
        return Key.MOUSE_8;

      default:
        return Key.UNKNOWN;
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

  /**
   * Converts a GLFW key constant to a Flint key.
   *
   * @param key The GLFW key constant to convert
   * @return The converted key, or {@link GLFW#GLFW_KEY_UNKNOWN} if the key is {@link Key#UNKNOWN}
   *     or not known
   */
  public static int flintKeyToGlfwKey(Key key) {
    switch (key) {
      case SPACE:
        return GLFW.GLFW_KEY_SPACE;
      case APOSTROPHE:
        return GLFW.GLFW_KEY_APOSTROPHE;
      case COMMA:
        return GLFW.GLFW_KEY_COMMA;
      case MINUS:
        return GLFW.GLFW_KEY_MINUS;
      case PERIOD:
        return GLFW.GLFW_KEY_PERIOD;
      case SLASH:
        return GLFW.GLFW_KEY_SLASH;
      case KEYBOARD_0:
        return GLFW.GLFW_KEY_0;
      case KEYBOARD_1:
        return GLFW.GLFW_KEY_1;
      case KEYBOARD_2:
        return GLFW.GLFW_KEY_2;
      case KEYBOARD_3:
        return GLFW.GLFW_KEY_3;
      case KEYBOARD_4:
        return GLFW.GLFW_KEY_4;
      case KEYBOARD_5:
        return GLFW.GLFW_KEY_5;
      case KEYBOARD_6:
        return GLFW.GLFW_KEY_6;
      case KEYBOARD_7:
        return GLFW.GLFW_KEY_7;
      case KEYBOARD_8:
        return GLFW.GLFW_KEY_8;
      case KEYBOARD_9:
        return GLFW.GLFW_KEY_9;
      case SEMICOLON:
        return GLFW.GLFW_KEY_SEMICOLON;
      case EQUAL:
        return GLFW.GLFW_KEY_EQUAL;
      case A:
        return GLFW.GLFW_KEY_A;
      case B:
        return GLFW.GLFW_KEY_B;
      case C:
        return GLFW.GLFW_KEY_C;
      case D:
        return GLFW.GLFW_KEY_D;
      case E:
        return GLFW.GLFW_KEY_E;
      case F:
        return GLFW.GLFW_KEY_F;
      case G:
        return GLFW.GLFW_KEY_G;
      case H:
        return GLFW.GLFW_KEY_H;
      case I:
        return GLFW.GLFW_KEY_I;
      case J:
        return GLFW.GLFW_KEY_J;
      case K:
        return GLFW.GLFW_KEY_K;
      case L:
        return GLFW.GLFW_KEY_L;
      case M:
        return GLFW.GLFW_KEY_M;
      case N:
        return GLFW.GLFW_KEY_N;
      case O:
        return GLFW.GLFW_KEY_O;
      case P:
        return GLFW.GLFW_KEY_P;
      case Q:
        return GLFW.GLFW_KEY_Q;
      case R:
        return GLFW.GLFW_KEY_R;
      case S:
        return GLFW.GLFW_KEY_S;
      case T:
        return GLFW.GLFW_KEY_T;
      case U:
        return GLFW.GLFW_KEY_U;
      case V:
        return GLFW.GLFW_KEY_V;
      case W:
        return GLFW.GLFW_KEY_W;
      case X:
        return GLFW.GLFW_KEY_X;
      case Y:
        return GLFW.GLFW_KEY_Y;
      case Z:
        return GLFW.GLFW_KEY_Z;
      case LEFT_BRACKET:
        return GLFW.GLFW_KEY_LEFT_BRACKET;
      case BACKSLASH:
        return GLFW.GLFW_KEY_BACKSLASH;
      case RIGHT_BRACKET:
        return GLFW.GLFW_KEY_RIGHT_BRACKET;
      case GRAVE_ACCENT:
        return GLFW.GLFW_KEY_GRAVE_ACCENT;
      case WORLD_1:
        return GLFW.GLFW_KEY_WORLD_1;
      case WORLD_2:
        return GLFW.GLFW_KEY_WORLD_2;
      case ESCAPE:
        return GLFW.GLFW_KEY_ESCAPE;
      case ENTER:
        return GLFW.GLFW_KEY_ENTER;
      case TAB:
        return GLFW.GLFW_KEY_TAB;
      case BACKSPACE:
        return GLFW.GLFW_KEY_BACKSPACE;
      case INSERT:
        return GLFW.GLFW_KEY_INSERT;
      case DELETE:
        return GLFW.GLFW_KEY_DELETE;
      case RIGHT:
        return GLFW.GLFW_KEY_RIGHT;
      case LEFT:
        return GLFW.GLFW_KEY_LEFT;
      case DOWN:
        return GLFW.GLFW_KEY_DOWN;
      case UP:
        return GLFW.GLFW_KEY_UP;
      case PAGE_UP:
        return GLFW.GLFW_KEY_PAGE_UP;
      case PAGE_DOWN:
        return GLFW.GLFW_KEY_PAGE_DOWN;
      case HOME:
        return GLFW.GLFW_KEY_HOME;
      case END:
        return GLFW.GLFW_KEY_END;
      case CAPS_LOCK:
        return GLFW.GLFW_KEY_CAPS_LOCK;
      case SCROLL_LOCK:
        return GLFW.GLFW_KEY_SCROLL_LOCK;
      case NUMPAD_LOCK:
        return GLFW.GLFW_KEY_NUM_LOCK;
      case PRINT_SCREEN:
        return GLFW.GLFW_KEY_PRINT_SCREEN;
      case PAUSE:
        return GLFW.GLFW_KEY_PAUSE;
      case F1:
        return GLFW.GLFW_KEY_F1;
      case F2:
        return GLFW.GLFW_KEY_F2;
      case F3:
        return GLFW.GLFW_KEY_F3;
      case F4:
        return GLFW.GLFW_KEY_F4;
      case F5:
        return GLFW.GLFW_KEY_F5;
      case F6:
        return GLFW.GLFW_KEY_F6;
      case F7:
        return GLFW.GLFW_KEY_F7;
      case F8:
        return GLFW.GLFW_KEY_F8;
      case F9:
        return GLFW.GLFW_KEY_F9;
      case F10:
        return GLFW.GLFW_KEY_F10;
      case F11:
        return GLFW.GLFW_KEY_F11;
      case F12:
        return GLFW.GLFW_KEY_F12;
      case F13:
        return GLFW.GLFW_KEY_F13;
      case F14:
        return GLFW.GLFW_KEY_F14;
      case F15:
        return GLFW.GLFW_KEY_F15;
      case F16:
        return GLFW.GLFW_KEY_F16;
      case F17:
        return GLFW.GLFW_KEY_F17;
      case F18:
        return GLFW.GLFW_KEY_F18;
      case F19:
        return GLFW.GLFW_KEY_F19;
      case F20:
        return GLFW.GLFW_KEY_F20;
      case F21:
        return GLFW.GLFW_KEY_F21;
      case F22:
        return GLFW.GLFW_KEY_F22;
      case F23:
        return GLFW.GLFW_KEY_F23;
      case F24:
        return GLFW.GLFW_KEY_F24;
      case F25:
        return GLFW.GLFW_KEY_F25;
      case NUMPAD_0:
        return GLFW.GLFW_KEY_KP_0;
      case NUMPAD_1:
        return GLFW.GLFW_KEY_KP_1;
      case NUMPAD_2:
        return GLFW.GLFW_KEY_KP_2;
      case NUMPAD_3:
        return GLFW.GLFW_KEY_KP_3;
      case NUMPAD_4:
        return GLFW.GLFW_KEY_KP_4;
      case NUMPAD_5:
        return GLFW.GLFW_KEY_KP_5;
      case NUMPAD_6:
        return GLFW.GLFW_KEY_KP_6;
      case NUMPAD_7:
        return GLFW.GLFW_KEY_KP_7;
      case NUMPAD_8:
        return GLFW.GLFW_KEY_KP_8;
      case NUMPAD_9:
        return GLFW.GLFW_KEY_KP_9;
      case NUMPAD_DECIMAL:
        return GLFW.GLFW_KEY_KP_DECIMAL;
      case NUMPAD_DIVIDE:
        return GLFW.GLFW_KEY_KP_DIVIDE;
      case NUMPAD_MULTIPLY:
        return GLFW.GLFW_KEY_KP_MULTIPLY;
      case NUMPAD_SUBTRACT:
        return GLFW.GLFW_KEY_KP_SUBTRACT;
      case NUMPAD_ADD:
        return GLFW.GLFW_KEY_KP_ADD;
      case NUMPAD_ENTER:
        return GLFW.GLFW_KEY_KP_ENTER;
      case NUMPAD_EQUAL:
        return GLFW.GLFW_KEY_KP_EQUAL;
      case LEFT_SHIFT:
        return GLFW.GLFW_KEY_LEFT_SHIFT;
      case LEFT_CONTROL:
        return GLFW.GLFW_KEY_LEFT_CONTROL;
      case LEFT_ALT:
        return GLFW.GLFW_KEY_LEFT_ALT;
      case LEFT_WIN:
        return GLFW.GLFW_KEY_LEFT_SUPER;
      case RIGHT_SHIFT:
        return GLFW.GLFW_KEY_RIGHT_SHIFT;
      case RIGHT_CONTROL:
        return GLFW.GLFW_KEY_RIGHT_CONTROL;
      case RIGHT_ALT:
        return GLFW.GLFW_KEY_RIGHT_ALT;
      case RIGHT_WIN:
        return GLFW.GLFW_KEY_RIGHT_SUPER;
      case MENU:
        return GLFW.GLFW_KEY_MENU;

      case MOUSE_LEFT:
        return GLFW.GLFW_MOUSE_BUTTON_LEFT;
      case MOUSE_MIDDLE:
        return GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
      case MOUSE_RIGHT:
        return GLFW.GLFW_MOUSE_BUTTON_RIGHT;
      case MOUSE_4:
        return GLFW.GLFW_MOUSE_BUTTON_4;
      case MOUSE_5:
        return GLFW.GLFW_MOUSE_BUTTON_5;
      case MOUSE_6:
        return GLFW.GLFW_MOUSE_BUTTON_6;
      case MOUSE_7:
        return GLFW.GLFW_MOUSE_BUTTON_7;
      case MOUSE_8:
        return GLFW.GLFW_MOUSE_BUTTON_8;

      case UNKNOWN:
      default:
        return GLFW.GLFW_KEY_UNKNOWN;
    }
  }
}
