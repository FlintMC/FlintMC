package net.labyfy.internal.component.world.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.tileentity.TileEntity;
import net.labyfy.component.tileentity.mapper.TileEntityMapper;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.world.World;
import net.minecraft.util.math.BlockPos;

/**
 * 1.15.2 implementation nof the world interceptor.
 */
@Singleton
@AutoLoad
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
          parameters = {
                  @Type(reference = net.minecraft.tileentity.TileEntity.class)
          }
  )
  public void hookAfterAddTileEntity(@Named("args") Object[] args) {
    net.minecraft.tileentity.TileEntity minecraftTileEntity = (net.minecraft.tileentity.TileEntity) args[0];

    if (minecraftTileEntity instanceof net.minecraft.tileentity.SignTileEntity) {
      net.minecraft.tileentity.SignTileEntity signTileEntity = (net.minecraft.tileentity.SignTileEntity) minecraftTileEntity;
      this.world.getLoadedTileEntities().add(this.tileEntityMapper.fromMinecraftSignTileEntity(signTileEntity));
    } else {
      this.world.getLoadedTileEntities().add(this.tileEntityMapper.fromMinecraftTileEntity(minecraftTileEntity));
    }

  }

  @Hook(
          className = "net.minecraft.world.World",
          methodName = "removeTileEntity",
          parameters = {
                  @Type(reference = BlockPos.class)
          }
  )
  public void hookAfterRemoveTileEntity(@Named("args") Object[] args) {
    BlockPos blockPos = (BlockPos) args[0];
    TileEntity tileEntity = this.world.getTileEntity(this.world.fromMinecraftBlockPos(blockPos));

    if (tileEntity != null) {
      this.world.getLoadedTileEntities().remove(tileEntity);
    }
  }
  

}
