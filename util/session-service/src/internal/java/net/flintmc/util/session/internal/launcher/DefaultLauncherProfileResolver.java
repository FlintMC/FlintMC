package net.flintmc.util.session.internal.launcher;

import com.google.gson.*;
import javassist.CtClass;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.util.session.launcher.LauncherProfile;
import net.flintmc.util.session.launcher.LauncherProfileResolver;
import net.flintmc.util.session.launcher.serializer.LauncherProfileSerializer;
import net.flintmc.util.session.launcher.LauncherProfiles;
import net.flintmc.framework.stereotype.service.CtResolver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

public class DefaultLauncherProfileResolver implements LauncherProfileResolver {

  private static final String AUTHENTICATION_DATABASE = "authenticationDatabase";
  private static final String LAUNCHER_VERSION = "launcherVersion";
  private static final String FORMAT = "profilesFormat";

  private final LauncherProfiles.Factory profilesFactory;
  private final Gson gson;
  private final Supplier<Path> launcherProfilesPathSupplier;

  private final Map<Integer, CtClass> serializerClasses;
  private final Map<Integer, LauncherProfileSerializer> serializers;

  protected DefaultLauncherProfileResolver(LauncherProfiles.Factory profilesFactory, Supplier<Path> minecraftDirSupplier) {
    this.profilesFactory = profilesFactory;
    this.launcherProfilesPathSupplier = () -> minecraftDirSupplier.get().resolve("launcher_profiles.json");
    this.gson = new GsonBuilder().setPrettyPrinting().create();
    this.serializerClasses = new HashMap<>();
    this.serializers = new HashMap<>();
  }

  @Override
  public void registerSerializer(int version, CtClass serializerClass) {
    if (this.serializerClasses.containsKey(version)) {
      throw new IllegalArgumentException("A serializer for the version " + version + " is already registered");
    }

    this.serializerClasses.put(version, serializerClass);
  }

  @Override
  public LauncherProfileSerializer getSerializer(int version) {
    if(!this.serializers.containsKey(version)){
      this.serializers.put(version, InjectionHolder.getInjectedInstance(CtResolver.get(serializerClasses.get(version))));
    }
    return this.serializers.get(version);
  }

  @Override
  public int getHighestSerializerVersion() {
    int max = 0;
    for (Integer version : this.serializerClasses.keySet()) {
      if (version > max) {
        max = version;
      }
    }
    return max;
  }

  private int getVersion(JsonObject launcherProfiles, int def) {
    if (!launcherProfiles.has(LAUNCHER_VERSION) || !launcherProfiles.get(LAUNCHER_VERSION).isJsonObject()) {
      return def;
    }
    JsonObject version = launcherProfiles.get(LAUNCHER_VERSION).getAsJsonObject();
    if (!version.has(FORMAT) || !version.get(FORMAT).isJsonPrimitive()) {
      return def;
    }

    return version.get(FORMAT).getAsInt();
  }

  @Override
  public LauncherProfiles loadProfiles() throws IOException {
    Path path = this.launcherProfilesPathSupplier.get();

    if (Files.notExists(path)) {
      return null;
    }

    try (InputStream inputStream = Files.newInputStream(path);
         Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      JsonElement element = JsonParser.parseReader(reader);
      if (!element.isJsonObject()) {
        // invalid file
        return null;
      }
      JsonObject launcherProfiles = element.getAsJsonObject();
      LauncherProfileSerializer serializer = this.getSerializer(this.getVersion(launcherProfiles, this.getHighestSerializerVersion()));
      if (serializer == null) {
        // unknown version
        return null;
      }

      String clientToken = UUID.randomUUID().toString().replace("-", "");
      Collection<LauncherProfile> profiles = new HashSet<>();

      JsonElement authenticationDatabase = launcherProfiles.get(AUTHENTICATION_DATABASE);
      if (authenticationDatabase.isJsonObject()) {
        // read the profiles if they exist
        profiles.addAll(serializer.readProfiles(authenticationDatabase.getAsJsonObject()).values());
      }

      JsonElement clientTokenElement = launcherProfiles.get("clientToken");
      if (clientTokenElement.isJsonPrimitive()) {
        // read the client token if it exists
        clientToken = clientTokenElement.getAsString();
      }

      return this.profilesFactory.create(clientToken, this.getVersion(launcherProfiles, this.getHighestSerializerVersion()), profiles);
    }
  }

  @Override
  public void storeProfiles(LauncherProfiles profiles) throws IOException {
    Path path = this.launcherProfilesPathSupplier.get();
    JsonObject object = new JsonObject();

    if (Files.exists(path)) {
      try (InputStream inputStream = Files.newInputStream(path);
           Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
        JsonElement element = JsonParser.parseReader(reader);
        if (element.isJsonObject()) {
          object = element.getAsJsonObject();
        }
      }
    }

    this.fillLauncherProfiles(object, profiles);

    if (Files.notExists(path.getParent())) {
      Files.createDirectories(path.getParent());
    }

    try (OutputStream outputStream = Files.newOutputStream(path);
         Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
      this.gson.toJson(object, writer);
    }
  }

  private void fillLauncherProfiles(JsonObject object, LauncherProfiles profiles) {
    int version = this.getVersion(object, profiles.getPreferredVersion());
    LauncherProfileSerializer serializer = this.getSerializer(version);
    if (serializer == null) {
      throw new IllegalStateException("Cannot serialize profiles because there is no serializer registered for version " + version);
    }

    JsonObject authData = object.has(AUTHENTICATION_DATABASE)
            ? object.get(AUTHENTICATION_DATABASE).getAsJsonObject()
            : new JsonObject();

    serializer.updateAuthData(profiles.getProfiles(), authData);

    object.add(AUTHENTICATION_DATABASE, authData);
    object.addProperty("clientToken", profiles.getClientToken());
  }

}
