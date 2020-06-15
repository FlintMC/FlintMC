package net.labyfy.component.gui.juklearmc;

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
import net.labyfy.component.gui.juklearmc.menues.JuklearMCScreen;
import net.labyfy.component.gui.juklearmc.style.DefaultLabyModStyle;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;
import net.labyfy.component.transform.hook.Hook;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
@Task(value = Tasks.POST_OPEN_GL_INITIALIZE, async = false)
public class JuklearMC implements GuiInputEventProcessor, GuiComponent {
  private final Juklear juklear;
  private final GuiController controller;
  private final Map<ScreenName, JuklearMCScreen> overwrittenScreens;
  private final List<JuklearTopLevelComponent> currentScreenTopLevels;
  private final List<Runnable> initializeTasks;

  private JuklearMCScreen currentJuklearScreen;

  private JuklearContext context;
  private JuklearFont defaultFont;
  private JuklearInput input;
  private MinecraftWindow minecraftWindow;

  private boolean hasRenderedThisFrame;

  private double mouseX;
  private double mouseY;
  private boolean leftMouseClicked;
  private boolean middleMouseClicked;
  private boolean rightMouseClicked;

  @Inject
  private JuklearMC(JuklearMCVersionedProvider versionedProvider, GuiController controller) throws IOException {
    JuklearNatives.setupWithTemporaryFolder();
    juklear = Juklear.usingInternalGarbageCollection(versionedProvider.backend());
    this.controller = controller;
    this.overwrittenScreens = new HashMap<>();
    this.currentScreenTopLevels = new ArrayList<>();
    this.initializeTasks = new ArrayList<>();
  }

  @TaskBody
  public void initialize() throws JuklearInitializationException, IOException {
    juklear.init();

    JuklearFontAtlas fontAtlas = juklear.defaultFontAtlas();
    JuklearFontAtlasEditor editor = fontAtlas.begin();
    defaultFont = editor.addFromURL(getClass().getResource("/assets/labymod/fonts/minecraft.ttf"), 16);
    editor.end();

    context = juklear.defaultContext(defaultFont);
    DefaultLabyModStyle.apply(context);
    controller.registerInputProcessor(this);
    controller.registerComponent(this);
    minecraftWindow = InjectionHolder.getInjectedInstance(MinecraftWindow.class);

    initializeTasks.forEach(Runnable::run);
  }

  public void overwriteScreen(ScreenName screen, JuklearMCScreen overwrite) {
    this.overwrittenScreens.put(screen, overwrite);
  }

  public void onInitialize(Runnable task) {
    this.initializeTasks.add(task);
  }

  public JuklearContext getContext() {
    return context;
  }

  @Override
  public void beginInput() {
    int drawWidth = minecraftWindow.getFramebufferWidth();
    int drawHeight = minecraftWindow.getFramebufferHeight();

    if (currentJuklearScreen != null) {
      currentJuklearScreen.updateSize(drawWidth, drawHeight);
    }

    this.input = context.beginInput();
  }

  @Override
  public boolean process(GuiInputEvent event) {
    if(currentJuklearScreen == null) {
      return false;
    }

    if (event instanceof CursorPosChanged) {
      mouseX = ((CursorPosChanged) event).getX();
      mouseY = ((CursorPosChanged) event).getY();
      input.motion((int) mouseX, (int) mouseY);
    } else if (event instanceof MouseClicked) {
      int button = ((MouseClicked) event).getValue();
      switch (button) {
        case MouseClicked.LEFT:
          leftMouseClicked = true;
          input.button(JuklearMouseButton.LEFT, (int) mouseX, (int) mouseY, true);
          break;

        case MouseClicked.RIGHT:
          rightMouseClicked = true;
          input.button(JuklearMouseButton.RIGHT, (int) mouseX, (int) mouseY, true);
          break;

        case MouseClicked.MIDDLE:
          middleMouseClicked = true;
          input.button(JuklearMouseButton.MIDDLE, (int) mouseY, (int) mouseY, true);
          break;
      }
    } else if (event instanceof MouseScrolled) {
      input.scroll((float) ((MouseScrolled) event).getXOffset(), (float) ((MouseScrolled) event).getYOffset());
    } else if (event instanceof UnicodeTyped) {
      input.unicode(((UnicodeTyped) event).getValue());
    }
    return true;
  }

  @Override
  public void endInput() {
    if(!leftMouseClicked) {
      input.button(JuklearMouseButton.LEFT, (int) mouseX, (int) mouseY, false);
    } else {
      leftMouseClicked = false;
    }

    if(!rightMouseClicked) {
      input.button(JuklearMouseButton.RIGHT, (int) mouseX, (int) mouseY, false);
    } else {
      leftMouseClicked = false;
    }

    if(!middleMouseClicked) {
      input.button(JuklearMouseButton.MIDDLE, (int) mouseX, (int) mouseY, false);
    } else {
      leftMouseClicked = false;
    }

    this.input.end();
    this.input = null;
  }

  @Override
  public void screenChanged(ScreenName newScreen) {
    if (!currentScreenTopLevels.isEmpty()) {
      currentScreenTopLevels.forEach(context::removeTopLevel);
      currentScreenTopLevels.clear();
    }

    if (currentJuklearScreen != null) {
      currentJuklearScreen.close();
    }

    if (newScreen != null) {
      currentJuklearScreen = overwrittenScreens.get(newScreen);
    }

    if (currentJuklearScreen != null) {
      currentJuklearScreen.open();
      currentJuklearScreen.topLevelComponents().forEach((c) -> {
        context.addTopLevel(c);
        currentScreenTopLevels.add(c);
      });
    }
  }

  @Override
  public boolean shouldRender(Hook.ExecutionTime executionTime, RenderExecution execution) {
    if (currentJuklearScreen != null && executionTime == Hook.ExecutionTime.BEFORE) {
      execution.getCancellation().cancel();
      return !hasRenderedThisFrame;
    }

    return currentJuklearScreen == null && !hasRenderedThisFrame;
  }

  @Override
  public void render(RenderExecution execution) {
    if (currentJuklearScreen != null) {
      currentJuklearScreen.preNuklearRender();
    }

    context.draw(
        minecraftWindow.getFramebufferWidth(),
        minecraftWindow.getFramebufferHeight(),
        new JuklearVec2(juklear, 1, 1),
        // Leave it off! OpenGL does Antialiasing for us, it creates weird artifacts if juklear does too!
        JuklearAntialiasing.OFF);

    if (currentJuklearScreen != null) {
      currentJuklearScreen.postNuklearRender();
    }

    hasRenderedThisFrame = true;
  }

  @Override
  public void frameDone() {
    hasRenderedThisFrame = false;
    context.processEvents();
  }
}
