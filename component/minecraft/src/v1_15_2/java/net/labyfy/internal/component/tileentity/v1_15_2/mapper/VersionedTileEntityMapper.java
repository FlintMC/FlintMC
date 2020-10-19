package net.labyfy.internal.component.tileentity.v1_15_2.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.tileentity.SignTileEntity;
import net.labyfy.component.tileentity.mapper.TileEntityMapper;
import net.labyfy.component.world.math.BlockPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

@Singleton
@Implement(value = TileEntityMapper.class, version = "1.15.2")
public class VersionedTileEntityMapper implements TileEntityMapper {

  private final SignTileEntity.Factory signTileEntityFactory;

  @Inject
  private VersionedTileEntityMapper(SignTileEntity.Factory signTileEntityFactory) {
    this.signTileEntityFactory = signTileEntityFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftSignTileEntity(SignTileEntity signTileEntity) {

    for (TileEntity tileEntity : Minecraft.getInstance().world.loadedTileEntityList) {
      if (tileEntity instanceof net.minecraft.tileentity.SignTileEntity &&
              equalsBlockPosition(signTileEntity.getPosition(), tileEntity.getPos())) {
        return tileEntity;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SignTileEntity fromMinecraftSignTileEntity(Object signTileEntity) {
    if (!(signTileEntity instanceof net.minecraft.tileentity.SignTileEntity)) {
      throw new IllegalArgumentException(signTileEntity.getClass().getName() + " is not an instance of " + net.minecraft.tileentity.SignTileEntity.class.getName());
    }

    net.minecraft.tileentity.SignTileEntity minecraftSignTileEntity = (net.minecraft.tileentity.SignTileEntity) signTileEntity;
    return this.signTileEntityFactory.create(minecraftSignTileEntity);
  }

  private boolean equalsBlockPosition(BlockPosition position, BlockPos blockPos) {
    return position.getX() == blockPos.getX() &&
            position.getY() == blockPos.getY() &&
            position.getZ() == blockPos.getZ();
  }
}
