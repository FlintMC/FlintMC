package net.labyfy.internal.component.gamesettings.modifier;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.modifier.AnnotationModifier;
import net.labyfy.component.config.modifier.ConfigModificationHandler;
import net.labyfy.component.gamesettings.configuration.AccessibilityConfiguration;
import net.labyfy.component.i18n.I18n;
import net.labyfy.component.settings.options.dropdown.CustomDropDownSetting;

import java.lang.annotation.Annotation;

@Singleton
@AnnotationModifier(value = AccessibilityConfiguration.class, method = "Language")
public class LanguageAnnotationModifier implements ConfigModificationHandler {

  private final CustomDropDownSetting modified;

  @Inject
  public LanguageAnnotationModifier(I18n i18n) {
    this.modified = new CustomDropDownSetting() {
      @Override
      public String[] value() {
        return i18n.getAvailableLanguages().toArray(new String[0]);
      }

      @Override
      public String defaultValue() {
        return "";
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return CustomDropDownSetting.class;
      }
    };
  }

  @Override
  public Annotation modify(Annotation annotation) {
    return annotation instanceof CustomDropDownSetting ? this.modified : annotation;
  }
}
