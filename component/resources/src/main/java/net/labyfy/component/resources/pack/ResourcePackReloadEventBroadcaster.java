package net.labyfy.component.resources.pack;

/**
 * Used for signaling the {@link ResourcePackReloadEvent} to the event system.
 *
 * @deprecated The event system will change and this class will be removed
 */
@Deprecated
public interface ResourcePackReloadEventBroadcaster {
  /**
   * Sends a {@link ResourcePackReloadEvent} to the event system.
   */
  void broadcast();
}
