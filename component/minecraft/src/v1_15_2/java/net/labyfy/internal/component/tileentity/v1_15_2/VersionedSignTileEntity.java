package net.labyfy.internal.component.tileentity.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.tileentity.SignTileEntity;
import net.labyfy.component.tileentity.type.TileEntityTypeRegister;
import net.labyfy.component.world.World;
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
