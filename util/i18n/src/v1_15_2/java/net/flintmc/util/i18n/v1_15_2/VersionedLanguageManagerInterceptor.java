package net.flintmc.util.i18n.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.transform.hook.Hook;
import net.flintmc.util.i18n.Localization;
import net.flintmc.util.i18n.LocalizationLoader;
import net.flintmc.util.i18n.v1_15_2.accessible.AccessibleLanguageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.resources.IResourceManager;

@Singleton
public class VersionedLanguageManagerInterceptor {

  private final LocalizationLoader localizationLoader;

  @Inject
  private VersionedLanguageManagerInterceptor(LocalizationLoader localizationLoader) {
    this.localizationLoader = localizationLoader;
  }

  @Hook(
      className = "net.minecraft.client.resources.LanguageManager",
      methodName = "onResourceManagerReload",
      parameters = {@Type(reference = IResourceManager.class)})
  public void hookOnResourceManagerReload(@Named("instance") Object instance) {
    LanguageManager languageManager = (LanguageManager) instance;
    AccessibleLanguageManager accessibleLanguageManager =
        (AccessibleLanguageManager) languageManager;

    Localization localization = (Localization) accessibleLanguageManager.getLocale();
    this.localizationLoader.load(
        localization, this.validLanguageCode(languageManager.getCurrentLanguage()));
  }

  private String validLanguageCode(Language language) {
    return language == null ? Minecraft.getInstance().gameSettings.language : language.getCode();
  }
}
