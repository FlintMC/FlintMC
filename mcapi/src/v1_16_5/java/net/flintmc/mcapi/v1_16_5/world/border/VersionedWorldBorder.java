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

package net.flintmc.mcapi.v1_16_5.world.border;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.border.BorderState;
import net.flintmc.mcapi.world.border.WorldBorder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.border.BorderStatus;

/**
 * 1.16.5 implementation of {@link WorldBorder}.
 */
@Singleton
@Implement(WorldBorder.class)
public class VersionedWorldBorder implements WorldBorder {

  /**
   * {@inheritDoc}
   */
  @Override
  public double minX() {
    return Minecraft.getInstance().world.getWorldBorder().minX();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double minZ() {
    return Minecraft.getInstance().world.getWorldBorder().minZ();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double maxX() {
    return Minecraft.getInstance().world.getWorldBorder().maxX();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double maxZ() {
    return Minecraft.getInstance().world.getWorldBorder().maxZ();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BorderState getState() {
    BorderStatus borderStatus = Minecraft.getInstance().world.getWorldBorder().getStatus();

    switch (borderStatus) {
      case GROWING:
        return BorderState.GROWING;
      case SHRINKING:
        return BorderState.SHRINKING;
      case STATIONARY:
        return BorderState.STATIONARY;
      default:
        throw new IllegalStateException("Unexpected value: " + borderStatus);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getCenterX() {
    return Minecraft.getInstance().world.getWorldBorder().getCenterX();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getCenterZ() {
    return Minecraft.getInstance().world.getWorldBorder().getCenterZ();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCenter(double x, double z) {
    Minecraft.getInstance().world.getWorldBorder().setCenter(x, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getDiameter() {
    return Minecraft.getInstance().world.getWorldBorder().getDiameter();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getTimeUntilTarget() {
    return Minecraft.getInstance().world.getWorldBorder().getTimeUntilTarget();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getTargetSize() {
    return Minecraft.getInstance().world.getWorldBorder().getTargetSize();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTransition(double newSize) {
    Minecraft.getInstance().world.getWorldBorder().setTransition(newSize);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTransition(double oldSize, double newSize, long duration) {
    Minecraft.getInstance().world.getWorldBorder().setTransition(oldSize, newSize, duration);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSize() {
    return Minecraft.getInstance().world.getWorldBorder().getSize();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSize(int size) {
    Minecraft.getInstance().world.getWorldBorder().setSize(size);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getDamageBuffer() {
    return Minecraft.getInstance().world.getWorldBorder().getDamageBuffer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDamageBuffer(double buffer) {
    Minecraft.getInstance().world.getWorldBorder().setDamageBuffer(buffer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getDamagePerBlock() {
    return Minecraft.getInstance().world.getWorldBorder().getDamagePerBlock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDamagePerBlock(double amount) {
    Minecraft.getInstance().world.getWorldBorder().setDamagePerBlock(amount);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getResizeSpeed() {
    return Minecraft.getInstance().world.getWorldBorder().getResizeSpeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getWarningDistance() {
    return Minecraft.getInstance().world.getWorldBorder().getWarningDistance();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWarningDistance(int warningDistance) {
    Minecraft.getInstance().world.getWorldBorder().setWarningDistance(warningDistance);
  }
}
