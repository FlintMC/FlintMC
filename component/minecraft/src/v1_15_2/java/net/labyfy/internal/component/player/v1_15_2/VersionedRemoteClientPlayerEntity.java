package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.nbt.mapper.NBTMapper;
import net.labyfy.component.player.RemoteClientPlayerEntity;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.type.model.ModelMapper;
import net.labyfy.component.world.ClientWorld;

/**
 * 1.15.2 implementation of the {@link RemoteClientPlayerEntity}.
 */
@Implement(value = RemoteClientPlayerEntity.class, version = "1.15.2")
public class VersionedRemoteClientPlayerEntity extends VersionedAbstractClientPlayer implements RemoteClientPlayerEntity {

  @AssistedInject
  private VersionedRemoteClientPlayerEntity(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          ClientWorld clientWorld,
          EntityMapper entityMapper,
          GameProfileSerializer gameProfileSerializer,
          ModelMapper modelMapper,
          NetworkPlayerInfoRegistry networkPlayerInfoRegistry,
          NBTMapper nbtMapper
  ) {
    super(
            entity,
            entityType,
            clientWorld,
            entityMapper,
            gameProfileSerializer,
            modelMapper,
            networkPlayerInfoRegistry,
            nbtMapper
    );
  }
}
