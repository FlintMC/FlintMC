package net.flintmc.mcapi.v1_15_2.resources.pack;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.pack.ResourcePack;
import net.flintmc.mcapi.resources.pack.ResourcePackProvider;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.stream.Collectors;

/** 1.15.2 implementation of the {@link ResourcePackProvider} */
@Singleton
@Implement(value = ResourcePackProvider.class, version = "1.15.2")
public class VersionedResourcePackProvider implements ResourcePackProvider {

  /** {@inheritDoc} */
  public List<ResourcePack> getEnabled() {
    return Minecraft.getInstance().getResourcePackList().getEnabledPacks().stream()
        .map(VersionedResourcePack::new)
        .collect(Collectors.toList());
  }

  /** {@inheritDoc} */
  @Override
  public List<ResourcePack> getAvailable() {
    return Minecraft.getInstance().getResourcePackList().getAllPacks().stream()
        .map(VersionedResourcePack::new)
        .collect(Collectors.toList());
  }
}
