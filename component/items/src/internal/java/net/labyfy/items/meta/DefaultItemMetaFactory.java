package net.labyfy.items.meta;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.items.type.ItemType;

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
