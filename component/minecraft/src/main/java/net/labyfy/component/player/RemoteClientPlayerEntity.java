package net.labyfy.component.player;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.PlayerClothingSerializer;
import net.labyfy.component.world.ClientWorld;

public interface RemoteClientPlayerEntity extends AbstractClientPlayerEntity {

  @AssistedFactory(RemoteClientPlayerEntity.class)
  interface Factory {

    RemoteClientPlayerEntity create(
            @Assisted("entity") Object entity,
            @Assisted("entityType") EntityType entityType,
            @Assisted("world") ClientWorld clientWorld,
            @Assisted("entityMapper") EntityMapper entityMapper,
            @Assisted("gameProfileSerializer") GameProfileSerializer gameProfileSerializer,
            @Assisted("playerClothingSerializer") PlayerClothingSerializer playerClothingSerializer,
            @Assisted("networkPlayerInfoRegistry") NetworkPlayerInfoRegistry networkPlayerInfoRegistry
    );

  }

  interface Provider {

    RemoteClientPlayerEntity get(Object entity);

  }

}
