package net.labyfy.component.packages.localization;

import java.io.IOException;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * Represents a loader for {@link PackageLocalization}'s.
 */
public interface PackageLocalizationLoader {

  /**
   * Whether if a language folder is present in the given {@link JarFile}.
   *
   * @param file The jar file to check for the language folder.
   * @return {@code true} if a language folder is present, otherwise {@code false}.
   */
  boolean isLanguageFolderPresent(JarFile file);

  /**
   * Tries to load all available languages from the folder in the given jar.
   *
   * @param file The jar file in which the languages can be found.
   * @throws IOException If the files could not be read.
   */
  void loadLocalizations(JarFile file) throws IOException;

  /**
   * Retrieves a key-value system with all available languages.
   *
   * @return A key-value system with all available languages.
   */
  Map<String, PackageLocalization> getLocalizations();

}
