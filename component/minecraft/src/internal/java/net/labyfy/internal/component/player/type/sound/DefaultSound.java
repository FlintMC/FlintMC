package net.labyfy.internal.component.player.type.sound;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.type.sound.Sound;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;

/**
 * Default implementation of the {@link Sound}.
 */
@Implement(Sound.class)
public class DefaultSound implements Sound {

  private final ResourceLocation resourceLocation;

  @AssistedInject
  private DefaultSound(
          @Assisted("path") String path,
          ResourceLocationProvider resourceLocationProvider
  ) {
    this.resourceLocation = resourceLocationProvider.get(path);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getName() {
    return this.resourceLocation;
  }
}
