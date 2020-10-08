package net.labyfy.internal.component.entity.v1_15_2.type;

import com.beust.jcommander.internal.Maps;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.entity.type.EntityTypeBuilder;
import net.labyfy.component.entity.type.EntityTypeMapper;
import net.labyfy.component.entity.type.EntityTypeRegister;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.minecraft.util.registry.Registry;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

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
  public void remappedMinecraftEntityTypes() {
    this.remappedRegisteredEntityTypes();
  }

  @Override
  public void remappedRegisteredEntityTypes() {
    for (net.minecraft.entity.EntityType<?> entityType : Registry.ENTITY_TYPE) {
      String key = Registry.ENTITY_TYPE.getKey(entityType).getPath();
      this.entityTypes.put(key, this.entityTypeMapper.fromMinecraftEntityType(entityType));
    }
  }

  @Override
  public void register(String key, Entity.Classification classification, EntityTypeBuilder.Provider provider) {
    EntityType entityType = provider.create(classification).build(key);

    if (!this.entityTypes.containsKey(key)) {
      this.entityTypes.put(key, entityType);
      this.registerMinecraft(key, entityType);
    }
  }

  @Override
  public Map<String, EntityType> getEntityTypes() {
    return this.entityTypes;
  }

  @Override
  public EntityType getEntityType(String key) {
    return this.entityTypes.get(key);
  }

  private void registerMinecraft(String key, EntityType entityType) {
    Registry.register(
            Registry.ENTITY_TYPE,
            key,
            (net.minecraft.entity.EntityType<?>) this.entityTypeMapper.toMinecraftEntityType(entityType)
    );
  }

}
