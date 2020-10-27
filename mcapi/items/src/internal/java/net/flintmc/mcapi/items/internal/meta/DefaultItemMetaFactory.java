package net.flintmc.mcapi.items.internal.meta;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.items.meta.ItemMeta;
import net.flintmc.mcapi.items.type.ItemType;

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
