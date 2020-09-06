package net.labyfy.items.meta;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.exception.ComponentDeserializationException;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

import java.util.ArrayList;
import java.util.List;

@Implement(value = ItemMeta.class, version = "1.15.2")
public class VersionedItemMeta extends DefaultItemMeta {

  private final CompoundNBT nbt;
  private final ComponentSerializer.Factory componentFactory;
  private ChatComponent customDisplayName;
  private ChatComponent[] lore;

  @Inject
  public VersionedItemMeta(ComponentSerializer.Factory componentFactory) {
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

  @Override
  public void applyNBTFrom(Object source) {
    Preconditions.checkArgument(source instanceof CompoundNBT, "Provided nbt object " + source.getClass().getName() + " is no instance of " + CompoundNBT.class.getName());

    this.move((CompoundNBT) source, this.nbt);
  }

  @Override
  public void copyNBTTo(Object target) {
    Preconditions.checkArgument(target instanceof CompoundNBT, "Provided nbt object " + target.getClass().getName() + " is no instance of " + CompoundNBT.class.getName());

    this.move(this.nbt, (CompoundNBT) target);
  }

  private void move(CompoundNBT source, CompoundNBT target) {
    for (String key : target.keySet()) {
      target.remove(key);
    }

    for (String key : source.keySet()) {
      target.put(key, source.get(key));
    }
  }

  interface Keys {

    String DISPLAY = "display";
    String NAME = "Name";
    String LORE = "Lore";
    String DAMAGE = "Damage";

  }

}
