package net.labyfy.internal.webgui.ultralight;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gui.GuiController;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.internal.webgui.ultralight.view.UltralightMainWebGuiView;
import net.labyfy.internal.webgui.ultralight.view.UltralightWebGuiView;
import net.labyfy.webgui.WebGuiController;
import net.labyfy.webgui.WebGuiView;
import net.labymedia.ultralight.UltralightJava;
import net.labymedia.ultralight.UltralightLoadException;
import net.labymedia.ultralight.UltralightPlatform;
import net.labymedia.ultralight.UltralightRenderer;
import net.labymedia.ultralight.config.FaceWinding;
import net.labymedia.ultralight.config.FontHinting;
import net.labymedia.ultralight.config.UltralightConfig;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Ultralight implementation of the {@link WebGuiController}.
 */
@Singleton
@Implement(WebGuiController.class)
public class UltralightWebGuiController implements WebGuiController {
  private final Logger logger;
  private final GuiController guiController;
  private final Set<UltralightWebGuiView> views;
  private final boolean useGPURenderer; // TODO: Make this configurable

  private UltralightPlatform platform;
  private UltralightRenderer renderer;
  private UltralightMainWebGuiView mainView;

  @Inject
  private UltralightWebGuiController(@InjectLogger Logger logger, GuiController guiController) {
    this.logger = logger;
    this.guiController = guiController;
    this.views = new HashSet<>();
    this.useGPURenderer = false;
  }

  /**
   * Runs Ultralight setup and initializes the native files.
   */
  @Task(Tasks.PRE_MINECRAFT_INITIALIZE)
  public void setupUltralightNatives() throws UltralightLoadException {
    logger.debug("Setting up Ultralight natives...");

    // Extract the native libraries into the run directory and load them from there
    // TODO: This works on Windows, but could cause issues on Linux and OSX
    Path runDirectory = Paths.get(".");
    // UltralightJava.extractNativeLibrary(runDirectory);
    UltralightJava.load(runDirectory);
  }

  /**
   * Wires up the Ultralight main view with the controller.
   */
  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void setupUltralight() {
    logger.debug("Setting up Ultralight...");

    // NOTE: This **needs** to be called on the render thread, else Ultralight will not work!
    // Get the ultralight platform singleton
    platform = UltralightPlatform.instance();
    logger.trace("Ultralight platform singleton at 0x{}\n", Long.toHexString(platform.getHandle()));

    // Configure ultralight
    platform.setConfig(new UltralightConfig()
        .resourcePath("./resources")
        .fontHinting(FontHinting.NORMAL)
        .deviceScale(1.0)
        .faceWinding(FaceWinding.COUNTER_CLOCKWISE)
        .userAgent("Labyfy") // TODO: Use better user agent (possibly unify with framework?)
        .useGpuRenderer(useGPURenderer));

    // Configure the platform
    platform.usePlatformFontLoader();
    platform.usePlatformFileSystem(".");
    platform.setLogger(InjectionHolder.getInjectedInstance(UltralightLoggingBridge.class));

    // Create the renderer
    renderer = UltralightRenderer.create();

    // Create the view displayed in the minecraft window
    this.mainView = InjectionHolder.getInjectedInstance(UltralightMainWebGuiView.class);

    // The view needs to be known by the GUI controller in order to call back to it
    guiController.registerComponent(this.mainView);

    // Set up the view and register it internally
    this.mainView.setURL("http://localhost:8080");
    this.views.add(this.mainView);
  }

  /**
   * Dispatches all render commands and updates the rendering resources.
   */
  public void renderAll() {
    // Synchronize the state of all views
    for(UltralightWebGuiView view : views) {
      view.update();
    }

    /*
     * Do ultralight internal drawing, this will draw **all** views, regardless of their state.
     * The main view can proceed just using the data, however, external views will need special care.
     *
     * Here are the cases how external views need to be handled:
     * 1. The CPU renderer is active:
     *    Textures are uploaded/processed as required, this can happen asynchronously, IF THE DATA IS COPIED!
     *    If the data is not copied, the access needs to be synchronous, or tearing issues might occur.
     *    Moreover, the pixels need to be unlocked afterwards to not block the renderer.
     *
     * 2. The GPU renderer is active:
     *    Things are slightly more complex now. As everything is rendered on 1 OpenGL context (the main context),
     *    external views are slightly harder to implement.
     *
     *    There are 2 possible variants to get data to another view:
     *    2.1 The external view is rendered using a CPU API (Swing/AWT, JavaFX, etc):
     *        The image data needs to be retrieved from the target texture using OpenGL (there are multiple
     *        ways to do that, see glGetTexImage). After that, the image needs to be converted to the target
     *        API and can then be used there. Note that this is slow, but required if the API rendering the
     *        external view is not GPU accelerated.
     *
     *   2.2 The external view is rendered using OpenGL (or any OpenGL interop capable API):
     *       The image data can be kept on the GPU, which is a huge performance boost. For an API which supports OpenGL
     *       interop, the texture handle needs to be converted to the API specific handle and then used there.
     *
     *       In OpenGL the texture handle can in theory be used directly, however, a call to glFinish might be required
     *       in order to ensure the data hs been written fully. Furthermore, the OpenGL context needs to have been
     *       created as a shared context with the main context so that resources can be exchanged. If this is not the
     *       case, either method 2.1 needs to be used or another way of passing the texture to the separate context.
     *
     */
    renderer.update();
    renderer.render();

    if(useGPURenderer) {
      throw new UnsupportedOperationException("Not yet implemented");
    } else {
      // Draw each view
      for(UltralightWebGuiView view : views) {
        view.drawUsingSurface();
      }
    }
  }

  /**
   * Retrieves the renderer the controller is using.
   *
   * @return The current ultralight renderer
   */
  public UltralightRenderer getRenderer() {
    return renderer;
  }

  /**
   * Retrieves the platform the controller is using.
   *
   * @return The current ultralight platform
   */
  public UltralightPlatform getPlatform() {
    return platform;
  }

  @Override
  public Collection<WebGuiView> getViews() {
    return Collections.unmodifiableCollection(views);
  }

  @Override
  public WebGuiView getMainView() {
    return mainView;
  }
}
