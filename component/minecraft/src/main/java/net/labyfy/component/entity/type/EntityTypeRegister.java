package net.labyfy.component.entity.type;

import net.labyfy.component.entity.Entity;

import java.util.Map;

public interface EntityTypeRegister {

  void remappedRegisteredEntityTypes();

  void register(String key, Entity.Classification classification, EntityTypeBuilder.Provider provider);

  Map<String, EntityType> getEntityTypes();

  EntityType getEntityType(String key);

}
