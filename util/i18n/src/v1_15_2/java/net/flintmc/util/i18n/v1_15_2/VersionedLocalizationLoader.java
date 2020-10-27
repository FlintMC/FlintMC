package net.flintmc.util.i18n.v1_15_2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.PackageLoader;
import net.flintmc.framework.packages.localization.PackageLocalization;
import net.flintmc.util.i18n.Localization;
import net.flintmc.util.i18n.LocalizationLoader;

import java.util.Map;

/**
 * 1.15.2 implementation of the {@link LocalizationLoader}.
 */
@Singleton
@Implement(value = LocalizationLoader.class, version = "1.15.2")
public class VersionedLocalizationLoader implements LocalizationLoader {

  private final PackageLoader packageLoader;

  @Inject
  private VersionedLocalizationLoader(PackageLoader packageLoader) {
    this.packageLoader = packageLoader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void load(Localization localization, String languageCode) {
    if (!this.packageLoader.getLoadedPackages().isEmpty()) {
      for (Package loadedPackage : this.packageLoader.getLoadedPackages()) {
        PackageLocalization packageLocalization = loadedPackage.getPackageLocalizationLoader()
                .getLocalizations()
                .get(languageCode);

        if (packageLocalization != null) {
          JsonObject object = JsonParser.parseString(packageLocalization.getLocalizationContentAsString()).getAsJsonObject();

          for (Map.Entry<String, JsonElement> elementEntry : object.entrySet()) {
            localization.add(elementEntry.getKey(), elementEntry.getValue().getAsString());
          }
        }

      }
    }
  }
}
