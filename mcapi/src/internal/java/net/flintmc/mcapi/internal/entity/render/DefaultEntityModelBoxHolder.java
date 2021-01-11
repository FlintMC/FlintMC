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

package net.flintmc.mcapi.internal.entity.render;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.render.EntityModelBoxHolder;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.render.model.internal.DefaultModelBoxHolder;

@Implement(EntityModelBoxHolder.class)
public class DefaultEntityModelBoxHolder
    extends DefaultModelBoxHolder<Entity, EntityRenderContext, DefaultEntityModelBoxHolder, Object>
    implements EntityModelBoxHolder {

  @AssistedInject
  protected DefaultEntityModelBoxHolder(
      @Assisted EntityRenderContext renderContext, @Assisted Object target) {
    super(renderContext, target);
  }
}
