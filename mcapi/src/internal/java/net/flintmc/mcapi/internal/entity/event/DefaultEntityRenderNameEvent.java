package net.flintmc.mcapi.internal.entity.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityRenderNameEvent;

@Implement(EntityRenderNameEvent.class)
public class DefaultEntityRenderNameEvent implements EntityRenderNameEvent {

  private final Entity entity;
  private final String displayName;
  private final Object matrix;
  private final Object buffer;
  private final int textBackgroundColor;
  private final boolean notSneaking;
  private final int packedLight;
  private final int y;

  @AssistedInject
  public DefaultEntityRenderNameEvent(
      @Assisted Entity entity,
      @Assisted String displayName,
      @Assisted("matrix") Object matrix,
      @Assisted("buffer") Object buffer,
      @Assisted boolean notSneaking,
      @Assisted("textBackgroundColor") int textBackgroundColor,
      @Assisted("packedLight") int packedLight,
      @Assisted("y") int y) {
    this.entity = entity;
    this.displayName = displayName;
    this.matrix = matrix;
    this.buffer = buffer;
    this.textBackgroundColor = textBackgroundColor;
    this.notSneaking = notSneaking;
    this.packedLight = packedLight;
    this.y = y;
  }

  @Override
  public Entity getEntity() {
    return this.entity;
  }

  @Override
  public String getDisplayName() {
    return this.displayName;
  }

  @Override
  public Object getMatrix() {
    return this.matrix;
  }

  @Override
  public Object getBuffer() {
    return this.buffer;
  }

  @Override
  public int getTextBackgroundColor() {
    return this.textBackgroundColor;
  }

  @Override
  public boolean isNotSneaking() {
    return this.notSneaking;
  }

  @Override
  public int getPackedLight() {
    return this.packedLight;
  }

  @Override
  public int getY() {
    return this.y;
  }
}
