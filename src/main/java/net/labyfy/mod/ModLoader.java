package net.labyfy.mod;

import net.labyfy.base.task.Task;
import net.labyfy.base.task.Tasks;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;

@Singleton
@Task(Tasks.PRE_MINECRAFT_INITIALIZE)
public class ModLoader {

  private final File modsFolder;

  @Inject
  private ModLoader(@Named("modsFolder") File modsFolder) {
    this.modsFolder = modsFolder;
  }

}
