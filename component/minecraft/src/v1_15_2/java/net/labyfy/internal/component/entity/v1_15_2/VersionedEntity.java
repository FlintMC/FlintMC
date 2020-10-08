package net.labyfy.internal.component.entity.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.component.TextComponent;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.ClientWorld;
import net.labyfy.component.world.World;

@Implement(value = Entity.class, version = "1.15.2")
public class VersionedEntity implements Entity {

  private final EntityType entityType;
  private final World world;

  private net.minecraft.entity.Entity entity;

  @AssistedInject
  private VersionedEntity(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          @Assisted("world") ClientWorld world
  ) {
    this.entityType = entityType;
    this.world = world;

    if (!(entity instanceof net.minecraft.entity.Entity)) {
      throw new IllegalArgumentException("");
    }

    this.entity = (net.minecraft.entity.Entity) entity;
  }

  @Override
  public int getTeamColor() {
    return this.entity.getTeamColor();
  }

  @Override
  public void detach() {
    this.entity.detach();
  }

  @Override
  public void setPacketCoordinates(double x, double y, double z) {
    this.entity.setPacketCoordinates(x, y, z);
  }

  @Override
  public EntityType getType() {
    return this.entityType;
  }

  @Override
  public TextComponent getName() {
    return null;
  }
}
