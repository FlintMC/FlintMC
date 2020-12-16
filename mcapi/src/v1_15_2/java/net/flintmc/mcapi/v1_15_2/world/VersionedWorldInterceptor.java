package net.flintmc.mcapi.v1_15_2.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.tileentity.TileEntity;
import net.flintmc.mcapi.tileentity.mapper.TileEntityMapper;
import net.flintmc.mcapi.world.World;
import net.flintmc.transform.hook.Hook;
import net.minecraft.util.math.BlockPos;

/**
 * 1.15.2 implementation nof the world interceptor.
 */
@Singleton
public class VersionedWorldInterceptor {

  private final World world;
  private final TileEntityMapper tileEntityMapper;

  @Inject
  private VersionedWorldInterceptor(World world, TileEntityMapper tileEntityMapper) {
    this.world = world;
    this.tileEntityMapper = tileEntityMapper;
  }

  @Hook(
      className = "net.minecraft.world.World",
      methodName = "addTileEntity",
      parameters = {@Type(reference = net.minecraft.tileentity.TileEntity.class)})
  public void hookAfterAddTileEntity(@Named("args") Object[] args) {
    net.minecraft.tileentity.TileEntity minecraftTileEntity =
        (net.minecraft.tileentity.TileEntity) args[0];

    if (minecraftTileEntity instanceof net.minecraft.tileentity.SignTileEntity) {
      net.minecraft.tileentity.SignTileEntity signTileEntity =
          (net.minecraft.tileentity.SignTileEntity) minecraftTileEntity;
      this.world
          .addTileEntity(this.tileEntityMapper.fromMinecraftSignTileEntity(signTileEntity));
    } else {
      this.world
          .addTileEntity(this.tileEntityMapper.fromMinecraftTileEntity(minecraftTileEntity));
    }
  }

  @Hook(
      className = "net.minecraft.world.World",
      methodName = "removeTileEntity",
      parameters = {@Type(reference = BlockPos.class)})
  public void hookAfterRemoveTileEntity(@Named("args") Object[] args) {
    BlockPos blockPos = (BlockPos) args[0];
    TileEntity tileEntity = this.world.getTileEntity(this.world.fromMinecraftBlockPos(blockPos));

    if (tileEntity != null) {
      this.world.removeTileEntity(tileEntity);
    }
  }
}
