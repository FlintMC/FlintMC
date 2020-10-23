package net.labyfy.internal.component.gui.juklearmc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.janrupf.juklear.Juklear;
import net.janrupf.juklear.JuklearContext;
import net.janrupf.juklear.drawing.JuklearAntialiasing;
import net.janrupf.juklear.exception.JuklearInitializationException;
import net.janrupf.juklear.font.JuklearFont;
import net.janrupf.juklear.font.JuklearFontAtlas;
import net.janrupf.juklear.font.JuklearFontAtlasEditor;
import net.janrupf.juklear.input.JuklearInput;
import net.janrupf.juklear.input.JuklearMouseButton;
import net.janrupf.juklear.layout.component.base.JuklearTopLevelComponent;
import net.janrupf.juklear.math.JuklearVec2;
import net.janrupf.juklear.util.JuklearNatives;
import net.labyfy.component.gui.GuiController;
import net.labyfy.component.gui.MinecraftWindow;
import net.labyfy.component.gui.RenderExecution;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.gui.event.*;
import net.labyfy.component.gui.juklearmc.JuklearMC;
import net.labyfy.component.gui.juklearmc.JuklearMCBackendProvider;
import net.labyfy.component.gui.juklearmc.menues.JuklearMCComponent;
import net.labyfy.component.gui.juklearmc.menues.JuklearMCScreen;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.internal.component.gui.juklearmc.style.DefaultLabyModStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the JuklearMC management system.
 */
@Singleton
@Implement(JuklearMC.class)
public class DefaultJuklearMC implements GuiInputEventProcessor, GuiComponent, JuklearMC {
  // GUI interaction instances
  private JuklearFontAtlas fontAtlas;
  private final Juklear juklear;
  private final GuiController controller;

  // Management of toplevel components
  private final Map<ScreenName, JuklearMCScreen> overwrittenScreens;
  private final List<JuklearTopLevelComponent> currentScreenTopLevels;
  private final List<JuklearMCComponent> standaloneJuklearComponents;

  // Tasks to run after Juklear is initialized
  private final List<Runnable> initializeTasks;

  private JuklearMCScreen currentJuklearScreen;

  // Juklear state
  private JuklearContext context;
  private JuklearFont defaultFont;
  private JuklearInput input;

  private MinecraftWindow minecraftWindow;

  private boolean hasRenderedThisFrame;

  // Cache of GUI state
  private double mouseX;
  private double mouseY;
  private float scale;

  @Inject
  private DefaultJuklearMC(JuklearMCBackendProvider versionedProvider, GuiController controller) throws IOException {
    // TODO: This should be updated once Juklear has a better system
    JuklearNatives.setupWithTemporaryFolder();
    juklear = Juklear.usingInternalGarbageCollection(versionedProvider.backend());
    this.controller = controller;
    this.overwrittenScreens = new HashMap<>();
    this.currentScreenTopLevels = new ArrayList<>();
    this.standaloneJuklearComponents = new ArrayList<>();
    this.initializeTasks = new ArrayList<>();
  }

  /**
   * Run as a task once OpenGL has been initialized
   *
   * @throws JuklearInitializationException If Juklear and/or its backend fails to initialize
   * @throws IOException                    If an I/O error occurs reading one of the required resources
   */
  @Task(value = Tasks.POST_OPEN_GL_INITIALIZE)
  public void initialize() throws JuklearInitializationException, IOException {
    juklear.init();
    minecraftWindow = InjectionHolder.getInjectedInstance(MinecraftWindow.class);

    // Load our default font, 20 seems to pretty much match the Minecraft scaling
    fontAtlas = juklear.defaultFontAtlas();
    JuklearFontAtlasEditor editor = fontAtlas.begin();
    defaultFont = editor.addFromURL(getClass().getResource("/assets/labymod/fonts/minecraft.ttf"), 20);
    editor.end();

    // Set up the context and apply the default style
    context = juklear.defaultContext(defaultFont);
    DefaultLabyModStyle.apply(context);

    // Make sure to notify the GUI controller that we exist
    controller.registerComponent(this);
    controller.registerInputProcessor(this);

    // After everything else has been done, run the init tasks
    initializeTasks.forEach(Runnable::run);
  }

  /**
   * Marks a vanilla screen as overwritten with a Juklear screen replacement
   *
   * @param screen    The name of the screen to overwrite
   * @param overwrite The screen to use as an overwrite
   */
  public void overwriteScreen(ScreenName screen, JuklearMCScreen overwrite) {
    this.overwrittenScreens.put(screen, overwrite);
  }

  public void registerStandaloneComponent(JuklearMCComponent juklearMCComponent) {
    this.standaloneJuklearComponents.add(juklearMCComponent);
  }

  /**
   * Adds a {@link Runnable} which should be executed after Juklear has been booted
   *
   * @param task The task to run
   */
  public void onInitialize(Runnable task) {
    this.initializeTasks.add(task);
  }

