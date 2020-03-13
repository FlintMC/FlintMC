package net.labyfy.mod;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;

@Singleton
public class ModLoader {

  private final File modsFolder;

  @Inject
  private ModLoader(@Named("modsFolder") File modsFolder) {
    this.modsFolder = modsFolder;
  }
}
