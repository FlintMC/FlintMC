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

package net.flintmc.mcapi.v1_16_5.items.meta;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.exception.ComponentDeserializationException;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.internal.items.meta.DefaultItemMeta;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.meta.ItemMeta;
import net.flintmc.mcapi.items.meta.enchantment.Enchantment;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

@Implement(value = ItemMeta.class)
public class VersionedItemMeta extends DefaultItemMeta {

  private final ItemRegistry itemRegistry;
  private final CompoundNBT nbt;
  private final ComponentSerializer.Factory componentFactory;
  private ChatComponent customDisplayName;
  private ChatComponent[] lore;
  private Map<EnchantmentType, Enchantment> enchantments;

  @Inject
  public VersionedItemMeta(
      ItemRegistry itemRegistry, ComponentSerializer.Factory componentFactory) {
    this.itemRegistry = itemRegistry;
    this.nbt = new CompoundNBT();
    this.componentFactory = componentFactory;
  }

  private INBT asNBT(ChatComponent component) {
    return StringNBT.valueOf(this.componentFactory.gson().serialize(component));
  }

  private ChatComponent asComponent(INBT nbt) {
    try {
      return this.componentFactory.gson().deserialize(nbt.getString());
    } catch (ComponentDeserializationException exception) {
      return null;
    }
  }

  @Override
  public ChatComponent getCustomDisplayName() {
    if (this.customDisplayName != null) {
      return this.customDisplayName;
    }

    if (!this.nbt.contains(Keys.DISPLAY, 10)) {
      return null;
    }
    CompoundNBT display = this.nbt.getCompound(Keys.DISPLAY);
    if (!display.contains(Keys.NAME, 8)) {
      return null;
    }

    return this.customDisplayName = this.asComponent(display.get(Keys.NAME));
  }

  @Override
  public void setCustomDisplayName(ChatComponent displayName) {
    CompoundNBT display = this.nbt.getCompound(Keys.DISPLAY);

    if (displayName != null) {
      display.put(Keys.NAME, this.asNBT(displayName));
    } else {
      display.remove(Keys.NAME);
    }

    this.nbt.put(Keys.DISPLAY, display);

    this.customDisplayName = displayName;
  }

  @Override
  public ChatComponent[] getLore() {
    if (this.lore != null) {
      return this.lore;
    }

    if (!this.nbt.contains(Keys.DISPLAY, 10)) {
      return this.lore = new ChatComponent[0];
    }
    CompoundNBT display = this.nbt.getCompound(Keys.DISPLAY);
    if (!display.contains(Keys.LORE, 9)) {
      return this.lore = new ChatComponent[0];
    }

    ListNBT nbtList = display.getList(Keys.LORE, 8);
    List<ChatComponent> lore = new ArrayList<>(nbtList.size());

    for (INBT line : nbtList) {
      ChatComponent component = this.asComponent(line);
      if (component != null) {
        lore.add(component);
      }
    }

    return this.lore = lore.toArray(new ChatComponent[0]);
  }

  @Override
  public void setLore(ChatComponent... lore) {
    CompoundNBT display = this.nbt.getCompound(Keys.DISPLAY);
    ListNBT nbtList = new ListNBT();

    for (ChatComponent component : lore) {
      nbtList.add(this.asNBT(component));
    }

    display.put(Keys.LORE, nbtList);
    this.nbt.put(Keys.DISPLAY, display);

    this.lore = lore;
  }

  @Override
  public void setLore(List<ChatComponent> lore) {
    this.setLore(lore.toArray(new ChatComponent[0]));
  }

  @Override
  public int getDamage() {
    return this.nbt.getInt(Keys.DAMAGE);
  }

  @Override
  public void setDamage(int damage) {
    super.validateDamage(damage);

    this.nbt.putInt(Keys.DAMAGE, damage);
  }

