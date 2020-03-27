package net.labyfy.component.gui.adapter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class GuiAdapterProvider {

  private final GuiAdapter.Factory guiAdapterFactory;
  private final Map<Class<?>, GuiAdapter> adapters;
  private final Map<GuiAdapter, GuiAdapter> childAdapter;

  @Inject
  private GuiAdapterProvider(GuiAdapter.Factory guiAdapterFactory) {
    this.guiAdapterFactory = guiAdapterFactory;
    this.adapters = Maps.newConcurrentMap();
    this.childAdapter = Maps.newConcurrentMap();
  }

  public GuiAdapter getAdapter(Object screen) {
    Preconditions.checkNotNull(screen);
    if (!adapters.containsKey(screen.getClass())) this.createAdapter(screen);
    return this.adapters.get(screen.getClass());
  }

  public GuiAdapter getChild(GuiAdapter guiAdapter) {
    Preconditions.checkNotNull(guiAdapter);
    if (!this.childAdapter.containsKey(guiAdapter)) this.createChild(guiAdapter);
    return this.childAdapter.get(guiAdapter);
  }

  private void createAdapter(Object screen) {
    this.adapters.put(screen.getClass(), this.guiAdapterFactory.create(screen));
  }

  private void createChild(GuiAdapter adapter) {
    this.childAdapter.put(adapter, this.guiAdapterFactory.create(adapter.getScreen()));
  }
}
