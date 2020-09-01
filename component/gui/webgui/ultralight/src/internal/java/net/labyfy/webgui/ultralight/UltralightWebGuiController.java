package net.labyfy.webgui.ultralight;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.webgui.WebGuiController;
import net.labyfy.webgui.WebGuiView;
import net.labymedia.ultralight.UltralightJava;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

@Singleton
@Implement(WebGuiController.class)
public class UltralightWebGuiController implements WebGuiController {
  private final Logger logger;

  @Inject
  private UltralightWebGuiController(@InjectLogger Logger logger) {
    this.logger = logger;
  }

  /**
   * Runs Ultralight setup and initializes the native files.
   */
  @Task(Tasks.PRE_MINECRAFT_INITIALIZE)
  public void setupUltralight() {
    logger.debug("Setting up Ultralight...");
  }

  @Override
  public Collection<WebGuiView> getViews() {
    return null;
  }
}
