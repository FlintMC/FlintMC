package net.flintmc.framework.packages.localization;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents the localization of a package.
 */
public interface PackageLocalization {

  /**
   * Retrieves the localization code.
   *
   * @return The code of the localization.
   */
  String getLocalizationCode();

  /**
   * Retrieves the {@link #getLocalizationContent()} as a {@link String}.
   *
   * @return The localization content as a string.
   */
  String getLocalizationContentAsString();

  /**
   * Retrieves the localization content as a byte array.
   *
   * @return The byte array of the localization content.
   */
  byte[] getLocalizationContent();

  /**
   * A factory class for creating {@link PackageLocalization}'s.
   */
  @AssistedFactory(PackageLocalization.class)
  interface Factory {

    /**
     * Creates a new {@link PackageLocalization} with the given {@code localizationCode} and {@code localizationContent}.
     *
     * @param localizationCode    The code of the localization.
     * @param localizationContent The content of the localization.
     * @return A created package localization.
     */
    PackageLocalization create(
            @Assisted("localizationCode") String localizationCode,
            @Assisted("localizationContent") byte[] localizationContent
    );

  }

}
