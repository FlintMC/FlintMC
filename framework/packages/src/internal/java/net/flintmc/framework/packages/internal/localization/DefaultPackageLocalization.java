package net.flintmc.framework.packages.internal.localization;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.packages.localization.PackageLocalization;

/** Default implementation of the {@link PackageLocalization}. */
@Implement(PackageLocalization.class)
public class DefaultPackageLocalization implements PackageLocalization {

  private final String localizationCode;
  private final byte[] localizationContent;
  private final String localizationContentAsString;

  @AssistedInject
  private DefaultPackageLocalization(
      @Assisted("localizationCode") String localizationCode,
      @Assisted("localizationContent") byte[] localizationContent) {
    this.localizationCode = localizationCode;
    this.localizationContent = localizationContent;
    this.localizationContentAsString = new String(localizationContent);
  }

  /** {@inheritDoc} */
  @Override
  public String getLocalizationCode() {
    return this.localizationCode;
  }

  /** {@inheritDoc} */
  @Override
  public String getLocalizationContentAsString() {
    return this.localizationContentAsString;
  }

  /** {@inheritDoc} */
  @Override
  public byte[] getLocalizationContent() {
    return this.localizationContent;
  }
}
