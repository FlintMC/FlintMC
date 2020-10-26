package net.labyfy.component.player.type.sound;

/**
 * Mapper between the Minecraft sound events, sound categories and Labyfy {@link Sound}, {@link SoundCategory}.
 */
public interface SoundMapper {

  /**
   * Retrieves a {@link Sound} by using the given Minecraft sound event.
   *
   * @param soundEvent The non-null minecraft sound event.
   * @return The sound.
   * @throws IllegalArgumentException If the given object is not a Minecraft sound event.
   */
  Sound fromMinecraftSoundEvent(Object soundEvent);

  /**
   * Retrieves a Minecraft sound event by using the given {@link Sound}.
   *
   * @param sound The non-null sound.
   * @return The sound event.
   */
  Object toMinecraftSoundEvent(Sound sound);

  /**
   * Retrieves a {@link SoundCategory} constant by using the given Minecraft sound category.
   *
   * @param soundCategory The non-null minecraft sound category.
   * @return The {@link SoundCategory} constant.
   * @throws IllegalArgumentException If the given object is not a Minecraft sound category.
   */
  SoundCategory fromMinecraftSoundCategory(Object soundCategory);

  /**
   * Retrieves a {@link Sound} by using the given Minecraft sound event.
   *
   * @param soundCategory The non-null minecraft sound event.
   * @return The sound category constant.
   */
  Object toMinecraftSoundCategory(SoundCategory soundCategory);

}
