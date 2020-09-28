package net.labyfy.internal.component.world.v1_15_2.border;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.border.BorderState;
import net.labyfy.component.world.border.WorldBorder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.border.BorderStatus;

/**
 * 1.15.2 implementation of {@link WorldBorder}.
 */
@Singleton
@Implement(value = WorldBorder.class, version = "1.15.2")
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
  public void setSize(int size) {
    Minecraft.getInstance().world.getWorldBorder().setSize(size);
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
