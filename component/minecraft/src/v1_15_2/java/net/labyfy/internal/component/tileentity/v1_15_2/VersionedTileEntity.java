package net.labyfy.internal.component.tileentity.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.tileentity.TileEntity;
import net.labyfy.component.tileentity.type.TileEntityType;
import net.labyfy.component.world.World;
import net.labyfy.component.world.math.BlockPosition;
import net.minecraft.util.math.BlockPos;

@Implement(value = TileEntity.class, version = "1.15.2")
public class VersionedTileEntity implements TileEntity {

  private final TileEntityType tileEntityType;
  private final World world;
  private final net.minecraft.tileentity.TileEntity tileEntity;

  @AssistedInject
  public VersionedTileEntity(
          @Assisted("tileEntity") Object tileEntity,
          @Assisted("tileEntityType") TileEntityType tileEntityType,
          World world
  ) {
    this.tileEntityType = tileEntityType;
    this.tileEntity = (net.minecraft.tileentity.TileEntity) tileEntity;
    this.world = world;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public World getWorld() {
    return this.world;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasWorld() {
    return this.tileEntity.hasWorld();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getDistanceSq(double x, double y, double z) {
    return this.tileEntity.getDistanceSq(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getMaxRenderDistanceSquared() {
    return this.tileEntity.getMaxRenderDistanceSquared();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockPosition getPosition() {
    return this.world.fromMinecraftBlockPos(this.tileEntity.getPos());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPosition(BlockPosition position) {
    this.tileEntity.setPos(
            (BlockPos) this.world.toMinecraftBlockPos(position)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRemoved() {
    return this.tileEntity.isRemoved();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removed() {
    this.tileEntity.remove();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void validate() {
    this.tileEntity.validate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateContainingBlockInfo() {
    this.tileEntity.updateContainingBlockInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TileEntityType getType() {
    return this.tileEntityType;
  }
}
