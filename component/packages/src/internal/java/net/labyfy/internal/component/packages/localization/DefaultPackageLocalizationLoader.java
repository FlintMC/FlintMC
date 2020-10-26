package net.labyfy.internal.component.packages.localization;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.packages.localization.PackageLocalization;
import net.labyfy.component.packages.localization.PackageLocalizationLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Default implementation of the {@link PackageLocalizationLoader}.
 */
@Implement(PackageLocalizationLoader.class)
public class DefaultPackageLocalizationLoader implements PackageLocalizationLoader {

  public static final int BUFFER_SIZE = 1024;
  public static final String LANGUAGE_FOLDER = "languages/";

  private final PackageLocalization.Factory packageLocalizationFactory;

  private final Map<String, PackageLocalization> localizations;
  private final Set<JarEntry> entries;

  @Inject
  private DefaultPackageLocalizationLoader(PackageLocalization.Factory packageLocalizationFactory) {
    this.packageLocalizationFactory = packageLocalizationFactory;
    this.localizations = Maps.newHashMap();
    this.entries = Sets.newHashSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLanguageFolderPresent(JarFile file) {
    return Collections.list(file.entries()).stream().anyMatch(entry -> entry.getName().equals(LANGUAGE_FOLDER));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void loadLocalizations(JarFile file) throws IOException {

    Collections.list(file.entries()).forEach(entry -> {
      if (entry.getName().startsWith(LANGUAGE_FOLDER)) {
        if (!entry.getName().equals(LANGUAGE_FOLDER)) {
          this.entries.add(entry);
        }
      }
    });

    for (JarEntry entry : entries) {
      String name = entry.getName().substring(LANGUAGE_FOLDER.length());

      if (!name.endsWith(".json")) {
        return;
      }

      String[] split = name.split("\\.");

      if (split.length == 2) {
        this.localizations.put(split[0],
                this.packageLocalizationFactory.create(
                        split[0],
                        this.readLocalization(file.getInputStream(file.getEntry(entry.getName())))
                ));
      }
    }

    this.entries.clear();
  }

  /**
   * {@inheritDoc}
   */
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
