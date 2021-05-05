package net.flintmc.mcapi.v1_15_2.player.skin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.mcapi.player.gameprofile.property.Property;
import net.flintmc.mcapi.player.skin.SkinProvider;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationWatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.SkinManager.ISkinAvailableCallback;
import net.minecraft.entity.player.PlayerEntity;

public class VersionedSkinLoadingTask implements ISkinAvailableCallback,
    ResourceLocationWatcher {

  private final ResourceLocation.Factory resourceLocationFactory;
  private final GameProfile gameProfile;
  private final Collection<Consumer<ResourceLocation>> loadCallbacks;
  private ResourceLocation currentLocation;
  private boolean loaded = false;

  @AssistedInject
  private VersionedSkinLoadingTask(
      ResourceLocation.Factory resourceLocationFactory,
      @Assisted net.flintmc.mcapi.player.gameprofile.GameProfile flintProfile) {
    this.resourceLocationFactory = resourceLocationFactory;
    this.gameProfile = convertProfile(flintProfile);
    this.loadCallbacks = new HashSet<>();
  }

  protected void load() {
    SkinManager skinManager = Minecraft.getInstance().getSkinManager();
    Map<Type, MinecraftProfileTexture> skinMap = skinManager
        .loadSkinFromCache(this.gameProfile);

    if (skinMap.containsKey(Type.SKIN)) {
      net.minecraft.util.ResourceLocation resourceLocation = Minecraft.getInstance()
          .getSkinManager().loadSkin(skinMap.get(Type.SKIN), Type.SKIN, this);
      this.currentLocation = convertResourceLocation(resourceLocation);
    } else {
      net.minecraft.util.ResourceLocation resourceLocation = DefaultPlayerSkin
          .getDefaultSkin(PlayerEntity.getUUID(this.gameProfile));
      this.currentLocation = convertResourceLocation(resourceLocation);
      skinManager.loadProfileTextures(gameProfile, this, false);
    }
  }

  private GameProfile convertProfile(
      net.flintmc.mcapi.player.gameprofile.GameProfile flintProfile) {
    GameProfile mojangProfile = new GameProfile(
        flintProfile.getUniqueId(), flintProfile.getName());

    for (Entry<String, Property> entry : flintProfile.getProperties().entries()) {
      mojangProfile.getProperties().put(entry.getKey(),
          new com.mojang.authlib.properties.Property(entry.getValue().getName(),
              entry.getValue().getValue(), entry.getValue().getSignature()));
    }

    return mojangProfile;
  }

  private ResourceLocation convertResourceLocation(
      net.minecraft.util.ResourceLocation resourceLocation) {
    return resourceLocationFactory
        .create(resourceLocation.getNamespace(), resourceLocation.getPath());
  }

  @Override
  public synchronized void onSkinTextureAvailable(Type type,
      net.minecraft.util.ResourceLocation resourceLocation,
      MinecraftProfileTexture p_onSkinTextureAvailable_3_) {
    if (type == Type.SKIN) {
      this.loaded = true;
      this.currentLocation = convertResourceLocation(resourceLocation);
      for (Consumer<ResourceLocation> loadCallback : this.loadCallbacks) {
        loadCallback.accept(this.currentLocation);
      }
      this.loadCallbacks.clear();
    }
  }

  @Override
  public synchronized ResourceLocation getCurrentLocation() {
    return this.currentLocation;
  }

  @Override
  public synchronized void registerLoadCallback(
      Consumer<ResourceLocation> loadCallback) {
    if (this.loaded) {
      loadCallback.accept(this.currentLocation);
      return;
    }
    this.loadCallbacks.add(loadCallback);
  }

  @Override
  public synchronized boolean isLoaded() {
    return this.loaded;
  }

  @AssistedFactory(VersionedSkinLoadingTask.class)
  public interface Factory {

    VersionedSkinLoadingTask create(
        @Assisted net.flintmc.mcapi.player.gameprofile.GameProfile gameProfilea);
  }
}
