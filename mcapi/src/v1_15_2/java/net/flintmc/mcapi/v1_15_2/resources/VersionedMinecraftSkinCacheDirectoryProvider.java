package net.flintmc.mcapi.v1_15_2.resources;

import java.io.File;
import javax.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.internal.resources.MinecraftSkinCacheDirectoryProvider;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(MinecraftSkinCacheDirectoryProvider.class)
public class VersionedMinecraftSkinCacheDirectoryProvider implements
    MinecraftSkinCacheDirectoryProvider {

  @Override
  public File getSkinCacheDirectory() {
    return ((SkinManagerShadow) Minecraft.getInstance().getSkinManager()).getSkinCacheDirectory();
  }
}
