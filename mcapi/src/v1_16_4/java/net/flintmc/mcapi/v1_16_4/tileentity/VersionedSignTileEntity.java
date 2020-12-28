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

package net.flintmc.mcapi.v1_16_4.tileentity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.tileentity.SignTileEntity;
import net.flintmc.mcapi.tileentity.type.TileEntityTypeRegister;
import net.flintmc.mcapi.world.World;
import net.minecraft.util.text.ITextComponent;

@Implement(value = SignTileEntity.class, version = "1.16.4")
public class VersionedSignTileEntity extends VersionedTileEntity implements SignTileEntity {

  private final net.minecraft.tileentity.SignTileEntity signTileEntity;
  private final MinecraftComponentMapper componentMapper;

  @AssistedInject
  private VersionedSignTileEntity(
      @Assisted("tileEntity") Object tileEntity,
      TileEntityTypeRegister tileEntityTypeRegister,
      World world,
      MinecraftComponentMapper componentMapper) {
    super(tileEntity, tileEntityTypeRegister.getTileEntityType("sign"), world);
    this.signTileEntity = (net.minecraft.tileentity.SignTileEntity) tileEntity;
    this.componentMapper = componentMapper;
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getText(int line) {
    try {
      return this.componentMapper.fromMinecraft(this.signTileEntity.getText(line));
    } catch (ArrayIndexOutOfBoundsException exception) {
      return null;
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setText(int line, ChatComponent component) {
    this.signTileEntity.setText(line, (ITextComponent) this.componentMapper.toMinecraft(component));
  }
}
