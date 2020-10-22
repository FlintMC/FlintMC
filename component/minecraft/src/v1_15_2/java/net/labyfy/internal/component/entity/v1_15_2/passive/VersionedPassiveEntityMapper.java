package net.labyfy.internal.component.entity.v1_15_2.passive;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.passive.AmbientEntity;
import net.labyfy.component.entity.passive.AnimalEntity;
import net.labyfy.component.entity.passive.PassiveEntityMapper;
import net.labyfy.component.entity.passive.farmanimal.PigEntity;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.internal.component.entity.cache.EntityCache;
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

    if (this.entityCache.isCached(ambientEntity.getUniqueID())) {
      Entity entity = this.entityCache.getEntity(ambientEntity.getUniqueID());

      if (entity instanceof AmbientEntity) {
        return (AmbientEntity) entity;
      }
    }

    return (AmbientEntity) this.entityCache.putAndRetrieveEntity(
            ambientEntity.getUniqueID(),
            this.ambientEntityProvider.get(ambientEntity)
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

    if (this.entityCache.isCached(animalEntity.getUniqueID())) {
      Entity entity = this.entityCache.getEntity(animalEntity.getUniqueID());

      if (entity instanceof AnimalEntity) {
        return (AnimalEntity) entity;
      }
    }

    return (AnimalEntity) this.entityCache.putAndRetrieveEntity(
            animalEntity.getUniqueID(),
            this.animalEntityProvider.get(animalEntity)
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

    if (this.entityCache.isCached(pigEntity.getUniqueID())) {
      Entity entity = this.entityCache.getEntity(pigEntity.getUniqueID());

      if (entity instanceof PigEntity) {
        return (PigEntity) entity;
      }
    }

    return (PigEntity) this.entityCache.putAndRetrieveEntity(
            pigEntity.getUniqueID(),
            this.animalEntityProvider.get(pigEntity)
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
