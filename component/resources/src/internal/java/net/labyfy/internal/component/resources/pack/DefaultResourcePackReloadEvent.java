package net.labyfy.internal.component.resources.pack;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.pack.ResourcePackReloadEvent;

@Implement(ResourcePackReloadEvent.class)
@Singleton
public class DefaultResourcePackReloadEvent implements ResourcePackReloadEvent {
}
