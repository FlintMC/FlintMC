package net.flintmc.framework.config.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import javax.annotation.Nullable;

/**
 * This event will be fired whenever the value in a {@link ConfigObjectReference} is being updated via {@link
 * ConfigObjectReference#setValue(Object)}. It will not be fired if the value is updated via a setter in
 * the config itself.
 * <p>
 * This event will be fired in both the {@link Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases.
 *
 * @see Subscribe
 */
public interface ConfigValueUpdateEvent extends Event {

  /**
   * Retrieves the reference where the value has been updated.
   *
   * @return The non-null reference where the value has been updated
   */
  ConfigObjectReference getReference();

  /**
   * Retrieves the value that was set in the config before it has been updated.
   *
   * @return The nullable value from before the update
   */
  Object getPreviousValue();

  /**
   * Retrieves the value that is now set in the config.
   *
   * @return The nullable value from after the update
   */
  Object getNewValue();

  /**
   * Factory for the {@link ConfigValueUpdateEvent}.
   */
  @AssistedFactory(ConfigValueUpdateEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ConfigValueUpdateEvent} with the given values.
     *
     * @param reference     The non-null reference where the value has been updated
     * @param previousValue The nullable value from before the update
     * @param newValue      The nullable value from after the update
     * @return The new non-null {@link ConfigValueUpdateEvent}
     */
    ConfigValueUpdateEvent create(@Assisted ConfigObjectReference reference,
                                  @Assisted("previousValue") @Nullable Object previousValue,
                                  @Assisted("newValue") @Nullable Object newValue);

  }

}
