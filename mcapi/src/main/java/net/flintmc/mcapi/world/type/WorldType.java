package net.flintmc.mcapi.world.type;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents the type of the world.
 */
public interface WorldType {

  /**
   * Retrieves the name of the world type.
   *
   * @return The world type's name.
   */
  String getName();

  /**
   * Retrieves the serialization identifier of the world type.
   *
   * @return The world type's serialization identifier.
   */
  String getSerializationIdentifier();

  /**
   * Retrieves the version of the world type.
   *
   * @return The world type's version.
   */
  int getVersion();

  /**
   * Whether the world type has a custom configuration.
   *
   * @return {@code true} if the world type has a custom configuration, otherwise {@code false}.
   */
  boolean hasCustomConfigurations();

  /**
   * Changes whether the world type has a custom configuration.
   *
   * @param customConfigurations {@code true} if the world type has a custom configuration,
   *                             otherwise {@code false}.
   */
  void setCustomConfigurations(boolean customConfigurations);

  /**
   * Whether the world type can be created.
   *
   * @return {@code true} if the world can be created.
   */
  boolean canBeCreated();

  /**
   * Changes whether the world type can be created.
   *
   * @param canBeCreated {@code true} if the world type can be created, otherwise {@code false}.
   */
  void setCanBeCreated(boolean canBeCreated);

  /**
   * Whether the world type is versioned.
   *
   * @return {@code true} if the world type is versioned, otherwise {@code false}.
   */
  boolean isVersioned();

  /**
   * Enables the versioned of the world type.
   */
  void enableVersioned();

  /**
   * Retrieves the identifier of the world type.
   *
   * @return The world type's identifier.
   */
  int getIdentifier();

  /**
   * Whether the world type has an information notice.
   *
   * @return {@code true} if the world type has an information notice, otherwise {@code false}.
   */
  boolean hasInfoNotice();

  /**
   * Enables the information notice of the world type.
   */
  void enabledInfoNotice();

  /**
   * A factory class for creating {@link WorldType}'s.
   */
  @AssistedFactory(WorldType.class)
  interface Factory {

    /**
     * Creates a new {@link WorldType} with the given parameters.
     *
     * @param identifier The identifier for the world type.
     * @param name       The name for the world type.
     * @return A created world type.
     */
    WorldType create(
        @Assisted("identifier") int identifier,
        @Assisted("name") String name);

    /**
     * Creates a new {@link WorldType} with the given parameters.
     *
     * @param identifier The identifier for the world type.
     * @param name       The name for the world type.
     * @param version    The version for the world type.
     * @return A created world type.
     */
    WorldType create(
        @Assisted("identifier") int identifier,
        @Assisted("name") String name,
        @Assisted("version") int version);

    /**
     * Creates a new {@link WorldType} with the given parameters.
     *
     * @param identifier           The identifier for the world type.
     * @param name                 The name for the world type.
     * @param serializedIdentifier The serialized identifier for the world type.
     * @param version              The version for the world type.
     * @return A created world type.
     */
    WorldType create(
        @Assisted("identifier") int identifier,
        @Assisted("name") String name,
        @Assisted("serializedIdentifier") String serializedIdentifier,
        @Assisted("version") int version);

    /**
     * Creates a new {@link WorldType} with the given parameters.
     *
     * @param identifier           The identifier for the world type.
     * @param name                 The name for the world type.
     * @param serializedIdentifier The serialized identifier for the world type.
     * @param version              The version for the world type.
     * @param canBeCreated         {@code true} if the world type can be created, otherwise {@code
     *                             false}.
     * @param versioned            {@code true} if the world type is versioned, otherwise {@code
     *                             false}.
     * @param hasInfoNotice        {@code true} if the world type has an information notice,
     *                             otherwise {@code false}.
     * @param customConfiguration  {@code true} if the world type has a custom configuration,
     *                             otherwise {@code false}.
     * @return A created world type.
     */
    WorldType create(
        @Assisted("identifier") int identifier,
        @Assisted("name") String name,
        @Assisted("serializedIdentifier") String serializedIdentifier,
        @Assisted("version") int version,
        @Assisted("canBeCreated") boolean canBeCreated,
        @Assisted("versioned") boolean versioned,
        @Assisted("hasInfoNotice") boolean hasInfoNotice,
        @Assisted("customConfiguration") boolean customConfiguration);
  }

}
