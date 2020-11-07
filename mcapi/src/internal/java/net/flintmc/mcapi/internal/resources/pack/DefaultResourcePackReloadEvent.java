package net.flintmc.mcapi.internal.resources.pack;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.pack.ResourcePackReloadEvent;

/** Default implementation of the {@link ResourcePackReloadEvent}. */
@Implement(ResourcePackReloadEvent.class)
@Singleton
public class DefaultResourcePackReloadEvent implements ResourcePackReloadEvent {}
