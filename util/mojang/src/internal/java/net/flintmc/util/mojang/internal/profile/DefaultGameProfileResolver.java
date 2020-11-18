package net.flintmc.util.mojang.internal.profile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.util.UUIDTypeAdapter;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.gameprofile.property.Property;
import net.flintmc.util.mojang.internal.cache.FileCache;
import net.flintmc.util.mojang.internal.cache.UUIDMappedValue;
import net.flintmc.util.mojang.profile.GameProfileResolver;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Singleton
@Implement(GameProfileResolver.class)
public class DefaultGameProfileResolver implements GameProfileResolver {

  private static final long VALID_TIME = TimeUnit.MINUTES.toMillis(10);
  private static final String UUID_TO_NAME_URL =
      "https://sessionserver.mojang.com/session/minecraft/profile/%s";
  private static final String NAME_TO_UUID_URL =
      "https://api.mojang.com/users/profiles/minecraft/%s";

  private final Logger logger;
  private final FileCache cache;
  private final GameProfile.Factory profileFactory;
  private final Property.Factory propertyFactory;

  @Inject
  private DefaultGameProfileResolver(
      @InjectLogger Logger logger,
      FileCache cache,
      GameProfile.Factory profileFactory,
      Property.Factory propertyFactory) {
    this.logger = logger;
    this.cache = cache;
    this.profileFactory = profileFactory;
    this.propertyFactory = propertyFactory;
  }

  @Override
  public CompletableFuture<GameProfile> resolveProfile(UUID uniqueId) {
    return this.cache.get(
        uniqueId, GameProfile.class, () -> this.resolveProfileWithTextures(uniqueId), VALID_TIME);
  }

  private GameProfile resolveProfileWithTextures(UUID uniqueId) {
    try {
      HttpURLConnection connection =
          (HttpURLConnection)
              new URL(String.format(UUID_TO_NAME_URL, UUIDTypeAdapter.fromUUID(uniqueId)))
                  .openConnection();
      int code = connection.getResponseCode();
      if (code == 204) {
        return null;
      }
      if (code != 200) {
        throw new IllegalStateException(
            String.format(
                "An unknown error occurred while resolving profile with the UUID %s: %d",
                uniqueId, code));
      }

      try (InputStream inputStream = connection.getInputStream();
          Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
        JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();

        return this.parseProfile(object);
      }
    } catch (IOException exception) {
      this.logger.trace("Failed to resolve profile properties");
      return null;
    }
  }

  private GameProfile parseProfile(JsonObject object) {
    GameProfile profile =
        this.profileFactory.create(
            UUIDTypeAdapter.fromString(object.get("id").getAsString()),
            object.get("name").getAsString());

    for (JsonElement element : object.getAsJsonArray("properties")) {
      JsonObject jsonProperty = element.getAsJsonObject();
      String name = jsonProperty.get("name").getAsString();
      String value = jsonProperty.get("value").getAsString();
      String signature =
          jsonProperty.has("signature") ? jsonProperty.get("signature").getAsString() : null;
      Property property =
          signature != null
              ? this.propertyFactory.create(name, value, signature)
              : this.propertyFactory.create(name, value);

      profile.getProperties().put(name, property);
    }

    return profile;
  }

  @Override
  public CompletableFuture<GameProfile> resolveProfile(String name, boolean textures) {
    return this.cache.get(
        profile -> profile.getName().equalsIgnoreCase(name),
        GameProfile.class,
        () -> {
          try {
            HttpURLConnection connection =
                (HttpURLConnection) new URL(String.format(NAME_TO_UUID_URL, name)).openConnection();
            int code = connection.getResponseCode();
            if (code == 204) {
              return null;
            }
            if (code != 200) {
              throw new IllegalStateException(
                  String.format(
                      "An unknown error occurred while resolving profile with the name %s: %d",
                      name, code));
            }

            try (InputStream inputStream = connection.getInputStream();
                Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
              JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
              UUID uniqueId = UUIDTypeAdapter.fromString(object.get("id").getAsString());
              if (textures) {
                return new UUIDMappedValue<>(uniqueId, this.resolveProfileWithTextures(uniqueId));
              }

              GameProfile profile =
                  this.profileFactory.create(uniqueId, object.get("name").getAsString());
              return new UUIDMappedValue<>(uniqueId, profile);
            }
          } catch (IOException exception) {
            this.logger.trace("Failed to resolve profile properties");
            return null;
          }
        },
        VALID_TIME);
  }

  @Override
  public CompletableFuture<Collection<GameProfile>> resolveAll(String... names) {
    return null;
  }
}
