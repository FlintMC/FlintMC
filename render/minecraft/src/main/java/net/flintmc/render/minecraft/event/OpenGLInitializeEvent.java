package net.flintmc.render.minecraft.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

/**
 * This event will be fired during Minecraft initialization, when OpenGL is initialized. It will be
 * fired in both {@link * Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases on the
 * Minecraft main thread.
 */
public interface OpenGLInitializeEvent extends Event {}