  /**
   * {@inheritDoc}
   */
  public JuklearContext getContext() {
    return context;
  }

  public JuklearFontAtlas getFontAtlas() {
    return this.fontAtlas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void beginInput() {
    scale = 1f / minecraftWindow.getScaleFactor() * 3;

    int drawWidth = (int) (minecraftWindow.getFramebufferWidth() * scale);
    int drawHeight = (int) (minecraftWindow.getFramebufferHeight() * scale);

    if (currentJuklearScreen != null) {
      currentJuklearScreen.updateSize(drawWidth, drawHeight);
    }

    this.input = context.beginInput();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean process(GuiInputEvent event) {
    if (currentJuklearScreen == null) {
      // If there is no active screen we don't consume the events
      return false;
    }

    // Translate the events
    if (event instanceof CursorPosChangedEvent) {
      mouseX = ((CursorPosChangedEvent) event).getX() * scale * minecraftWindow.getFramebufferWidth() / minecraftWindow.getWidth();
      mouseY = ((CursorPosChangedEvent) event).getY() * scale * minecraftWindow.getFramebufferHeight() / minecraftWindow.getHeight();
      input.motion((int) mouseX, (int) mouseY);
    } else if (event instanceof MouseButtonEvent) {
      int button = ((MouseButtonEvent) event).getValue();
      boolean isPressed = ((MouseButtonEvent) event).getState() != MouseButtonEvent.State.RELEASE;

      switch (button) {
        case MouseButtonEvent.LEFT:
          input.button(JuklearMouseButton.LEFT, (int) mouseX, (int) mouseY, isPressed);
          break;

        case MouseButtonEvent.RIGHT:
          input.button(JuklearMouseButton.RIGHT, (int) mouseX, (int) mouseY, isPressed);
          break;

        case MouseButtonEvent.MIDDLE:
          input.button(JuklearMouseButton.MIDDLE, (int) mouseY, (int) mouseY, isPressed);
          break;
      }
    } else if (event instanceof MouseScrolledEvent) {
      input.scroll((float) ((MouseScrolledEvent) event).getXOffset(), (float) ((MouseScrolledEvent) event).getYOffset());
    } else if (event instanceof UnicodeTypedEvent) {
      input.unicode(((UnicodeTypedEvent) event).getValue());
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void endInput() {
    this.input.end();
    this.input = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void screenChanged(ScreenName newScreen) {
    if (!currentScreenTopLevels.isEmpty()) {
      // Clean up everything which has been added by the previous screen
      currentScreenTopLevels.forEach(context::removeTopLevel);
      currentScreenTopLevels.clear();
    }

    if (currentJuklearScreen != null) {
      // Make sure we notify the screen that it is being closed
      currentJuklearScreen.close();
    }

    if (newScreen != null) {
      // If we have a new screen, try to match it against a Juklear replacement
      currentJuklearScreen = overwrittenScreens.get(newScreen);
    }

    if (currentJuklearScreen != null) {
      // Found a replacement, notify the screen that it is being opened and
      // add all top level components the screen is exposing
      currentJuklearScreen.open();
      currentJuklearScreen.topLevelComponents().forEach((c) -> {
        context.addTopLevel(c);
        currentScreenTopLevels.add(c);
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean shouldRender(Hook.ExecutionTime executionTime, RenderExecution execution) {
    if (currentJuklearScreen != null && executionTime == Hook.ExecutionTime.BEFORE) {
      // If we have an overwritten screen, cancel the vanilla rendering and
      // render only if we have not rendered already
      execution.getCancellation().cancel();
      return !hasRenderedThisFrame;
    }

    // If we don't have an overwritten screen, but have no rendered yet, do so now
    // TODO: This should probably happen post Minecraft render
    return currentJuklearScreen == null && !hasRenderedThisFrame && !standaloneJuklearComponents.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void render(RenderExecution execution) {
    if (currentJuklearScreen != null) {
      currentJuklearScreen.preNuklearRender();
    }

    for (JuklearMCComponent standaloneJuklearComponent : this.standaloneJuklearComponents) {
      JuklearTopLevelComponent juklearTopLevelComponent = standaloneJuklearComponent.topLevelComponent();

      if (!standaloneJuklearComponent.shouldRender()) {
        this.context.removeTopLevel(juklearTopLevelComponent);
      } else if (!this.context.getTopLevelComponents().contains(juklearTopLevelComponent)) {
        this.context.addTopLevel(juklearTopLevelComponent);
      }
    }

    context.draw(
        minecraftWindow.getFramebufferWidth(),
        minecraftWindow.getFramebufferHeight(),
        new JuklearVec2(juklear, scale, scale),
        // Leave it off! OpenGL does Antialiasing for us, it creates weird artifacts if juklear does too!
        JuklearAntialiasing.OFF);

    if (currentJuklearScreen != null) {
      currentJuklearScreen.postNuklearRender();
    }

    hasRenderedThisFrame = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void frameDone() {
    hasRenderedThisFrame = false;
    context.processEvents();
  }
}
