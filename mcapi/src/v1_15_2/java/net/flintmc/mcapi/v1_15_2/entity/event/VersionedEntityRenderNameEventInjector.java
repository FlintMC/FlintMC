package net.flintmc.mcapi.v1_15_2.entity.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityRenderNameEvent;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;

@Singleton
public class VersionedEntityRenderNameEventInjector {

  private final EntityMapper entityMapper;
  private final EventBus eventBus;
  private final EntityRenderNameEvent.Factory eventFactory;

  @Inject
  private VersionedEntityRenderNameEventInjector(
      EntityMapper entityMapper, EventBus eventBus, EntityRenderNameEvent.Factory eventFactory) {
    this.entityMapper = entityMapper;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }

  /**
   * Called from the {@link net.minecraft.client.renderer.entity.EntityRenderer} which is
   * transformed by the {@link VersionedEntityRenderNameEventInjectorTransformer}.
   */
  public void renderName(Object[] args, Matrix4f matrix, int y, boolean notSneaking) {
    Entity entity = this.entityMapper.fromAnyMinecraftEntity(args[0]);
    if (entity == null) {
      return;
    }

    int textBackgroundColor = 0;
    if (!notSneaking) {
      float opacity =
          net.minecraft.client.Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
      textBackgroundColor = (int) (opacity * 255.0F) << 24;
    }

    String displayName = (String) args[1];
    IRenderTypeBuffer buffer = (IRenderTypeBuffer) args[3];
    int packedLight = (int) args[4];

    EntityRenderNameEvent event =
        this.eventFactory.create(
            entity, displayName, matrix, buffer, notSneaking, textBackgroundColor, packedLight, y);

    this.eventBus.fireEvent(event, Subscribe.Phase.PRE);
  }
}
