package net.flintmc.mcapi.v1_15_2.entity.passive.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.passive.AnimalEntity;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;

@Singleton
@Implement(value = AnimalEntity.Provider.class, version = "1.15.2")
public class VersionedAnimalEntityProvider implements AnimalEntity.Provider {

  private final AnimalEntity.Factory animalEntityFactory;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedAnimalEntityProvider(
      AnimalEntity.Factory animalEntityFactory, EntityTypeMapper entityTypeMapper) {
    this.animalEntityFactory = animalEntityFactory;
    this.entityTypeMapper = entityTypeMapper;
  }

  /** {@inheritDoc} */
  @Override
  public AnimalEntity get(Object entity) {
    if (!(entity instanceof net.minecraft.entity.passive.AnimalEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.passive.AnimalEntity.class.getName());
    }
    net.minecraft.entity.passive.AnimalEntity animalEntity =
        (net.minecraft.entity.passive.AnimalEntity) entity;

    return this.animalEntityFactory.create(
        animalEntity, this.entityTypeMapper.fromMinecraftEntityType(animalEntity.getType()));
  }
}
