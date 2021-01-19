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

package net.flintmc.util.session.launcher;

import java.io.IOException;
import javassist.CtClass;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.event.MinecraftInitializeEvent;
import net.flintmc.util.session.launcher.serializer.LauncherProfileSerializer;
import net.flintmc.util.session.launcher.serializer.ProfileSerializerVersion;

/**
 * Contains the reader and writer for the launcher_profiles.json for the Minecraft launcher.
 */
public interface LauncherProfileResolver {

  /**
   * Registers a new serializer for the {@link LauncherProfiles} with the specified version. This
   * serializer will be called by {@link #loadProfiles()} and {@link #storeProfiles(LauncherProfiles)}
   * if the version in the {@link LauncherProfiles} matches the version of this serializer.
   *
   * @param version         The version of the serializer
   * @param serializerClass The non-null class of the serializer which should be called
   * @throws IllegalArgumentException If a serializer for the given version is already registered
   */
  void registerSerializer(int version, CtClass serializerClass);

  /**
   * Retrieves the serializer from the given version which has been registered via {@link
   * #registerSerializer(int, CtClass)} )} or the {@link ProfileSerializerVersion} annotation.
   *
   * @param version The version of the serializer
   * @return The serializer or {@code null}, if no serializer for the given version has been
   * registered
   */
  LauncherProfileSerializer getSerializer(int version);

  /**
   * Retrieves the highest version of all serializers that have been registered in this resolver.
   *
   * @return The highest version or {@code 0} if no serializer has been registered
   */
  int getHighestSerializerVersion();

  /**
   * Loads all profiles from the launcher_profiles.json by the Minecraft launcher using the
   * serializer with the version that is specified inside of this file. This method may not be
   * called before the {@link MinecraftInitializeEvent} has been fired in the {@link
   * Subscribe.Phase#POST} phase, otherwise it may throw unspecified exceptions.
   *
   * @return The launcher profiles or {@code null}, if an error occurred (e.g. if the file doesn't
   * exist, if there is no serializer for the specified version registered)
   * @throws IOException If an I/O error occurred
   */
  LauncherProfiles loadProfiles() throws IOException;

  /**
   * Stores all profiles in the given profiles object to the launcher_profiles.json by the Minecraft
   * launcher using the serializer with the version that is specified inside of the given object.
   * This method may not be called before the {@link MinecraftInitializeEvent} has been fired in the
   * {@link Subscribe.Phase#POST} phase, otherwise it may throw unspecified exceptions.
   *
   * @param profiles The non-null profiles to be written
   * @throws IOException           If an I/O error occurred
   * @throws IllegalStateException If there is no serializer registered for the specified version
   */
  void storeProfiles(LauncherProfiles profiles) throws IOException;
}
