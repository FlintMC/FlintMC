package net.labyfy.component.entity.type;

import net.labyfy.component.entity.Entity;

public interface EntityTypeRegister {

  void remappedRegisteredEntityTypes();

  void register(String key, Entity.Classification classification, EntityTypeBuilder.Provider provider);

}
