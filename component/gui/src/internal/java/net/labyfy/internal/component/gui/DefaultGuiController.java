package net.labyfy.internal.component.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gui.GuiController;
import net.labyfy.component.gui.InputInterceptor;
import net.labyfy.component.gui.RenderExecution;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.gui.event.GuiInputEvent;
import net.labyfy.component.gui.event.GuiInputEventProcessor;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.gui.screen.ScreenNameMapper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.hook.Hook;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link GuiController}. Provides most functionality which will ever be required.
 */
@Singleton
@Implement(GuiController.class)
public class DefaultGuiController implements GuiController {
  private final ScreenNameMapper nameMapper;
  private final ClassMappingProvider classMappingProvider;
  private final InputInterceptor inputInterceptor;
  private final List<GuiInputEventProcessor> inputEventProcessors;
  private final List<GuiComponent> components;

  private ScreenName currentScreen;
  private boolean inputActive;

  @Inject
  private DefaultGuiController(
      ScreenNameMapper nameMapper, ClassMappingProvider classMappingProvider, InputInterceptor inputInterceptor) {
    this.nameMapper = nameMapper;
    this.classMappingProvider = classMappingProvider;
    this.inputInterceptor = inputInterceptor;
    this.inputEventProcessors = new ArrayList<>();
    this.components = new ArrayList<>();
  }

  /**
   * Called from injected code, every time the render method of a screen is.
   *
   * @param executionTime The time the hook is called
   * @param execution     The current execution, used for cancellation and such
   */
  public void screenRenderCalled(Hook.ExecutionTime executionTime, RenderExecution execution) {
    for (GuiComponent component : components) {
      if (component.shouldRender(executionTime, execution)) {
        component.render(execution);
      }
    }
  }

  /**
   * Called from injected code, every time input events occur.
   *
   * @param event The input event that occurred
   * @return Wether the event has been captured and should not be passed on
   */
  public boolean doInput(GuiInputEvent event) {
    if (!inputActive) {
      throw new IllegalStateException("Input is not active");
    }

    boolean capture = false;
    for (GuiInputEventProcessor processor : inputEventProcessors) {
      if (processor.process(event)) {
        capture = true;
      }
    }

    return capture;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerInputProcessor(GuiInputEventProcessor processor) {
    this.inputEventProcessors.add(processor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeInputProcessor(GuiInputEventProcessor processor) {
    return this.inputEventProcessors.remove(processor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerComponent(GuiComponent component) {
    component.screenChanged(currentScreen);
    this.components.add(component);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeComponent(GuiComponent component) {
    return this.components.remove(component);
  }

  /**
   * Called from injected code, prepares the controller for input.
   *
   * @throws IllegalStateException If the input is active already
   */
  public void beginInput() {
    if (inputActive) {
      throw new IllegalStateException("Input is active already");
    }

    inputActive = true;
    this.inputEventProcessors.forEach(GuiInputEventProcessor::beginInput);
  }

  /**
   * Called from injected code, prepares the controller for input if is not prepared already.
   */
  public void safeBeginInput() {
    if (!inputActive) {
      beginInput();
    }
  }

  /**
   * Called from injected code, finalizes the controllers input state.
   *
   * @throws IllegalStateException If the controller has not been prepared for input
   */
  public void endInput() {
    if (!inputActive) {
      throw new IllegalStateException("Input is not active");
    }

    inputActive = false;
    this.inputEventProcessors.forEach(GuiInputEventProcessor::endInput);
  }

  /**
   * Called from injected code, finalizes the controllers input state if the controller has been prepared for input.
   */
  public void safeEndInput() {
    if (inputActive) {
      endInput();
    }
  }

  /**
   * Called from injected code, marks the current frame as done.
   */
  public void endFrame() {
    components.forEach(GuiComponent::frameDone);
  }

  /**
   * Called from injected code, every time the screen changes.
   *
   * @param newScreen The instance of the new screen, may be null
   */
  public void screenChanged(Object newScreen) {
    if (newScreen != null) {
      // Deobfuscate the name of the screen class to map it back to a ScreenName
      String mappedName = classMappingProvider.get(newScreen.getClass().getName()).getDeobfuscatedName();
      currentScreen = nameMapper.fromClass(mappedName);
      if (currentScreen == null) {
        // If we don't know the screen, mark it as unknown
        currentScreen = ScreenName.unknown(newScreen.getClass().getName());
      }
    }

    for (GuiComponent component : components) {
      component.screenChanged(currentScreen);
    }

    updateMousePosition();
  }

  /**
   * Invokes the mouse position callback in the GUI interceptor to prevent outdated positions
   */
  public void updateMousePosition() {
    boolean shouldControlInput = !inputActive;

    if (shouldControlInput) {
      beginInput();
    }

    inputInterceptor.signalCurrentMousePosition();

    if (shouldControlInput) {
      endInput();
    }
  }
}
