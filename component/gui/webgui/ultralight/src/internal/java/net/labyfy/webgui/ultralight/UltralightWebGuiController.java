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
import net.labymedia.ultralight.UltralightLoadException;
import net.labymedia.ultralight.UltralightPlatform;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Ultralight implementation of the {@link WebGuiController}.
 */
@Singleton
@Implement(WebGuiController.class)
public class UltralightWebGuiController implements WebGuiController {
  private final Logger logger;

  private UltralightPlatform platform;

  @Inject
  private UltralightWebGuiController(@InjectLogger Logger logger) {
    this.logger = logger;
  }

  /**
   * Runs Ultralight setup and initializes the native files.
   */
  @Task(Tasks.PRE_MINECRAFT_INITIALIZE)
  public void setupUltralight() throws UltralightLoadException {
    logger.debug("Setting up Ultralight...");

    // Extract the native libraries into the run directory and load them from there
    // TODO: This works on Windows, but could cause issues on Linux and OSX
    Path runDirectory = Paths.get(".");
    UltralightJava.extractNativeLibrary(runDirectory);
    UltralightJava.load(runDirectory);

    // Get the ultralight platform singleton
    platform = UltralightPlatform.instance();
    logger.debug("Ultralight platform singleton at 0x{}\n", Long.toHexString(platform.getHandle()));
  }

  @Override
  public Collection<WebGuiView> getViews() {
    return null;
  }
}
