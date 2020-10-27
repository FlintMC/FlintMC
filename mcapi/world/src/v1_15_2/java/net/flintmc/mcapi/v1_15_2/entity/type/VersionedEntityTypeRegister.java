package net.flintmc.mcapi.v1_15_2.entity.type;

import com.beust.jcommander.internal.Maps;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.tasks.Task;
import net.flintmc.framework.tasks.Tasks;
import net.minecraft.util.registry.Registry;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * 1.15.2 implementation of the {@link EntityTypeRegister}.
 */
@Singleton
@Implement(value = EntityTypeRegister.class, version = "1.15.2")
public class VersionedEntityTypeRegister implements EntityTypeRegister {

  private final EntityTypeMapper entityTypeMapper;
  private final Map<String, EntityType> entityTypes;

  @Inject
  private VersionedEntityTypeRegister(EntityTypeMapper entityTypeMapper) {
    this.entityTypeMapper = entityTypeMapper;
    this.entityTypes = Maps.newHashMap();
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void convertEntityTypes() {
    for (net.minecraft.entity.EntityType<?> entityType : Registry.ENTITY_TYPE) {
      String key = Registry.ENTITY_TYPE.getKey(entityType).getPath();
      this.entityTypes.put(key, this.entityTypeMapper.fromMinecraftEntityType(entityType));
    }
  }
  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, EntityType> getEntityTypes() {
    return this.entityTypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityType getEntityType(String key) {
    return this.entityTypes.get(key);
  }

}
