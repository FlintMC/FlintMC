package net.labyfy.internal.component.gamesettings.modifier;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.modifier.AnnotationModifier;
import net.labyfy.component.config.modifier.ConfigModificationHandler;
import net.labyfy.component.gamesettings.configuration.AccessibilityConfiguration;
import net.labyfy.component.i18n.I18n;
import net.labyfy.component.settings.options.dropdown.CustomSelectSetting;
import net.labyfy.component.settings.options.dropdown.SelectMenuType;
import net.labyfy.component.settings.options.dropdown.Selection;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
@AnnotationModifier(value = AccessibilityConfiguration.class, method = "Language")
public class LanguageAnnotationModifier implements ConfigModificationHandler {

  private final Map<SelectMenuType, CustomSelectSetting> modifiedTypes;

  @Inject
  public LanguageAnnotationModifier(I18n i18n) {
    this.modifiedTypes = new HashMap<>();

    for (SelectMenuType type : SelectMenuType.values()) {
      CustomSelectSetting setting = new CustomSelectSetting() {
        @Override
        public Selection[] value() {
          Collection<String> languages = i18n.getAvailableLanguages();
          Selection[] selections = new Selection[languages.size()];

          int i = 0;
          for (String language : languages) {
            selections[i++] = new DefaultSelection(language);
          }

          return selections;
        }

        @Override
        public String defaultValue() {
          Collection<String> languages = i18n.getAvailableLanguages();
          return languages.isEmpty() ? "" : languages.iterator().next();
        }

        @Override
        public SelectMenuType type() {
          return type;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
          return CustomSelectSetting.class;
        }
      };
      this.modifiedTypes.put(type, setting);
    }
  }

  @Override
  public Annotation modify(Annotation annotation) {
    return annotation instanceof CustomSelectSetting ? this.modifiedTypes.get(((CustomSelectSetting) annotation).type()) : annotation;
  }
}
