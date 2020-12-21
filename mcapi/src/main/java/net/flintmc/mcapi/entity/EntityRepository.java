package net.flintmc.mcapi.entity;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public interface EntityRepository {

  Entity getEntity(UUID uniqueId);

  Entity putIfAbsent(UUID uniqueId, Supplier<Entity> supplier);

  void clear();

  int size();

  Map<UUID, Entity> getEntities();
}
