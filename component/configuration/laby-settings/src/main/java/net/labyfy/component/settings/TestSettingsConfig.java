package net.labyfy.component.settings;

import net.labyfy.chat.annotation.Component;
import net.labyfy.chat.component.TextComponent;
import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.defval.annotation.*;
import net.labyfy.component.settings.annotation.ui.Category;
import net.labyfy.component.settings.annotation.ui.Description;
import net.labyfy.component.settings.annotation.ui.DisplayName;
import net.labyfy.component.settings.options.BooleanSetting;
import net.labyfy.component.settings.options.dropdown.CustomSelectSetting;
import net.labyfy.component.settings.options.dropdown.EnumSelectSetting;
import net.labyfy.component.settings.options.dropdown.SelectMenuType;
import net.labyfy.component.settings.options.dropdown.Selection;
import net.labyfy.component.settings.options.keybind.DefaultKeyBind;
import net.labyfy.component.settings.options.keybind.KeyBindSetting;
import net.labyfy.component.settings.options.keybind.PhysicalKey;
import net.labyfy.component.settings.options.numeric.NumericSetting;
import net.labyfy.component.settings.options.numeric.Range;
import net.labyfy.component.settings.options.numeric.SliderSetting;
import net.labyfy.component.settings.options.text.*;

// TODO remove

// TODO move the settings into LabyMod? Problem: We wouldn't be able to use it in the GameSettings module

@Config
@Category("TestSettings")
public interface TestSettingsConfig {

  @CustomSelectSetting(value = {
      @Selection("val1"),
      @Selection("val2"),
      @Selection("val3"),
      @Selection("val4")
  }, type = SelectMenuType.SWITCH)
  @DefaultString("val3")
  String getCustomDropDown();

  @EnumSelectSetting(SelectMenuType.SWITCH)
  @DefaultEnum(2 /* A3 */)
  TestEnum getEnumDropDown();

  @KeyBindSetting
  @DefaultKeyBind(PhysicalKey.B)
  PhysicalKey getKeyBind();

  @NumericSetting
  @DefaultNumber(10)
  double getNumeric();

  @NumericSetting
  @DefaultNumber(5)
  int getIntNumeric();

  @SliderSetting(@Range(min = -20, max = 10))
  @DefaultNumber(10)
  int getSlider();

  @CharSetting
  @DefaultChar('a')
  char getChar();

  @StringSetting(maxLength = 5)
  @DefaultString("asdf")
  String getString();

  @StringSetting(StringRestriction.URL_ONLY)
  String getURL();

  enum TestEnum {

    A1,
    A2,
    A3,
    A4,
    A5

  }

  SubSettings getSubSettings();

  interface SubSettings {

    @DisplayName(@Component(value = "labymod.settings.booleanValue", translate = true))
    @Description(@Component("§6Some boolean §cvalue"))
    @BooleanSetting
    @DefaultBoolean(true)
    boolean isBooleanValue();

    @StringSetting
    @DefaultString("asdf")
    String getAsdf();

    @StringSetting(StringRestriction.URL_ONLY)
    @DefaultString("https://labymod.net")
    String getUrl();

    @ComponentSetting
    @DefaultComponent(@Component("§cdefault §7component"))
    TextComponent getComponent();

    void setComponent(TextComponent component);

    @EnumSelectSetting
    @DefaultEnum(2 /* A3 */)
    TestEnum getDropDownEnum();

  }

}
