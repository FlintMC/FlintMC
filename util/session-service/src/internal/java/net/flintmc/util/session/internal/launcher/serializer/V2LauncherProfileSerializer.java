package net.flintmc.util.session.internal.launcher.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.mojang.util.UUIDTypeAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.util.session.launcher.LauncherProfile;
import net.flintmc.util.session.launcher.serializer.LauncherProfileSerializer;
import net.flintmc.util.session.launcher.serializer.ProfileSerializerVersion;

/**
 * Serializer for version 2 of the launcher_profiles.json by Mojang.
 */
@ProfileSerializerVersion(2)
public class V2LauncherProfileSerializer implements LauncherProfileSerializer {

  private final LauncherProfile.Factory profileFactory;

  @Inject
  public V2LauncherProfileSerializer(LauncherProfile.Factory profileFactory) {
    this.profileFactory = profileFactory;
  }

  @Override
  public void updateAuthData(Collection<LauncherProfile> profiles, JsonObject authData) {
    for (LauncherProfile profile : profiles) {
      JsonObject profileObject =
          authData.has(profile.getProfileId())
              ? authData.get(profile.getProfileId()).getAsJsonObject()
              : new JsonObject();
      JsonObject gameProfiles =
          profileObject.has("profiles")
              ? profileObject.get("profiles").getAsJsonObject()
              : new JsonObject();

      for (GameProfile gameProfile : profile.getProfiles()) {
        JsonObject gameProfileObject = new JsonObject();
        gameProfileObject.addProperty("displayName", gameProfile.getName());
        gameProfiles.add(gameProfile.getUniqueId().toString().replace("-", ""), gameProfileObject);
      }

      profileObject.addProperty("accessToken", profile.getAccessToken());
      profileObject.add("profiles", gameProfiles);

      authData.add(profile.getProfileId(), profileObject);
    }
  }

  @Override
  public Map<String, LauncherProfile> readProfiles(JsonObject authData) {
    Map<String, LauncherProfile> result = new HashMap<>();

    for (Map.Entry<String, JsonElement> entry : authData.entrySet()) {
      JsonElement value = entry.getValue();
      if (!value.isJsonObject()) {
        continue;
      }
      JsonObject object = value.getAsJsonObject();
      JsonElement accessToken = object.get("accessToken");
      JsonElement profiles = object.get("profiles");
      if (!accessToken.isJsonPrimitive() || !profiles.isJsonObject()) {
        continue;
      }

      Collection<GameProfile> gameProfiles = new ArrayList<>();
      for (Map.Entry<String, JsonElement> profileEntry : profiles.getAsJsonObject().entrySet()) {
        if (!profileEntry.getValue().isJsonObject()) {
          continue;
        }
        String id = profileEntry.getKey();
        JsonElement displayName = profileEntry.getValue().getAsJsonObject().get("displayName");
        if (!displayName.isJsonPrimitive()) {
          continue;
        }

        try {
          gameProfiles.add(
              InjectionHolder.getInjectedInstance(GameProfile.Builder.class)
                  .setUniqueId(UUIDTypeAdapter.fromString(id))
                  .setName(displayName.getAsString())
                  .build());
        } catch (IllegalArgumentException ignored) {
          // invalid UUID provided in the file
        }
      }

      result.put(
          entry.getKey(),
          this.profileFactory.create(
              entry.getKey(), accessToken.getAsString(), gameProfiles.toArray(new GameProfile[0])));
    }

    return result;
  }
}
