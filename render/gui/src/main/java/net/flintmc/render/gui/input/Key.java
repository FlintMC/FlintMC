package net.flintmc.render.gui.input;

import java.util.HashMap;
import java.util.Map;

/** An enumeration representing some keys from a keyboard or mouse. */
public enum Key {
  UNKNOWN("key.keyboard.unknown", -1, -1, false),
  MOUSE_LEFT("key.mouse.left", 0, -100, true),
  MOUSE_RIGHT("key.mouse.right", 1, -99, true),
  MOUSE_MIDDLE("key.mouse.middle", 2, -98, true),
  MOUSE_4("key.mouse.4", 3, -97, true),
  MOUSE_5("key.mouse.5", 4, -96, true),
  MOUSE_6("key.mouse.6", 5, -95, true),
  MOUSE_7("key.mouse.7", 6, -94, true),
  MOUSE_8("key.mouse.8", 7, -93, true),
  KEYBOARD_0("key.keyboard.0", 48, 11, false),
  KEYBOARD_1("key.keyboard.1", 49, 2, false),
  KEYBOARD_2("key.keyboard.2", 50, 3, false),
  KEYBOARD_3("key.keyboard.3", 51, 4, false),
  KEYBOARD_4("key.keyboard.4", 52, 5, false),
  KEYBOARD_5("key.keyboard.5", 53, 6, false),
  KEYBOARD_6("key.keyboard.6", 54, 7, false),
  KEYBOARD_7("key.keyboard.7", 55, 8, false),
  KEYBOARD_8("key.keyboard.8", 56, 9, false),
  KEYBOARD_9("key.keyboard.9", 57, 10, false),
  A("key.keyboard.a", 65, 30, false),
  B("key.keyboard.b", 66, 48, false),
  C("key.keyboard.c", 67, 46, false),
  D("key.keyboard.d", 68, 32, false),
  E("key.keyboard.e", 69, 18, false),
  F("key.keyboard.f", 70, 33, false),
  G("key.keyboard.g", 71, 34, false),
  H("key.keyboard.h", 72, 35, false),
  I("key.keyboard.i", 73, 23, false),
  J("key.keyboard.j", 74, 36, false),
  K("key.keyboard.k", 75, 37, false),
  L("key.keyboard.l", 76, 38, false),
  M("key.keyboard.m", 77, 50, false),
  N("key.keyboard.n", 78, 49, false),
  O("key.keyboard.o", 79, 24, false),
  P("key.keyboard.p", 80, 25, false),
  Q("key.keyboard.q", 81, 16, false),
  R("key.keyboard.r", 82, 19, false),
  S("key.keyboard.s", 83, 31, false),
  T("key.keyboard.t", 84, 20, false),
  U("key.keyboard.u", 85, 22, false),
  V("key.keyboard.v", 86, 47, false),
  W("key.keyboard.w", 87, 17, false),
  X("key.keyboard.x", 88, 45, false),
  Y("key.keyboard.y", 89, 21, false),
  Z("key.keyboard.z", 90, 44, false),
  F1("key.keyboard.f1", 290, 59, false),
  F2("key.keyboard.f2", 291, 60, false),
  F3("key.keyboard.f3", 292, 61, false),
  F4("key.keyboard.f4", 293, 62, false),
  F5("key.keyboard.f5", 294, 63, false),
  F6("key.keyboard.f6", 295, 64, false),
  F7("key.keyboard.f7", 296, 65, false),
  F8("key.keyboard.f8", 297, 66, false),
  F9("key.keyboard.f9", 298, 67, false),
  F10("key.keyboard.f10", 299, 68, false),
  F11("key.keyboard.f11", 300, 87, false),
  F12("key.keyboard.f12", 301, 88, false),
  F13("key.keyboard.f13", 302, 100, false),
  F14("key.keyboard.f14", 303, 101, false),
  F15("key.keyboard.f15", 304, 102, false),
  F16("key.keyboard.f16", 305, 103, false),
  F17("key.keyboard.f17", 306, 104, false),
  F18("key.keyboard.f18", 307, 105, false),
  F19("key.keyboard.f19", 308, 113, false),
  F20("key.keyboard.f20", 309, false),
  F21("key.keyboard.f21", 310, false),
  F22("key.keyboard.f22", 311, false),
  F23("key.keyboard.f23", 312, false),
  F24("key.keyboard.f24", 313, false),
  F25("key.keyboard.f25", 314, false),
  NUMPAD_LOCK("key.keyboard.num.lock", 282, 69, false),
  NUMPAD_0("key.keyboard.keypad.0", 320, 82, false),
  NUMPAD_1("key.keyboard.keypad.1", 321, 79, false),
  NUMPAD_2("key.keyboard.keypad.2", 322, 80, false),
  NUMPAD_3("key.keyboard.keypad.3", 323, 81, false),
  NUMPAD_4("key.keyboard.keypad.4", 324, 75, false),
  NUMPAD_5("key.keyboard.keypad.5", 325, 76, false),
  NUMPAD_6("key.keyboard.keypad.6", 326, 77, false),
  NUMPAD_7("key.keyboard.keypad.7", 327, 71, false),
  NUMPAD_8("key.keyboard.keypad.8", 328, 72, false),
  NUMPAD_9("key.keyboard.keypad.9", 329, 73, false),
  NUMPAD_ADD("key.keyboard.keypad.add", 334, 78, false),
  NUMPAD_DECIMAL("key.keyboard.keypad.decimal", 330, 83, false),
  NUMPAD_ENTER("key.keyboard.keypad.enter", 335, 156, false),
  NUMPAD_EQUAL("key.keyboard.keypad.equal", 336, 141, false),
  NUMPAD_MULTIPLY("key.keyboard.keypad.multiply", 332, 55, false),
  NUMPAD_DIVIDE("key.keyboard.keypad.divide", 331, 181, false),
  NUMPAD_SUBTRACT("key.keyboard.keypad.subtract", 333, 74, false),
  DOWN("key.keyboard.down", 264, 208, false),
  LEFT("key.keyboard.left", 263, 203, false),
  RIGHT("key.keyboard.right", 262, 205, false),
  UP("key.keyboard.up", 265, 200, false),
  APOSTROPHE("key.keyboard.apostrophe", 39, 40, false),
  BACKSLASH("key.keyboard.backslash", 92, 43, false),
  COMMA("key.keyboard.comma", 44, 51, false),
  EQUAL("key.keyboard.equal", 61, 13, false),
  GRAVE_ACCENT("key.keyboard.grave.accent", 96, 41, false),
  LEFT_BRACKET("key.keyboard.left.bracket", 91, 26, false),
  MINUS("key.keyboard.minus", 45, 12, false),
  PERIOD("key.keyboard.period", 46, 52, false),
  RIGHT_BRACKET("key.keyboard.right.bracket", 93, 27, false),
  SEMICOLON("key.keyboard.semicolon", 59, 39, false),
  SLASH("key.keyboard.slash", 47, 53, false),
  SPACE("key.keyboard.space", 32, 57, false),
  TAB("key.keyboard.tab", 258, 15, false),
  LEFT_ALT("key.keyboard.left.alt", 342, 56, false),
  LEFT_CONTROL("key.keyboard.left.control", 341, 29, false),
  LEFT_SHIFT("key.keyboard.left.shift", 340, 42, false),
  LEFT_WIN("key.keyboard.left.win", 343, 219, false),
  RIGHT_ALT("key.keyboard.right.alt", 346, 184, false),
  RIGHT_CONTROL("key.keyboard.right.control", 345, 157, false),
  RIGHT_SHIFT("key.keyboard.right.shift", 344, 54, false),
  RIGHT_WIN("key.keyboard.right.win", 347, 220, false),
  ENTER("key.keyboard.enter", 257, 28, false),
  ESCAPE("key.keyboard.escape", 256, 1, false),
  BACKSPACE("key.keyboard.backspace", 259, 14, false),
  DELETE("key.keyboard.delete", 261, 211, false),
  END("key.keyboard.end", 269, 207, false),
  HOME("key.keyboard.home", 268, 199, false),
  INSERT("key.keyboard.insert", 260, 210, false),
  PAGE_DOWN("key.keyboard.page.down", 267, 209, false),
  PAGE_UP("key.keyboard.page.up", 266, 201, false),
  CAPS_LOCK("key.keyboard.caps.lock", 280, 58, false),
  PAUSE("key.keyboard.pause", 284, false),
  SCROLL_LOCK("key.keyboard.scroll.lock", 281, 70, false),
  MENU("key.keyboard.menu", 348, false),
  PRINT_SCREEN("key.keyboard.print.screen", 283, false),
  WORLD_1("key.keyboard.world.1", 161, false),
  WORLD_2("key.keyboard.world.2", 162, false);

