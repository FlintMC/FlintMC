package net.flintmc.framework.packages.internal.localization;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.packages.localization.PackageLocalization;
import net.flintmc.framework.packages.localization.PackageLocalizationLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.JarFile;

/** Default implementation of the {@link PackageLocalizationLoader}. */
@Implement(PackageLocalizationLoader.class)
public class DefaultPackageLocalizationLoader implements PackageLocalizationLoader {

  public static final int BUFFER_SIZE = 1024;
  public static final String LANGUAGE_FOLDER = "languages/";

  private final PackageLocalization.Factory packageLocalizationFactory;

  private final Map<String, PackageLocalization> localizations;

  @Inject
  private DefaultPackageLocalizationLoader(PackageLocalization.Factory packageLocalizationFactory) {
    this.packageLocalizationFactory = packageLocalizationFactory;
    this.localizations = Maps.newHashMap();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isLanguageFolderPresent(JarFile file) {
    return file.getEntry(LANGUAGE_FOLDER) != null;
  }

  /** {@inheritDoc} */
  @Override
  public void loadLocalizations(JarFile file) throws IOException {
    file.stream()
        .forEach(
            entry -> {
              if (entry.getName().startsWith(LANGUAGE_FOLDER)) {
                if (!entry.getName().equals(LANGUAGE_FOLDER)) {
                  String name = entry.getName().substring(LANGUAGE_FOLDER.length());

                  if (!name.endsWith(".json")) {
                    return;
                  }

                  String[] split = name.split("\\.");

                  if (split.length == 2) {
                    try {
                      this.localizations.put(
                          split[0],
                          this.packageLocalizationFactory.create(
                              split[0],
                              this.readLocalization(
                                  file.getInputStream(file.getEntry(entry.getName())))));
                    } catch (IOException exception) {
                      exception.printStackTrace();
                    }
                  }
                }
              }
            });
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, PackageLocalization> getLocalizations() {
    return this.localizations;
  }

  private byte[] readLocalization(InputStream inputStream) throws IOException {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    byte[] buffer = new byte[BUFFER_SIZE];

    int length;

    while ((length = inputStream.read(buffer)) != -1) {
      result.write(buffer, 0, length);
    }

    return result.toByteArray();
  }
}
