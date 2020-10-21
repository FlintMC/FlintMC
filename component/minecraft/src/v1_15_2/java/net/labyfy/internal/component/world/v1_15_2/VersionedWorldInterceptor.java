package net.labyfy.internal.component.world.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.tileentity.mapper.TileEntityMapper;
import net.labyfy.component.world.World;

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

  // TODO: 21.10.2020 It does not work with the current hook system. The Game says: 'I'm cold, I freezing' :(
  /*
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
      SignTileEntity signTileEntity = (SignTileEntity) minecraftTileEntity;
      this.world.getLoadedTileEntities().add(this.tileEntityMapper.fromMinecraftSignTileEntity(signTileEntity));
    } else {
      TileEntity tileEntity = this.tileEntityMapper.fromMinecraftTileEntity(minecraftTileEntity);
      this.world.getLoadedTileEntities().add(this.tileEntityMapper.fromMinecraftTileEntity(tileEntity));
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
  
   */
}
