package net.labyfy.component.gui;

import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.gui.event.GuiInputEvent;
import net.labyfy.component.gui.event.GuiInputEventProcessor;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.gui.screen.ScreenNameMapper;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.hook.Hook;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class GuiController {
  private final ScreenNameMapper nameMapper;
  private final ClassMappingProvider classMappingProvider;
  private final List<GuiInputEventProcessor> inputEventProcessors;
  private final List<GuiComponent> components;

  private ScreenName currentScreen;
  private boolean inputActive;

  @Inject
  private GuiController(ScreenNameMapper nameMapper, ClassMappingProvider classMappingProvider) {
    this.nameMapper = nameMapper;
    this.classMappingProvider = classMappingProvider;
    this.inputEventProcessors = new ArrayList<>();
    this.components = new ArrayList<>();
  }

  public void screenRenderCalled(Hook.ExecutionTime executionTime, RenderExecution execution) {
    for(GuiComponent component : components) {
      if(component.shouldRender(executionTime, execution)) {
        component.render(execution);
      }
    }
  }

  public boolean doInput(GuiInputEvent event) {
    if(!inputActive) {
      throw new IllegalStateException("Input is not active");
    }

    boolean capture = false;
    for(GuiInputEventProcessor processor : inputEventProcessors) {
      if(processor.process(event)) {
        capture = true;
      }
    }

    return capture;
  }

  public void registerInputProcessor(GuiInputEventProcessor processor) {
    this.inputEventProcessors.add(processor);
  }

  public boolean removeInputProcessor(GuiInputEventProcessor processor) {
    return this.inputEventProcessors.remove(processor);
  }

  public void registerComponent(GuiComponent component) {
    component.screenChanged(currentScreen);
    this.components.add(component);
  }

  public boolean removeComponent(GuiComponent component) {
    return this.components.remove(component);
  }

  public void beginInput() {
    if(inputActive) {
      throw new IllegalStateException("Input is active already");
    }

    inputActive = true;
    this.inputEventProcessors.forEach(GuiInputEventProcessor::beginInput);
  }

  public void endInput() {
    if(!inputActive) {
      throw new IllegalStateException("Input is not active");
    }

    inputActive = false;
    this.inputEventProcessors.forEach(GuiInputEventProcessor::endInput);
  }

  public void endFrame() {
    components.forEach(GuiComponent::frameDone);
  }

  public void screenChanged(Object newScreen) {
    if(newScreen != null) {
      String mappedName = classMappingProvider.get(newScreen.getClass().getName()).getName();
      currentScreen = nameMapper.fromClass(mappedName);
      if (currentScreen == null) {
        currentScreen = ScreenName.unknown(newScreen.getClass().getName());
      }
    }

    for(GuiComponent component : components) {
      component.screenChanged(currentScreen);
    }
  }

  public void inputOnlyIterationDone() {
    this.inputEventProcessors.forEach(GuiInputEventProcessor::inputOnlyIterationDone);
  }
}
