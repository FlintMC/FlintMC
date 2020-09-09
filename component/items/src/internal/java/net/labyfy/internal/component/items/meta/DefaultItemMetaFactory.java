package net.labyfy.internal.component.items.meta;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.items.meta.ItemMeta;
import net.labyfy.component.items.type.ItemType;

@Singleton
@Implement(ItemMeta.Factory.class)
public class DefaultItemMetaFactory implements ItemMeta.Factory {

  @Override
  public ItemMeta createMeta(ItemType type) {
    ItemMeta meta = InjectionHolder.getInjectedInstance(type.getMetaClass());
    if (meta instanceof DefaultItemMeta) {
      ((DefaultItemMeta) meta).setType(type);
    }
    return meta;
  }

}
