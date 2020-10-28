package net.labyfy.component.settings;

import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.settings.annotation.ui.Category;
import net.labyfy.component.settings.options.dropdown.CustomDropDownSetting;
import net.labyfy.component.settings.options.dropdown.EnumDropDownSetting;
import net.labyfy.component.settings.options.keybind.KeyBindSetting;
import net.labyfy.component.settings.options.keybind.PhysicalKey;
import net.labyfy.component.settings.options.numeric.NumericSetting;
import net.labyfy.component.settings.options.numeric.Range;
import net.labyfy.component.settings.options.numeric.SliderSetting;
import net.labyfy.component.settings.options.text.CharSetting;
import net.labyfy.component.settings.options.text.StringRestriction;
import net.labyfy.component.settings.options.text.StringSetting;

@Config
@Category("TestSettings")
public interface TestSettingsConfig {

  @CustomDropDownSetting(value = {"val1", "val2", "val3", "val4"}, defaultValue = "val3")
  String getCustomDropDown();

  @EnumDropDownSetting(defaultValue = 2 /* A3 */)
  TestEnum getEnumDropDown();

  @KeyBindSetting(defaultValue = PhysicalKey.B)
  PhysicalKey getKeyBind();

  @NumericSetting(defaultValue = 10)
  double getNumeric();

  @NumericSetting(defaultValue = 5)
  int getIntNumeric();

  @SliderSetting(defaultValue = 10, value = @Range(min = -20, max = 10))
  int getSlider();

  @CharSetting(defaultValue = 'a')
  char getChar();

  @StringSetting(defaultValue = "asdf", maxLength = 5)
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

  /*
  SubSettings getSubSettings();

  interface SubSettings {

    @NamedSetting(@Component(value = "labymod.settings.booleanValue", translate = true))
    @DescribedSetting(@Component("§6Some boolean §cvalue"))
    @BooleanSetting(defaultValue = true)
    boolean isBooleanValue();

    @StringSetting(defaultValue = "asdf")
    String getAsdf();

    @StringSetting(value = StringRestriction.URL_ONLY, defaultValue = "https://labymod.net")
    String getUrl();

    //@ComponentSetting(defaultValue = "§cdefault §7component §avalue")
    // TODO: this requires custom serializers like those for the Map/Collection: TextComponent getComponent();

    @EnumDropDownSetting(defaultValue = 2) // = A3
    TestEnum getDropDownEnum();

  }
  */

}
