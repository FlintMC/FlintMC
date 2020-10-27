package net.flintmc.mcapi.v1_15_2.tileentity;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.tileentity.SignTileEntity;
import net.flintmc.mcapi.tileentity.type.TileEntityTypeRegister;
import net.flintmc.mcapi.world.World;
import net.minecraft.util.text.ITextComponent;

@Implement(value = SignTileEntity.class, version = "1.15.2")
public class VersionedSignTileEntity extends VersionedTileEntity implements SignTileEntity {

  private final net.minecraft.tileentity.SignTileEntity signTileEntity;
  private final MinecraftComponentMapper componentMapper;

  @AssistedInject
  private VersionedSignTileEntity(
          @Assisted("tileEntity") Object tileEntity,
          TileEntityTypeRegister tileEntityTypeRegister,
          World world,
          MinecraftComponentMapper componentMapper
  ) {
    super(tileEntity, tileEntityTypeRegister.getTileEntityType("sign"), world);
    this.signTileEntity = (net.minecraft.tileentity.SignTileEntity) tileEntity;
    this.componentMapper = componentMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getText(int line) {
    try {
      return this.componentMapper.fromMinecraft(this.signTileEntity.getText(line));
    } catch (ArrayIndexOutOfBoundsException exception) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setText(int line, ChatComponent component) {
    this.signTileEntity.setText(
            line,
            (ITextComponent) this.componentMapper.toMinecraft(component)
    );
  }
}
