package net.labyfy.component.gui.adapter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.Collection;

public class GuiAdapter {

  private final Collection<GuiComponent> guiComponents;
  private final Object screen;

  @AssistedInject
  private GuiAdapter(@Assisted("screen") Object screen) {
    this.screen = screen;
    this.guiComponents = Sets.newConcurrentHashSet();
  }

  @AssistedFactory(GuiAdapter.class)
  public interface Factory {
    GuiAdapter create(@Assisted("screen") Object screen);
  }

  public GuiAdapter addComponent(GuiComponent component) {
    Preconditions.checkNotNull(component);
    this.guiComponents.add(component);
    return this;
  }

  /**
   * @param <T> type to cast
   * @return the screen of this {@link GuiAdapter} implicitly casted unsafe to T. The cast can be
   *     used in version implementations only, because the core does not know any minecraft classes.
   */
  public <T> T getScreen() {
    return (T) screen;
  }
}
