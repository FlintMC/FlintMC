package net.labyfy.component.entity;

import net.labyfy.component.entity.name.Nameable;

/**
 * Represents the Minecraft entity.
 */
public interface Entity extends Nameable {

  /**
   * Retrieves the team color as an {@link Integer} of this entity.
   *
   * @return The team color as an {@link Integer}.
   */
  int getTeamColor();

  default boolean isSpectator() {
    return false;
  }

  /**
   * Detach all passengers from this entity and stop the riding if the entity itself is a passenger.
   */
  void detach();

  void setPacketCoordinates(double x, double y, double z);


  /**
   * An enumeration representing all classifications for entities.
   */
  enum Classification {

    MONSTER("monster", 70, false, false),
    CREATURE("creature", 10, true, true),
    AMBIENT("ambient", 15, true, false),
    WATER_CREATURE("water_creature", 15, true, false),
    MISC("misc", 15, true, true);

    private final String name;
    private final int maxNumberOfCreature;
    private final boolean peacefulCreature;
    private final boolean animal;

    Classification(String name, int maxNumberOfCreature, boolean peacefulCreature, boolean animal) {
      this.name = name;
      this.maxNumberOfCreature = maxNumberOfCreature;
      this.peacefulCreature = peacefulCreature;
      this.animal = animal;
    }

    public String getName() {
      return name;
    }

    public int getMaxNumberOfCreature() {
      return maxNumberOfCreature;
    }

    public boolean isPeacefulCreature() {
      return peacefulCreature;
    }

    public boolean isAnimal() {
      return animal;
    }
  }


}
