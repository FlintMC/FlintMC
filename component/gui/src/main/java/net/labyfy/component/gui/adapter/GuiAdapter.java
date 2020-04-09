package net.labyfy.component.gui.adapter;

import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.inject.assisted.AssistedFactory;

import javax.vecmath.Vector2d;
import java.util.Collection;
import java.util.LinkedHashSet;

public class GuiAdapter {

  private final Vector2d mousePosition;
  private final GuiAdapterProvider guiAdapterProvider;
  private final Collection<GuiComponent> guiComponents;
  private final Object screen;
  private GuiAdapter child;
  private float partialTick;

  @AssistedInject
  private GuiAdapter(GuiAdapterProvider guiAdapterProvider, @Assisted("screen") Object screen) {
    this.guiAdapterProvider = guiAdapterProvider;
    this.screen = screen;
    this.guiComponents = new LinkedHashSet<>();
    this.mousePosition = new Vector2d();
  }

  public void drawComponents() {
    this.guiComponents.forEach(component -> component.render(this.getChild()));
    if (this.child != null) this.child.drawComponents();
  }

  public void initComponents() {
    this.guiComponents.forEach(component -> component.init(this.getChild()));
    if (this.child != null) this.child.initComponents();
  }

  public GuiAdapter addComponent(GuiComponent component) {
    Preconditions.checkNotNull(component);
    this.guiComponents.add(component);
    return this;
  }

  public GuiAdapter getChild() {
    if (this.child == null) this.child = this.guiAdapterProvider.getChild(this);
    return this.child;
  }

  /**
   * @param <T> type to cast
   * @return the screen of this {@link GuiAdapter} implicitly casted unsafe to T. The cast can be
   *     used in version implementations only, because the core does not know any minecraft classes.
   */
  public <T> T getScreen() {
    return (T) screen;
  }

  public void updateMousePosition(int x, int y) {
    this.mousePosition.x = x;
    this.mousePosition.y = y;
  }

  public Vector2d getMousePosition() {
    return new Vector2d(mousePosition);
  }

  public void updatePartialTick(float partialTick) {
    this.partialTick = partialTick;
  }

  public float getPartialTick() {
    return partialTick;
  }

  public void reset() {
    this.guiComponents.clear();
  }

  @AssistedFactory(GuiAdapter.class)
  public interface Factory {
    GuiAdapter create(@Assisted("screen") Object screen);
  }
}
