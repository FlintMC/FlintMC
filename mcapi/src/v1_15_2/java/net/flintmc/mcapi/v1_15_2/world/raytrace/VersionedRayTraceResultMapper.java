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

package net.flintmc.mcapi.v1_15_2.world.raytrace;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.flintmc.mcapi.world.ClientWorld;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.math.Direction;
import net.flintmc.mcapi.world.math.Vector3D;
import net.flintmc.mcapi.world.raytrace.BlockRayTraceResult;
import net.flintmc.mcapi.world.raytrace.EntityRayTraceResult;
import net.flintmc.mcapi.world.raytrace.RayTraceResult;
import net.flintmc.mcapi.world.raytrace.RayTraceResultMapper;
import net.minecraft.util.math.RayTraceResult.Type;

@Singleton
@Implement(RayTraceResultMapper.class)
public class VersionedRayTraceResultMapper implements RayTraceResultMapper {

  private final EntityRayTraceResult.Factory entityFactory;
  private final BlockRayTraceResult.Factory blockFactory;

  private final Vector3D.Factory vectorFactory;
  private final EntityMapper entityMapper;
  private final ClientWorld world;

  private final RayTraceResult missResult;

  @Inject
  private VersionedRayTraceResultMapper(
      RayTraceResult.Factory factory,
      EntityRayTraceResult.Factory entityFactory,
      BlockRayTraceResult.Factory blockFactory,
      Vector3D.Factory vectorFactory,
      EntityMapper entityMapper,
      ClientWorld world) {
    this.entityFactory = entityFactory;
    this.blockFactory = blockFactory;

    this.vectorFactory = vectorFactory;
    this.entityMapper = entityMapper;
    this.world = world;

    this.missResult = factory.createMiss();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RayTraceResult fromMinecraft(Object handle) {
    net.minecraft.util.math.RayTraceResult result = (net.minecraft.util.math.RayTraceResult) handle;

    if (result.getType() == Type.MISS) {
      return this.missResult;
    }

    Vector3D hitVector = this.vectorFactory
        .create(result.getHitVec().getX(), result.getHitVec().getY(), result.getHitVec().getZ());

    if (result.getType() == Type.ENTITY) {
      Entity entity = this.entityMapper.fromAnyMinecraftEntity(
          ((net.minecraft.util.math.EntityRayTraceResult) result).getEntity());
      return this.entityFactory.create(hitVector, entity);
    }

    if (result.getType() == Type.BLOCK) {
      BlockPosition position = this.world.fromMinecraftBlockPos(
          ((net.minecraft.util.math.BlockRayTraceResult) result).getPos());
      Direction face = Direction.fromAngle(
          ((net.minecraft.util.math.BlockRayTraceResult) result).getFace().getHorizontalAngle());

      return this.blockFactory.create(hitVector, position, face);
    }

    return this.missResult;
  }
}
