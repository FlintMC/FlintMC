/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.util.session.internal.launcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import javassist.CtClass;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.util.session.launcher.LauncherProfile;
import net.flintmc.util.session.launcher.LauncherProfileResolver;
import net.flintmc.util.session.launcher.LauncherProfiles;
import net.flintmc.util.session.launcher.serializer.LauncherProfileSerializer;

public class DefaultLauncherProfileResolver implements LauncherProfileResolver {

  private static final String AUTHENTICATION_DATABASE = "authenticationDatabase";
  private static final String LAUNCHER_VERSION = "launcherVersion";
  private static final String FORMAT = "profilesFormat";

  private final LauncherProfiles.Factory profilesFactory;
  private final Gson gson;
  private final Supplier<Path> launcherProfilesPathSupplier;

  private final Map<Integer, CtClass> serializerClasses;
  private final Map<Integer, LauncherProfileSerializer> serializers;

  protected DefaultLauncherProfileResolver(
      LauncherProfiles.Factory profilesFactory, Supplier<Path> minecraftDirSupplier) {
    this.profilesFactory = profilesFactory;
    this.launcherProfilesPathSupplier =
        () -> minecraftDirSupplier.get().resolve("launcher_profiles.json");
    this.gson = new GsonBuilder().setPrettyPrinting().create();
    this.serializerClasses = new HashMap<>();
    this.serializers = new HashMap<>();
  }

  @Override
  public void registerSerializer(int version, CtClass serializerClass) {
    if (this.serializerClasses.containsKey(version)) {
      throw new IllegalArgumentException(
          "A serializer for the version " + version + " is already registered");
    }

    this.serializerClasses.put(version, serializerClass);
  }

  @Override
  public LauncherProfileSerializer getSerializer(int version) {
    if (!this.serializers.containsKey(version)) {
      this.serializers.put(
          version,
          InjectionHolder.getInjectedInstance(CtResolver.get(serializerClasses.get(version))));
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
    if (!launcherProfiles.has(LAUNCHER_VERSION)
        || !launcherProfiles.get(LAUNCHER_VERSION).isJsonObject()) {
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
      LauncherProfileSerializer serializer =
          this.getSerializer(this.getVersion(launcherProfiles, this.getHighestSerializerVersion()));
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

      return this.profilesFactory.create(
          clientToken,
          this.getVersion(launcherProfiles, this.getHighestSerializerVersion()),
          profiles);
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
      throw new IllegalStateException(
          "Cannot serialize profiles because there is no serializer registered for version "
              + version);
    }

    JsonObject authData =
        object.has(AUTHENTICATION_DATABASE)
            ? object.get(AUTHENTICATION_DATABASE).getAsJsonObject()
            : new JsonObject();

    serializer.updateAuthData(profiles.getProfiles(), authData);

    object.add(AUTHENTICATION_DATABASE, authData);
    object.addProperty("clientToken", profiles.getClientToken());
  }
}
