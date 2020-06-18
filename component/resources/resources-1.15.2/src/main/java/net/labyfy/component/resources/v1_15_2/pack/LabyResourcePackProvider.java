package net.labyfy.component.resources.v1_15_2.pack;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.pack.ResourcePack;
import net.labyfy.component.resources.pack.ResourcePackProvider;
import net.minecraft.client.Minecraft;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
@Implement(value = ResourcePackProvider.class, version = "1.15.2")
public class LabyResourcePackProvider implements ResourcePackProvider {

  private final LabyResourcePack.Factory resourcePackFactory;

  @Inject
  private LabyResourcePackProvider(LabyResourcePack.Factory resourcePackFactory) {
    this.resourcePackFactory = resourcePackFactory;
  }

  public Collection<ResourcePack> getEnabled() {
    return Minecraft.getInstance().getResourcePackList().getEnabledPacks().stream()
        .map(this.resourcePackFactory::create)
        .collect(Collectors.toSet());
  }
}