  private static final Map<String, Key> BY_NAME = new HashMap<>();
  private static final Map<Integer, Key> BY_SCAN_CODE = new HashMap<>();
  private static final Map<Integer, Key> BY_KEY_CODE = new HashMap<>();

  static {
    for (Key value : values()) {
      BY_NAME.put(value.configurationName, value);
      BY_SCAN_CODE.put(value.scanCode, value);
      BY_KEY_CODE.put(value.key, value);
    }
  }

  private final String configurationName;
  private final int key;
  private final int scanCode;
  private final boolean mouse;

  Key(String configurationName, int key, boolean mouse) {
    this(configurationName, key, -1, mouse);
  }

  Key(String configurationName, int key, int scanCode, boolean mouse) {
    this.configurationName = configurationName;
    this.key = key;
    this.scanCode = scanCode;
    this.mouse = mouse;
  }

  /**
   * Retrieves the scan code by the given configuration name.
   *
   * @param name The configuration name.
   * @return The scan code of the given configuration name or {@code -1}.
   */
  public static int getScanCode(String name) {
    return BY_NAME.getOrDefault(name, UNKNOWN).scanCode;
  }

  /**
   * Retrieves the key configuration name by the given scanCode.
   *
   * @param scanCode The scan code of a keybinding.
   * @return The configuration name or {@link #UNKNOWN#getConfigurationName()}
   */
  public static String getConfigurationName(int scanCode) {
    return BY_SCAN_CODE.getOrDefault(scanCode, UNKNOWN).getConfigurationName();
  }

  public static Key getByConfigurationName(String configurationName) {
    return BY_NAME.getOrDefault(configurationName, UNKNOWN);
  }

  public static Key getByScanCode(int scanCode) {
    return BY_SCAN_CODE.getOrDefault(scanCode, UNKNOWN);
  }

  public static Key getByKeyCode(int keyCode) {
    return BY_KEY_CODE.getOrDefault(keyCode, UNKNOWN);
  }

  /**
   * Retrieves the configuration name of an enum constant.
   *
   * @return The configuration name.
   */
  public String getConfigurationName() {
    return configurationName;
  }

  /**
   * Retrieves the key code of an enum constant.
   *
   * @return The key code.
   */
  public int getKey() {
    return key;
  }

  /**
   * Retrieves whether this key is on the mouse or on the keyboard.
   *
   * @return {@code true} if it is on the mouse, {@code false} if it is on the keyboard
   */
  public boolean isMouse() {
    return this.mouse;
  }

  /**
   * Retrieves the scan code of an enum constant.
   *
   * @return The scan code.
   */
  public int getScanCode() {
    return scanCode;
  }
}
