package net.labyfy.component.entity.projectile;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.items.ItemStack;

/**
 * Represents the Minecraft arrow entity.
 */
public interface ArrowEntity extends ArrowBaseEntity {

  /**
   * Changes the potion effect of the arrow.
   *
   * @param itemStack The item stack to change the potion effect.
   */
  void setPotionEffect(ItemStack itemStack);

  /**
   * Retrieves the arrow color.
   *
   * @return The arrow color.
   */
  int getColor();

  /**
   * A factory class for the {@link ArrowBaseEntity}.
   */
  @AssistedFactory(ArrowEntity.class)
  interface Factory {

    /**
     * Creates a new {@link ArrowEntity} with the given entity.
     *
     * @param entity The entity.
     * @return A created arrow base entity.
     */
    ArrowEntity create(@Assisted("entity") Object entity);

    /**
     * Creates a new {@link ArrowEntity} with the given parameters.
     *
     * @param entity The entity.
     * @param x      The x position.
     * @param y      The y position.
     * @param z      The z position.
     * @return A created arrow base entity.
     */
    ArrowEntity create(
            @Assisted("entity") Object entity,
            @Assisted("x") double x,
            @Assisted("y") double y,
            @Assisted("z") double z
    );

    /**
     * Creates a new {@link ArrowEntity} with the given parameters.
     *
     * @param entity  The entity.
     * @param shooter The shooter of the arrow.
     * @return A created arrow base entity.
     */
    ArrowEntity create(
            @Assisted("entity") Object entity,
            @Assisted("shooter") LivingEntity shooter
    );

  }

}
