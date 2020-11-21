package net.flintmc.util.mojang.profile;

import net.flintmc.mcapi.player.gameprofile.GameProfile;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/** Resolver for {@link GameProfile}s by Mojang with the Name or UUID including their textures. */
public interface GameProfileResolver {

  /**
   * Resolves the name and textures of a profile with the given UUID.
   *
   * <p>If there is no profile with the given UUID on Mojang or an error with Mojang occurred, the
   * future will contain {@code null}.
   *
   * @param uniqueId The non-null UUID to resolve the name and textures for
   * @return The non-null future with the profile containing the name and textures of the given UUID
   */
  CompletableFuture<GameProfile> resolveProfile(UUID uniqueId);

  /**
   * Resolves the name and if enabled textures of a profile with the given UUID.
   *
   * <p>If there is no profile with the given name on Mojang or an error with Mojang occurred, the
   * future will contain {@code null}.
   *
   * @param name The non-null case-insensitive name to resolve the UUID (and textures) for
   * @param textures {@code true} to also resolve the textures, {@code false} to only resolve the
   *     UUID
   * @return The non-null future with the profile containing the name and textures of the given UUID
   */
  CompletableFuture<GameProfile> resolveProfile(String name, boolean textures);

  /**
   * Resolves the UUIDs of multiple names. This doesn't resolve the textures of the profiles, if you
   * need to have them, consider using {@link #resolveProfile(String, boolean)}.
   *
   * <p>If there is no profile with one of the given names on Mojang or an error with Mojang
   * occurred, the collection will not contain it.
   *
   * @param names The non-null varargs of all non-null names that should be resolved, this array
   *     cannot be larger than 10 elements. Each name needs to be non-empty and with a max length of
   *     25 characters
   * @return The non-null future with the profiles, each containing the name and UUID that have been
   *     resolved
   */
  CompletableFuture<Collection<GameProfile>> resolveAll(String... names);
}
