package net.labyfy.internal.component.tileentity.v1_15_2.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.tileentity.SignTileEntity;
import net.labyfy.component.tileentity.TileEntity;
import net.labyfy.component.tileentity.mapper.TileEntityMapper;
import net.labyfy.component.tileentity.type.TileEntityTypeRegister;
import net.labyfy.component.world.math.BlockPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

@Singleton
@Implement(value = TileEntityMapper.class, version = "1.15.2")
public class VersionedTileEntityMapper implements TileEntityMapper {

  private final TileEntity.Factory tileEntityFactory;
  private final TileEntityTypeRegister tileEntityTypeRegister;
  private final SignTileEntity.Factory signTileEntityFactory;

  @Inject
  private VersionedTileEntityMapper(
          TileEntity.Factory tileEntityFactory,
          TileEntityTypeRegister tileEntityTypeRegister,
          SignTileEntity.Factory signTileEntityFactory
  ) {
    this.tileEntityFactory = tileEntityFactory;
    this.tileEntityTypeRegister = tileEntityTypeRegister;
    this.signTileEntityFactory = signTileEntityFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftTileEntity(TileEntity tileEntity) {

    for (net.minecraft.tileentity.TileEntity entity : Minecraft.getInstance().world.loadedTileEntityList) {
      if (entity instanceof net.minecraft.tileentity.SignTileEntity &&
              equalsBlockPosition(tileEntity.getPosition(), entity.getPos())) {
        return entity;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TileEntity fromMinecraftTileEntity(Object tileEntity) {
    if (!(tileEntity instanceof net.minecraft.tileentity.TileEntity)) {
      throw new IllegalArgumentException(tileEntity.getClass().getName() + " is not an instance of " + net.minecraft.tileentity.TileEntity.class.getName());
    }


    net.minecraft.tileentity.TileEntity minecraftTileEntity = (net.minecraft.tileentity.TileEntity) tileEntity;

    return this.tileEntityFactory.create(
            minecraftTileEntity,
            this.tileEntityTypeRegister.getTileEntityType(
                    Registry.BLOCK_ENTITY_TYPE.getKey(
                            minecraftTileEntity.getType()
                    ).getPath()
            )
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftSignTileEntity(SignTileEntity signTileEntity) {

    for (net.minecraft.tileentity.TileEntity tileEntity : Minecraft.getInstance().world.loadedTileEntityList) {
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
