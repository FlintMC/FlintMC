package net.labyfy.component.gui.mcjfxgl;

import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;

@FunctionalInterface
public interface McJfxGLInitializer {

  @Task(Tasks.POST_MINECRAFT_INITIALIZE)
  @TaskBody
  void initialize(McJfxGLApplication mcJfxGLApplication);
}
