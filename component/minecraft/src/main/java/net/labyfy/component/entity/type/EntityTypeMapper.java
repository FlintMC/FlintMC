package net.labyfy.component.entity.type;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntitySize;

public interface EntityTypeMapper {

  Object toMinecraftEntityType(EntityType<?> type);

  EntityType<?> fromMinecraftEntityType(Object object);

  Object toMinecraftEntityClassification(Entity.Classification classification);

  Entity.Classification fromMinecraftEntityClassification(Object object);

  Object toMinecraftEntitySize(EntitySize entitySize);

  EntitySize fromMinecraftEntitySize(Object object);

}
