package net.labyfy.component.gamesettings;

/**
 * An enumeration representing some keys from a keyboard or mouse.
 */
public enum KeyBindMappings {

  UNKNOWN("key.keyboard.unknown", -1, 0),
  MOUSE_LEFT("key.mouse.left", 0, -100),
  MOUSE_RIGHT("key.mouse.right", 1, -99),
  MOUSE_MIDDLE("key.mouse.middle", 2, -98),
  MOUSE_4("key.mouse.4", 3, -97),
  MOUSE_5("key.mouse.5", 4, -96),
  MOUSE_6("key.mouse.6", 5, -95),
  MOUSE_7("key.mouse.7", 6, -94),
  MOUSE_8("key.mouse.8", 7, -93),
  KEYBOARD_0("key.keyboard.0", 48, 11),
  KEYBOARD_1("key.keyboard.1", 49, 2),
  KEYBOARD_2("key.keyboard.2", 50, 3),
  KEYBOARD_3("key.keyboard.3", 51, 4),
  KEYBOARD_4("key.keyboard.4", 52, 5),
  KEYBOARD_5("key.keyboard.5", 53, 6),
  KEYBOARD_6("key.keyboard.6", 54, 7),
  KEYBOARD_7("key.keyboard.7", 55, 8),
  KEYBOARD_8("key.keyboard.8", 56, 9),
  KEYBOARD_9("key.keyboard.9", 57, 10),
  A("key.keyboard.a", 65, 30),
  B("key.keyboard.b", 66, 48),
  C("key.keyboard.c", 67, 46),
  D("key.keyboard.d", 68, 32),
  E("key.keyboard.e", 69, 18),
  F("key.keyboard.f", 70, 33),
  G("key.keyboard.g", 71, 34),
  H("key.keyboard.h", 72, 35),
  I("key.keyboard.i", 73, 23),
  J("key.keyboard.j", 74, 36),
  K("key.keyboard.k", 75, 37),
  L("key.keyboard.l", 76, 38),
  M("key.keyboard.m", 77, 50),
  N("key.keyboard.n", 78, 49),
  O("key.keyboard.o", 79, 24),
  P("key.keyboard.p", 80, 25),
  Q("key.keyboard.q", 81, 16),
  R("key.keyboard.r", 82, 19),
  S("key.keyboard.s", 83, 31),
  T("key.keyboard.t", 84, 20),
  U("key.keyboard.u", 85, 22),
  V("key.keyboard.v", 86, 47),
  W("key.keyboard.w", 87, 17),
  X("key.keyboard.x", 88, 45),
  Y("key.keyboard.y", 89, 21),
  Z("key.keyboard.z", 90, 44),
  F1("key.keyboard.f1", 290, 59),
  F2("key.keyboard.f2", 291, 60),
  F3("key.keyboard.f3", 292, 61),
  F4("key.keyboard.f4", 293, 62),
  F5("key.keyboard.f5", 294, 63),
  F6("key.keyboard.f6", 295, 64),
  F7("key.keyboard.f7", 296, 65),
  F8("key.keyboard.f8", 297, 66),
  F9("key.keyboard.f9", 298, 67),
  F10("key.keyboard.f10", 299, 68),
  F11("key.keyboard.f11", 300, 87),
  F12("key.keyboard.f12", 301, 88),
  F13("key.keyboard.f13", 302, 100),
  F14("key.keyboard.f14", 303, 101),
  F15("key.keyboard.f15", 304, 102),
  F16("key.keyboard.f16", 305, 103),
  F17("key.keyboard.f17", 306, 104),
  F18("key.keyboard.f18", 307, 105),
  F19("key.keyboard.f19", 308, 113),
  F20("key.keyboard.f20", 309),
  F21("key.keyboard.f21", 310),
  F22("key.keyboard.f22", 311),
  F23("key.keyboard.f23", 312),
  F24("key.keyboard.f24", 313),
  F25("key.keyboard.f25", 314),
  NUMPAD_LOCK("key.keyboard.num.lock", 282, 69),
  NUMPAD_0("key.keyboard.keypad.0", 320, 82),
  NUMPAD_1("key.keyboard.keypad.1", 321, 79),
  NUMPAD_2("key.keyboard.keypad.2", 322, 80),
  NUMPAD_3("key.keyboard.keypad.3", 323, 81),
  NUMPAD_4("key.keyboard.keypad.4", 324, 75),
  NUMPAD_5("key.keyboard.keypad.5", 325, 76),
  NUMPAD_6("key.keyboard.keypad.6", 326, 77),
  NUMPAD_7("key.keyboard.keypad.7", 327, 71),
  NUMPAD_8("key.keyboard.keypad.8", 328, 72),
  NUMPAD_9("key.keyboard.keypad.9", 329, 73),
  NUMPAD_ADD("key.keyboard.keypad.add", 334, 78),
  NUMPAD_DECIMAL("key.keyboard.keypad.decimal", 330, 83),
  NUMPAD_ENTER("key.keyboard.keypad.enter", 335, 156),
  NUMPAD_EQUAL("key.keyboard.keypad.equal", 336, 141),
  NUMPAD_MULTIPLY("key.keyboard.keypad.multiply", 332, 55),
  NUMPAD_DIVIDE("key.keyboard.keypad.divide", 331, 181),
  NUMPAD_SUBTRACt("key.keyboard.keypad.subtract", 333, 74),
  DOWN("key.keyboard.down", 264, 208),
  LEFT("key.keyboard.left", 263, 203),
  RIGHT("key.keyboard.right", 262, 205),
  UP("key.keyboard.up", 265, 200),
  APOSTROPHE("key.keyboard.apostrophe", 39, 40),
  BACKSLASH("key.keyboard.backslash", 92, 43),
  COMMA("key.keyboard.comma", 44, 51),
  EQUAL("key.keyboard.equal", 61, 13),
  GRAVE_ACCENT("key.keyboard.grave.accent", 96, 41),
  LEFT_BRACKET("key.keyboard.left.bracket", 91, 26),
  MINUS("key.keyboard.minus", 45, 12),
  PERIOD("key.keyboard.period", 46, 52),
  RIGHT_BRACKET("key.keyboard.right.bracket", 93, 27),
  SEMICOLON("key.keyboard.semicolon", 59, 39),
  SLASH("key.keyboard.slash", 47, 53),
  SPACE("key.keyboard.space", 32, 57),
  TAB("key.keyboard.tab", 258, 15),
  LEFT_ALT("key.keyboard.left.alt", 342, 56),
  CONTROL("key.keyboard.left.control", 341, 29),
  SHIFT("key.keyboard.left.shift", 340, 42),
  LEFT_WIN("key.keyboard.left.win", 343, 219),
  RIGHT_ALT("key.keyboard.right.alt", 346, 184),
  RIGHT_CONTROl("key.keyboard.right.control", 345, 157),
  RIGHT_SHIFT("key.keyboard.right.shift", 344, 54),
  RIGHT_WIN("key.keyboard.right.win", 347, 220),
  ENTER("key.keyboard.enter", 257, 28),
  ESCAPE("key.keyboard.escape", 256, 1),
  BACKSPACE("key.keyboard.backspace", 259, 14),
  DELETE("key.keyboard.delete", 261, 211),
  END("key.keyboard.end", 269, 207),
  HOME("key.keyboard.home", 268, 199),
  INSERT("key.keyboard.insert", 260, 210),
  PAGE_DOWN("key.keyboard.page.down", 267, 209),
  PAGE_UP("key.keyboard.page.up", 266, 201),
  CAPS_LOCK("key.keyboard.caps.lock", 280, 58),
  PAUSE("key.keyboard.pause", 284),
  SCROLL_LOCK("key.keyboard.scroll.lock", 281, 70),
  MENU("key.keyboard.menu", 348),
  PRINT_SCREEN("key.keyboard.print.screen", 283),
  WORLD_1("key.keyboard.world.1", 161),
  WORLD_2("key.keyboard.world.2", 162);

  private final String configurationName;
  private final int key;
  private final int scanCode;

  KeyBindMappings(String configurationName, int key) {
    this(configurationName, key, -1);
  }

  KeyBindMappings(String configurationName, int key, int scanCode) {
    this.configurationName = configurationName;
    this.key = key;
    this.scanCode = scanCode;
  }

  /**
   * Retrieves the scan code by the configuration name.
   *
   * @param name The configuration name.
   * @return The scan code of the given configuration name or {@code -1}.
   */
  public static int getScanCode(String name) {
    for (KeyBindMappings value : values()) {
      if (value.getConfigurationName().equalsIgnoreCase(name)) {
        return value.getScanCode();
      }
    }
    return -1;
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
   * Retrieves the scan code of an enum constant.
   *
   * @return The scan code.
   */
  public int getScanCode() {
    return scanCode;
  }

}
