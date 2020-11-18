package net.flintmc.util.mojang.profile;

import net.flintmc.mcapi.player.gameprofile.GameProfile;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface GameProfileResolver {

  CompletableFuture<GameProfile> resolveProfile(UUID uniqueId);

  CompletableFuture<GameProfile> resolveProfile(String name, boolean textures);

  // doesn't resolve textures, 0 < name.length < 25, case-insensitive
  CompletableFuture<Collection<GameProfile>> resolveAll(String... names);
}
