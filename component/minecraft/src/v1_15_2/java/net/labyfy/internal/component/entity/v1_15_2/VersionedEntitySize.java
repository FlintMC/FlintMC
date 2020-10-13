package net.labyfy.internal.component.entity.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.EntitySize;
import net.labyfy.component.inject.implement.Implement;

/**
 * 1.15.2 implementation of the {@link EntitySize}.
 */
@Implement(value = EntitySize.class, version = "1.15.2")
public class VersionedEntitySize implements EntitySize {

  private float width;
  private float height;
  private boolean fixed;

  @AssistedInject
  private VersionedEntitySize(
          @Assisted("width") float width,
          @Assisted("height") float height,
          @Assisted("fixed") boolean fixed
  ) {
    this.width = width;
    this.height = height;
    this.fixed = fixed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntitySize scale(float factor) {
    return this.scale(factor, factor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntitySize scale(float widthFactor, float heightFactor) {
    if (!this.fixed && (widthFactor != 1.0F || heightFactor != 1.0F)) {
      this.width *= widthFactor;
      this.height *= heightFactor;
      this.fixed = false;
    }

    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getWidth() {
    return this.width;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHeight() {
    return this.height;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isFixed() {
    return this.fixed;
  }

}
