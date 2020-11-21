package net.flintmc.util.mojang.internal.profile;

import com.google.gson.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.util.UUIDTypeAdapter;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.gameprofile.property.Property;
import net.flintmc.util.mojang.MojangRateLimitException;
import net.flintmc.util.mojang.internal.cache.FileCache;
import net.flintmc.util.mojang.profile.GameProfileResolver;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
  private static final String NAMES_TO_UUIDS_URL = "https://api.mojang.com/profiles/minecraft";

  private final Logger logger;
  private final Gson gson;
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
    this.gson = new Gson();
    this.cache = cache;
    this.profileFactory = profileFactory;
    this.propertyFactory = propertyFactory;
  }

  @Override
  public CompletableFuture<GameProfile> resolveProfile(UUID uniqueId) {
    GameProfile cached = this.cache.getCached(GameProfile.class, uniqueId);
    if (cached != null) {
      return CompletableFuture.completedFuture(cached);
    }

    return CompletableFuture.supplyAsync(
        () -> {
          GameProfile profile = null;

          try {
            profile = this.resolveProfileInternal(uniqueId);
          } catch (IOException exception) {
            this.logger.trace("Failed to resolve profile for UUID " + uniqueId, exception);
          }

          this.cache.cache(uniqueId, GameProfile.class, profile, VALID_TIME);

          return profile;
        });
  }

  private GameProfile resolveProfileInternal(UUID uniqueId) throws IOException {
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
    GameProfile cached =
        this.cache.getCached(
            GameProfile.class,
            profile ->
                profile.getName().equalsIgnoreCase(name)
                    && (!textures || !profile.getProperties().isEmpty()));
    if (cached != null) {
      return CompletableFuture.completedFuture(cached);
    }

    return CompletableFuture.supplyAsync(
        () -> {
          try {
            GameProfile profile = this.resolveProfileInternal(name, textures);

            if (profile != null) {
              this.cache.cache(profile.getUniqueId(), GameProfile.class, profile, VALID_TIME);
            }

            return profile;
          } catch (IOException exception) {
            this.logger.trace(
                "Failed to resolve profile for name "
                    + name
                    + " with"
                    + (textures ? "" : "out")
                    + " textures",
                exception);
          }

          return null;
        });
  }

  private GameProfile resolveProfileInternal(String name, boolean textures) throws IOException {
    HttpURLConnection connection =
        (HttpURLConnection) new URL(String.format(NAME_TO_UUID_URL, name)).openConnection();
    int code = connection.getResponseCode();
    if (code == 204) {
      return null;
    }
    if (code == 429) {
      throw MojangRateLimitException.INSTANCE;
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
        return this.resolveProfileInternal(uniqueId);
      }

      return this.profileFactory.create(uniqueId, object.get("name").getAsString());
    }
  }

  @Override
  public CompletableFuture<Collection<GameProfile>> resolveAll(String... names) {
    if (names.length == 0) {
      return CompletableFuture.completedFuture(Collections.emptyList());
    }

    Collection<GameProfile> result = new HashSet<>();
    Collection<String> request = new HashSet<>();

    for (String name : names) {
      GameProfile profile =
          this.cache.getCached(GameProfile.class, test -> test.getName().equalsIgnoreCase(name));
      if (profile != null) {
        result.add(profile);
      } else {
        request.add(name);
      }
    }

    if (request.isEmpty()) {
      return CompletableFuture.completedFuture(result);
    }

    return CompletableFuture.supplyAsync(
        () -> {
          try {
            Collection<GameProfile> resolved = this.resolveProfilesInternal(request);
            for (GameProfile profile : resolved) {
              this.cache.cache(profile.getUniqueId(), GameProfile.class, profile, VALID_TIME);
            }
            result.addAll(resolved);
          } catch (IOException exception) {
            this.logger.trace("Failed to resolve the profiles for the names " + request, exception);
          }

          return result;
        });
  }

  private Collection<GameProfile> resolveProfilesInternal(Collection<String> names)
      throws IOException {
    if (names.size() == 0) {
      return Collections.emptyList();
    }

    HttpURLConnection connection = (HttpURLConnection) new URL(NAMES_TO_UUIDS_URL).openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);

    try (OutputStream outputStream = connection.getOutputStream()) {
      outputStream.write(this.gson.toJson(names).getBytes());
    }

    Collection<GameProfile> profiles = new HashSet<>();

    try (InputStream inputStream = connection.getInputStream();
        Reader reader = new InputStreamReader(inputStream)) {
      JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();

      for (JsonElement element : array) {
        JsonObject object = element.getAsJsonObject();
        GameProfile profile =
            this.profileFactory.create(
                UUIDTypeAdapter.fromString(object.get("id").getAsString()),
                object.get("name").getAsString());
        profiles.add(profile);
      }
    }

    return profiles;
  }
}
