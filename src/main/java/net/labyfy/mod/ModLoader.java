package net.labyfy.mod;

import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.subproperty.TaskBody;
import net.labyfy.component.tasks.Tasks;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;

@Singleton
@Task(Tasks.PRE_MINECRAFT_INITIALIZE)
public class ModLoader {

  private final File labyfyModsRoot;

  @Inject
  private ModLoader(
          @Named("labyfyModsRoot") File labyfyModsRoot) {
    this.labyfyModsRoot = labyfyModsRoot;
  }

  @TaskBody
  private void load() {
    System.out.println("Load mods...");
  }
}
