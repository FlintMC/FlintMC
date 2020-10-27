package net.flintmc.util.session.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;
import net.flintmc.mcapi.player.serializer.gameprofile.GameProfileSerializer;
import net.flintmc.util.session.ProfileLoader;

import java.net.Proxy;
import java.util.UUID;

@Singleton
@Implement(ProfileLoader.class)
public class DefaultProfileLoader implements ProfileLoader {

  private final GameProfileSerializer<GameProfile> profileSerializer;
  private final MinecraftSessionService sessionService;

  @Inject
  private DefaultProfileLoader(GameProfileSerializer profileSerializer) {
    this.profileSerializer = profileSerializer;
    this.sessionService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString()).createMinecraftSessionService();
  }

  @Override
  public PropertyMap loadProfileProperties(UUID uniqueId) {
    return this.loadProfile(uniqueId).getProperties();
  }

  @Override
  public net.flintmc.mcapi.player.gameprofile.GameProfile loadProfile(UUID uniqueId) {
    GameProfile profile = new GameProfile(uniqueId, "Steve");
    this.sessionService.fillProfileProperties(profile, false);
    return this.profileSerializer.deserialize(profile);
  }
}
