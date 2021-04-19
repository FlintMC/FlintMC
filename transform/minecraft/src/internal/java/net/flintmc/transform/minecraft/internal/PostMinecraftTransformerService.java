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

package net.flintmc.transform.minecraft.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.Service.State;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.metaprogramming.AnnotationMeta;
import net.flintmc.transform.minecraft.MinecraftTransformer;

@Singleton
@Service(value = MinecraftTransformer.class, priority = -200000, state = State.AFTER_IMPLEMENT)
public class PostMinecraftTransformerService implements ServiceHandler<MinecraftTransformer> {

  private final MinecraftTransformerService service;

  @Inject
  private PostMinecraftTransformerService(MinecraftTransformerService service) {
    this.service = service;
  }

  @Override
  public void discover(AnnotationMeta<MinecraftTransformer> meta)
      throws ServiceNotFoundException {
    if (!meta.getAnnotation().implementations()) {
      // handled by the PreMinecraftTransformerService
      return;
    }

    this.service.handleAnnotation(meta);
  }
}
