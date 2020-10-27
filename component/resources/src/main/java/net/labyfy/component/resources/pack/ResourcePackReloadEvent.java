package net.labyfy.component.resources.pack;

import net.labyfy.component.eventbus.event.Event;

/**
 * This event will be fired whenever the resource packs have been reloaded, it only supports the
 * POST phase.
 */
public interface ResourcePackReloadEvent extends Event {}
