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

package net.flintmc.mcapi.v1_16_4.entity.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityRenderNameEvent;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;

@Singleton
public class VersionedEntityRenderNameEventInjector {

  private final EntityMapper entityMapper;
  private final EventBus eventBus;
  private final EntityRenderNameEvent.Factory eventFactory;

  @Inject
  private VersionedEntityRenderNameEventInjector(
      EntityMapper entityMapper, EventBus eventBus, EntityRenderNameEvent.Factory eventFactory) {
    this.entityMapper = entityMapper;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }

  /**
   * Called from the {@link net.minecraft.client.renderer.entity.EntityRenderer} which is
   * transformed by the {@link VersionedEntityRenderNameEventInjectorTransformer}.
   */
  public void renderName(Object[] args, Matrix4f matrix, int y, boolean notSneaking) {
    Entity entity = this.entityMapper.fromAnyMinecraftEntity(args[0]);
    if (entity == null) {
      return;
    }

    int textBackgroundColor = 0;
    if (!notSneaking) {
      float opacity =
          net.minecraft.client.Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
      textBackgroundColor = (int) (opacity * 255.0F) << 24;
    }

    ITextComponent displayName = (ITextComponent) args[1];
    IRenderTypeBuffer buffer = (IRenderTypeBuffer) args[3];
    int packedLight = (int) args[4];

    EntityRenderNameEvent event =
        this.eventFactory.create(
            entity, displayName.getString(), matrix, buffer, notSneaking, textBackgroundColor, packedLight, y);

    this.eventBus.fireEvent(event, Subscribe.Phase.PRE);
  }
}
