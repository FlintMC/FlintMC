package net.flintmc.mcapi.v1_15_2.entity.passive;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.internal.entity.cache.EntityCache;
import net.flintmc.mcapi.entity.passive.AmbientEntity;
import net.flintmc.mcapi.entity.passive.AnimalEntity;
import net.flintmc.mcapi.entity.passive.PassiveEntityMapper;
import net.flintmc.mcapi.entity.passive.farmanimal.PigEntity;
import net.flintmc.framework.inject.implement.Implement;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(value = PassiveEntityMapper.class, version = "1.15.2")
public class VersionedPassiveEntityMapper implements PassiveEntityMapper {

  private final AmbientEntity.Provider ambientEntityProvider;
  private final AnimalEntity.Provider animalEntityProvider;
  private final EntityCache entityCache;
  private final PigEntity.Factory pigEntityFactory;

  @Inject
  private VersionedPassiveEntityMapper(
          AmbientEntity.Provider ambientEntityProvider,
          AnimalEntity.Provider animalEntityProvider,
          EntityCache entityCache,
          PigEntity.Factory pigEntityFactory
  ) {
    this.ambientEntityProvider = ambientEntityProvider;
    this.animalEntityProvider = animalEntityProvider;
    this.entityCache = entityCache;
    this.pigEntityFactory = pigEntityFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AmbientEntity fromMinecraftAmbientEntity(Object handle) {
    if (!(handle instanceof net.minecraft.entity.passive.AmbientEntity)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.entity.passive.AmbientEntity.class.getName());
    }

    net.minecraft.entity.passive.AmbientEntity ambientEntity = (net.minecraft.entity.passive.AmbientEntity) handle;

    return (AmbientEntity) this.entityCache.putIfAbsent(
            ambientEntity.getUniqueID(),
            () -> this.ambientEntityProvider.get(ambientEntity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftAmbientEntity(AmbientEntity ambientEntity) {
    for (net.minecraft.entity.Entity allEntity : Minecraft.getInstance().world.getAllEntities()) {
      if (allEntity instanceof net.minecraft.entity.passive.AmbientEntity && allEntity.getEntityId() == ambientEntity.getIdentifier()) {
        return allEntity;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AnimalEntity fromMinecraftAnimalEntity(Object handle) {
    if (!(handle instanceof net.minecraft.entity.passive.AnimalEntity)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.entity.passive.AnimalEntity.class.getName());
    }

    net.minecraft.entity.passive.AnimalEntity animalEntity = (net.minecraft.entity.passive.AnimalEntity) handle;

    return (AnimalEntity) this.entityCache.putIfAbsent(
            animalEntity.getUniqueID(),
            () -> this.animalEntityProvider.get(animalEntity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftAnimalEntity(AnimalEntity animalEntity) {
    for (net.minecraft.entity.Entity allEntity : Minecraft.getInstance().world.getAllEntities()) {
      if (allEntity instanceof net.minecraft.entity.passive.AnimalEntity && allEntity.getEntityId() == animalEntity.getIdentifier()) {
        return allEntity;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PigEntity fromMinecraftPigEntity(Object handle) {
    if (!(handle instanceof net.minecraft.entity.passive.PigEntity)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.entity.passive.PigEntity.class.getName());
    }

    net.minecraft.entity.passive.PigEntity pigEntity = (net.minecraft.entity.passive.PigEntity) handle;

    return (PigEntity) this.entityCache.putIfAbsent(
            pigEntity.getUniqueID(),
            () -> this.pigEntityFactory.create(pigEntity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftPigEntity(PigEntity pigEntity) {
    for (net.minecraft.entity.Entity allEntity : Minecraft.getInstance().world.getAllEntities()) {
      if (allEntity instanceof net.minecraft.entity.passive.PigEntity && allEntity.getEntityId() == pigEntity.getIdentifier()) {
        return allEntity;
      }
    }

    return null;
  }
}
