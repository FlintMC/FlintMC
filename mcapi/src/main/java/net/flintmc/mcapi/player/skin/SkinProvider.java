package net.flintmc.mcapi.player.skin;

import java.util.UUID;
import java.util.function.Consumer;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationWatcher;

public interface SkinProvider {

  ResourceLocationWatcher loadSkin(GameProfile gameProfile);

  ResourceLocationWatcher loadHeadSkin(GameProfile gameProfile);

  ResourceLocationWatcher loadSkin(GameProfile gameProfile, Consumer<ResourceLocation> updateCallback);

  ResourceLocationWatcher loadHeadSkin(GameProfile gameProfile, Consumer<ResourceLocation> updateCallback);

}
