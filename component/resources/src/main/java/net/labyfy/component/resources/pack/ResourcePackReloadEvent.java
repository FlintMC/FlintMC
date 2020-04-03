package net.labyfy.component.resources.pack;

import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.assisted.AssistedFactory;

public class ResourcePackReloadEvent {

  @AssistedInject
  private ResourcePackReloadEvent() {}

  @AssistedFactory(ResourcePackReloadEvent.class)
  public interface Factory {
    ResourcePackReloadEvent create();
  }
}
