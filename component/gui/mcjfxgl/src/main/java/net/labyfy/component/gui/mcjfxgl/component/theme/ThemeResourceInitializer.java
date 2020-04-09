package net.labyfy.component.gui.mcjfxgl.component.theme;

import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;

@FunctionalInterface
public interface ThemeResourceInitializer {

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  @TaskBody(priority = -1000)
  void initialize();

}
