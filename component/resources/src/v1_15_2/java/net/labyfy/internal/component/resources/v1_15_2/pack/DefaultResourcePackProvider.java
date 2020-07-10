package net.labyfy.internal.component.resources.v1_15_2.pack;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.pack.ResourcePack;
import net.labyfy.component.resources.pack.ResourcePackProvider;
import net.minecraft.client.Minecraft;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 1.15.2 implementation of the {@link ResourcePackProvider}
 */
@Singleton
@Implement(value = ResourcePackProvider.class, version = "1.15.2")
public class DefaultResourcePackProvider implements ResourcePackProvider {
  /**
   * {@inheritDoc}
   */
  public Collection<ResourcePack> getEnabled() {
    return Minecraft.getInstance().getResourcePackList().getEnabledPacks().stream()
        .map(DefaultResourcePack::new)
        .collect(Collectors.toSet());
  }
}
