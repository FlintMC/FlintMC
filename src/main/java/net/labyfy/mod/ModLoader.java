package net.labyfy.mod;

import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.subproperty.TaskBody;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBodyPriority;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;

@Singleton
@Task(Tasks.PRE_MINECRAFT_INITIALIZE)
@TaskBodyPriority()
public class ModLoader {

  private final File modsFolder;

  @Inject
  private ModLoader(@Named("modsFolder") File modsFolder) {
    this.modsFolder = modsFolder;
  }

  @TaskBody
  private void load() {
    System.out.println("Load mods...");
  }
}