  private void loadEnchantments() {
    if (this.enchantments != null) {
      return;
    }

    this.enchantments = new HashMap<>();

    if (!this.nbt.contains(Keys.ENCHANTMENTS, 9)) { // 9 = list
      return;
    }

    ListNBT enchantments = this.nbt.getList(Keys.ENCHANTMENTS, 10); // 10 = compound
    for (INBT enchantmentBase : enchantments) {
      CompoundNBT enchantment = (CompoundNBT) enchantmentBase;
      if (!enchantment.contains(Keys.ENCHANTMENT_ID, 8)) {
        continue;
      }

      NameSpacedKey registryName = NameSpacedKey.parse(enchantment.getString(Keys.ENCHANTMENT_ID));
      int level = enchantment.getShort(Keys.ENCHANTMENT_LEVEL); // begins at 1

      EnchantmentType type = this.itemRegistry.getEnchantmentType(registryName);
      if (type == null) {
        continue;
      }

      this.enchantments.put(type, type.createEnchantment(level));
    }
  }

  private void writeEnchantments() {
    if (this.enchantments == null) {
      return;
    }

    ListNBT enchantments = new ListNBT();

    for (Enchantment enchantment : this.enchantments.values()) {
      CompoundNBT compound = new CompoundNBT();

      compound.putString(Keys.ENCHANTMENT_ID, enchantment.getType().getRegistryName().toString());
      compound.putShort(Keys.ENCHANTMENT_LEVEL, (short) enchantment.getLevel());

      enchantments.add(compound);
    }

    this.nbt.put(Keys.ENCHANTMENTS, enchantments);
  }

  @Override
  public Enchantment[] getEnchantments() {
    this.loadEnchantments();
    return this.enchantments.values().toArray(new Enchantment[0]);
  }

  @Override
  public boolean hasEnchantment(EnchantmentType type) {
    this.loadEnchantments();
    return this.enchantments.containsKey(type);
  }

  @Override
  public void addEnchantment(Enchantment enchantment) {
    if (this.hasEnchantment(enchantment.getType())
        && this.getEnchantment(enchantment.getType()).getLevel() == enchantment.getLevel()) {
      return;
    }

    this.enchantments.put(enchantment.getType(), enchantment);
    this.writeEnchantments();
  }

  @Override
  public void removeEnchantment(EnchantmentType type) {
    if (!this.hasEnchantment(type)) {
      return;
    }

    this.enchantments.remove(type);
    this.writeEnchantments();
  }

  @Override
  public Enchantment getEnchantment(EnchantmentType type) {
    this.loadEnchantments();
    return this.enchantments.get(type);
  }

  @Override
  public void applyNBTFrom(Object source) {
    Preconditions.checkArgument(
        source instanceof CompoundNBT,
        "Provided nbt object "
            + source.getClass().getName()
            + " is no instance of "
            + CompoundNBT.class.getName());

    this.move((CompoundNBT) source, this.nbt);
  }

  @Override
  public void copyNBTTo(Object target) {
    Preconditions.checkArgument(
        target instanceof CompoundNBT,
        "Provided nbt object "
            + target.getClass().getName()
            + " is no instance of "
            + CompoundNBT.class.getName());

    this.move(this.nbt, (CompoundNBT) target);
  }

  @Override
  public Object getNBT() {
    return this.nbt;
  }

  private void move(CompoundNBT source, CompoundNBT target) {
    for (String key : target.keySet()) {
      target.remove(key);
    }

    for (String key : source.keySet()) {
      target.put(key, source.get(key));
    }
  }

  @Override
  protected int getHideFlagsBase() {
    return this.nbt.getInt(Keys.HIDE_FLAGS);
  }

  @Override
  protected void setHideFlagsBase(int base) {
    this.nbt.putInt(Keys.HIDE_FLAGS, base);
  }

  interface Keys {

    String DISPLAY = "display";
    String NAME = "Name";
    String LORE = "Lore";
    String HIDE_FLAGS = "HideFlags";
    String DAMAGE = "Damage";
    String ENCHANTMENTS = "Enchantments";
    String ENCHANTMENT_ID = "id";
    String ENCHANTMENT_LEVEL = "lvl";
  }
}
