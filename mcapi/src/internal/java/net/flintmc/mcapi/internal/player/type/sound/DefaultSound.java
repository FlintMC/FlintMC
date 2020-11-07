package net.flintmc.mcapi.internal.player.type.sound;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationProvider;

/** Default implementation of the {@link Sound}. */
@Implement(Sound.class)
public class DefaultSound implements Sound {

  private final ResourceLocation resourceLocation;

  @AssistedInject
  private DefaultSound(
      @Assisted("path") String path, ResourceLocationProvider resourceLocationProvider) {
    this.resourceLocation = resourceLocationProvider.get(path);
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getName() {
    return this.resourceLocation;
  }
}
