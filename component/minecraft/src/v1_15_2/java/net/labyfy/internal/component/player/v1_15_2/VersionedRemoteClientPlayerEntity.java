package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.RemoteClientPlayerEntity;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.type.model.ModelMapper;
import net.labyfy.component.world.ClientWorld;

@Implement(value = RemoteClientPlayerEntity.class, version = "1.15.2")
public class VersionedRemoteClientPlayerEntity extends VersionedAbstractClientPlayer implements RemoteClientPlayerEntity {

  @AssistedInject
  private VersionedRemoteClientPlayerEntity(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          @Assisted("world") ClientWorld clientWorld,
          @Assisted("entityMapper") EntityMapper entityMapper,
          @Assisted("gameProfileSerializer") GameProfileSerializer gameProfileSerializer,
          @Assisted("modelMapper") ModelMapper modelMapper,
          @Assisted("networkPlayerInfoRegistry") NetworkPlayerInfoRegistry networkPlayerInfoRegistry
  ) {
    super(entity, entityType, clientWorld, entityMapper, gameProfileSerializer, modelMapper, networkPlayerInfoRegistry);
  }
}
