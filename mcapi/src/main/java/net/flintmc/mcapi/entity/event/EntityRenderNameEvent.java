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

package net.flintmc.mcapi.entity.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;

/**
 * This event will be rendered whenever the name of an entity is being rendered. It may be fired
 * twice per entity if the entity is not sneaking because in this case the string needs to be
 * rendered twice (once with the textBackgroundColor being 0 and once with it being the color of the
 * background) so that it will be completely rendered. It will only be fired in the {@link
 * Subscribe.Phase#PRE} phase.
 *
 * @see Subscribe
 */
public interface EntityRenderNameEvent extends Event {

  /**
   * Retrieves the entity whose name is being rendered.
   *
   * @return The non-null entity of this event
   */
  Entity getEntity();

  /**
   * Retrieves the display name of the entity that is being rendered by Minecraft.
   *
   * @return The non-null display name of the entity
   */
  String getDisplayName();

  /**
   * Retrieves the matrix that contains data for the renderer. This is version specific, but when
   * rendering something it should be applied to the renderer if possible.
   *
   * @return The matrix of this event, or {@code null} if the version doesn't use a matrix for
   * rendering
   */
  Object getMatrix();

  /**
   * Retrieves the buffer that contains data for the renderer. This is version specific, but when
   * rendering something it should be applied to the renderer if possible.
   *
   * @return The buffer of this event, or {@code null} if the version doesn't use a buffer for
   * rendering
   */
  Object getBuffer();

  /**
   * Retrieves whether the name should be rendered as if the player is not sneaking. If this is
   * {@code false}, this doesn't mean that the player is sneaking, it just means that the name
   * should be rendered as if the player is sneaking.
   *
   * @return {@code true} if the name should be rendered as if the player is not sneaking, {@code
   * false} otherwise
   */
  boolean isNotSneaking();

  /**
   * Retrieves the color of the text background that should be used when rendering text on the
   * screen.
   *
   * @return The text background color of this event
   */
  int getTextBackgroundColor();

  /**
   * Retrieves the packed light value that should be used to render the name.
   *
   * @return The packed light of this event
   */
  int getPackedLight();

  /**
   * Retrieves the Y coordinate where the name should be rendered.
   *
   * @return The Y coordinate where the name should be rendered
   */
  int getY();

  /**
   * Factory for the {@link EntityRenderNameEvent}.
   */
  @AssistedFactory(EntityRenderNameEvent.class)
  interface Factory {

    /**
     * Creates a new {@link EntityRenderNameEvent}.
     *
     * @param entity              The non-null entity that is being rendered
     * @param displayName         The non-null display name of the entity
     * @param matrix              The matrix of this event, or {@code null} if the version doesn't
     *                            use a matrix for rendering
     * @param buffer              The buffer of this event, or {@code null} if the version doesn't
     *                            use a buffer for rendering
     * @param notSneaking         {@code true} if the name should be rendered as if the player is
     *                            not sneaking, {@code false} otherwise
     * @param textBackgroundColor The text background color of this event
     * @param packedLight         The packed light to be rendered
     * @param y                   The Y coordinate where the name should be rendered
     * @return The new non-null {@link EntityRenderNameEvent}
     */
    EntityRenderNameEvent create(
        @Assisted Entity entity,
        @Assisted String displayName,
        @Assisted("matrix") Object matrix,
        @Assisted("buffer") Object buffer,
        @Assisted boolean notSneaking,
        @Assisted("textBackgroundColor") int textBackgroundColor,
        @Assisted("packedLight") int packedLight,
        @Assisted("y") int y);
  }
}
