package net.labyfy.component.entity.type;

import net.labyfy.component.entity.Entity;

public interface EntityTypeRegister<T extends Entity> {

  void convertRegisteredEntityTypes();

  void register(String key, EntityType.Builder<T> builder);

}
