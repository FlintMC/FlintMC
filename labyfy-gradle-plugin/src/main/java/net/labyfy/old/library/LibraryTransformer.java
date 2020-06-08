package net.labyfy.old.library;

import org.gradle.api.artifacts.transform.*;
import org.gradle.api.attributes.Attribute;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.work.InputChanges;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;

public abstract class LibraryTransformer implements TransformAction<TransformParameters.None> {
  public static final Attribute<Boolean> DEOBFUSCATED = Attribute.of("deobfuscated", Boolean.class);

  public static void configure(TransformSpec<TransformParameters.None> spec) {
    spec.getFrom().attribute(DEOBFUSCATED, false);
    spec.getTo().attribute(DEOBFUSCATED, true);
  }

  @InputArtifact
  abstract Provider<FileSystemLocation> getInputArtifact();

  public void transform(@Nonnull TransformOutputs outputs) {
    try {
      File input = getInputArtifact().get().getAsFile();

      String name = input.getName();
      if (!name.endsWith(".xml")) {
        outputs.file(input);
        return;
      } else {
        name = name.substring(0, name.length() - 4) + ".jar";
      }

      File output = outputs.file(name);
      Files.copy(input.toPath(), output.toPath());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
