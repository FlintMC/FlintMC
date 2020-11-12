package net.flintmc.mcapi.internal.entity.render;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.chat.event.ChatSendEvent;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.internal.entity.cache.EntityCache;
import net.flintmc.render.model.ModelBox;

import java.util.Map;
import java.util.UUID;

@Singleton
public class Test123 {

  private final EntityCache entityCache;

  @Inject
  private Test123(EntityCache entityCache) {
    this.entityCache = entityCache;
  }

  @Subscribe
  public void test123(ChatSendEvent chatSendEvent){
    for (Map.Entry<UUID, Entity> uuidEntityEntry : this.entityCache.getEntities().entrySet()) {
      for (Map.Entry<String, ModelBox> stringModelBoxEntry : uuidEntityEntry.getValue().getRenderContext().getRenderables().entrySet()) {
        System.out.println(stringModelBoxEntry.getKey() + " " + uuidEntityEntry.getValue());
      }
    }
  }

}
