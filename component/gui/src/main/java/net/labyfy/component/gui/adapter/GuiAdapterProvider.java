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

  @Inject
  private GuiAdapterProvider(GuiAdapter.Factory guiAdapterFactory) {
    this.guiAdapterFactory = guiAdapterFactory;
    this.adapters = Maps.newConcurrentMap();
  }

  public GuiAdapter getAdapter(Object screen) {
    Preconditions.checkNotNull(screen);
    if (!adapters.containsKey(screen.getClass())) this.createAdapter(screen);
    return this.adapters.get(screen.getClass());
  }

  private void createAdapter(Object screen) {
    adapters.put(screen.getClass(), guiAdapterFactory.create(screen));
  }
}
