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

package net.flintmc.mcapi.v1_15_2.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;

/**
 * 1.15.2 implementation of the {@link ResourceLocationProvider}
 */
@Singleton
@Implement(value = ResourceLocationProvider.class, version = "1.15.2")
public class VersionedResourceLocationProvider implements ResourceLocationProvider {

  private final VersionedResourceLocation.Factory resourceLocationFactory;

  @Inject
  private VersionedResourceLocationProvider(ResourceLocation.Factory resourceLocationFactory) {
    this.resourceLocationFactory = resourceLocationFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation fromMinecraft(Object handle) {
    if (!(handle instanceof net.minecraft.util.ResourceLocation)) {
      throw new IllegalArgumentException(
          "Object must be an instance of " + net.minecraft.util.ResourceLocation.class.getName());
    }

    net.minecraft.util.ResourceLocation location = (net.minecraft.util.ResourceLocation) handle;
    return this.get(location.getNamespace(), location.getPath());
  }

  /**
   * {@inheritDoc}
   */
  public ResourceLocation get(String path) {
    return this.resourceLocationFactory.create(path);
  }

  /**
   * {@inheritDoc}
   */
  public ResourceLocation get(String nameSpace, String path) {
    return this.resourceLocationFactory.create(nameSpace, path);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<ResourceLocation> getRecursive(ResourceLocation resourceLocation)
      throws IOException {
    return Minecraft.getInstance()
        .getResourceManager()
        .getAllResources(resourceLocation.getHandle())
        .stream()
        .map(IResource::getLocation)
        .map(this::fromMinecraft)
        .collect(Collectors.toSet());
  }

  /**
   * {@inheritDoc}
   */
  public Collection<ResourceLocation> getLoaded(String namespace) {
    return this.getLoaded(namespace, s -> true);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<ResourceLocation> getLoaded(String namespace, Predicate<String> predicate) {
    return Minecraft.getInstance()
        .getResourceManager()
        .getAllResourceLocations(namespace, predicate)
        .stream()
        .map(this::fromMinecraft)
        .collect(Collectors.toSet());
  }
}
