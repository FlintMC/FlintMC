package net.flintmc.util.session.internal.launcher.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.util.session.launcher.LauncherProfile;
import net.flintmc.util.session.launcher.serializer.LauncherProfileSerializer;
import net.flintmc.util.session.launcher.serializer.ProfileSerializerVersion;

/**
 * Serializer for version 1 of the launcher_profiles.json by Mojang.
 */
@ProfileSerializerVersion(1)
public class V1LauncherProfileSerializer implements LauncherProfileSerializer {

  private final LauncherProfile.Factory profileFactory;

  @Inject
  public V1LauncherProfileSerializer(LauncherProfile.Factory profileFactory) {
    this.profileFactory = profileFactory;
  }

  @Override
  public void updateAuthData(Collection<LauncherProfile> profiles, JsonObject authData) {

    for (LauncherProfile profile : profiles) {
      if (profile.getProfiles().length == 0) {
        continue;
      }

      String existingKey = this.findProfileKey(profile.getProfileId(), authData);
      JsonObject object =
          existingKey != null && authData.has(existingKey)
              ? authData.getAsJsonObject(existingKey)
              : new JsonObject();
      if (existingKey != null) {
        authData.remove(existingKey);
      }

      GameProfile gameProfile = profile.getProfiles()[0];
      object.addProperty("accessToken", profile.getAccessToken());
      object.addProperty("displayName", gameProfile.getName());
      object.addProperty("uuid", gameProfile.getUniqueId().toString());

      String newKey =
          existingKey != null ? existingKey : gameProfile.getUniqueId().toString().replace("-", "");
      authData.add(newKey, object);
    }
  }

  private String findProfileKey(String profileId, JsonObject authData) {
    for (Map.Entry<String, JsonElement> entry : authData.entrySet()) {
      if (profileId.equals(entry.getValue().getAsJsonObject().get("userid").getAsString())) {
        return entry.getKey();
      }
    }
    return null;
  }

  @Override
  public Map<String, LauncherProfile> readProfiles(JsonObject authData) {
    Map<String, LauncherProfile> result = new HashMap<>();

    for (Map.Entry<String, JsonElement> entry : authData.entrySet()) {
      if (!entry.getValue().isJsonObject()) {
        continue;
      }

      JsonObject object = entry.getValue().getAsJsonObject();

      String profileId = object.get("userid").getAsString();
      String accessToken = object.get("accessToken").getAsString();

      GameProfile profile;
      try {
        profile =
            InjectionHolder.getInjectedInstance(GameProfile.Builder.class)
                .setUniqueId(UUID.fromString(object.get("uuid").getAsString()))
                .setName(object.get("displayName").getAsString())
                .build();
      } catch (IllegalArgumentException ignored) {
        // invalid UUID provided in the file
        continue;
      }

      result.put(
          profileId,
          this.profileFactory.create(profileId, accessToken, new GameProfile[]{profile}));
    }

    return result;
  }
}
