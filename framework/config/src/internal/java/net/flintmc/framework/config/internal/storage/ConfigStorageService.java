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

package net.flintmc.framework.config.internal.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import net.flintmc.framework.config.storage.ConfigStorage;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.config.storage.StoragePriority;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.Identifier;

@Singleton
@Service(StoragePriority.class)
public class ConfigStorageService implements ServiceHandler<StoragePriority> {

  private final ConfigStorageProvider storageProvider;

  @Inject
  private ConfigStorageService(ConfigStorageProvider storageProvider) {
    this.storageProvider = storageProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(AnnotationMeta<StoragePriority> meta) {
    Identifier<CtClass> identifier = meta.getIdentifier();

    ConfigStorage storage =
        InjectionHolder.getInjectedInstance(CtResolver.get(identifier.getLocation()));
    this.storageProvider.registerStorage(storage);
  }
}
