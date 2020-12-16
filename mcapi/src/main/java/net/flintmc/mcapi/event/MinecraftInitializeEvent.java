package net.flintmc.mcapi.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

/**
 * This event will be fired when Minecraft is initialized. It will be fired in both {@link
 * Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases.
 */
public interface MinecraftInitializeEvent extends Event {}
