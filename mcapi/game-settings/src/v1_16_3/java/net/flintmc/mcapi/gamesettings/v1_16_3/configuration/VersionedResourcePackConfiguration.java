package net.flintmc.mcapi.gamesettings.v1_16_3.configuration;

import com.google.inject.Singleton;
import net.flintmc.mcapi.gamesettings.configuration.ResourcePackConfiguration;
import net.flintmc.framework.inject.implement.Implement;
import net.minecraft.client.Minecraft;

import java.util.List;

/**
 * 1.16.3 implementation of {@link ResourcePackConfiguration}.
 */
@Singleton
@Implement(value = ResourcePackConfiguration.class, version = "1.16.3")
public class VersionedResourcePackConfiguration implements ResourcePackConfiguration {

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getResourcePacks() {
    return Minecraft.getInstance().gameSettings.resourcePacks;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setResourcePacks(List<String> resourcePacks) {
    Minecraft.getInstance().gameSettings.resourcePacks = resourcePacks;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getIncompatibleResourcePacks() {
    return Minecraft.getInstance().gameSettings.incompatibleResourcePacks;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setIncompatibleResourcePacks(List<String> incompatibleResourcePacks) {
    Minecraft.getInstance().gameSettings.incompatibleResourcePacks = incompatibleResourcePacks;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

}
