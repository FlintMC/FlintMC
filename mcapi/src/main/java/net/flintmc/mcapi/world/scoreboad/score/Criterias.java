package net.flintmc.mcapi.world.scoreboad.score;

import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

/**
 * Criterias that can be used by {@link Objective}'s.
 */
public final class Criterias {

  private static Criteria.Factory criteriaFactory;
  public static final Criteria DUMMY = create("dummy");
  public static final Criteria TRIGGER = create("trigger");
  public static final Criteria DEATH_COUNT = create("death_count");
  public static final Criteria PLAYER_KILL_COUNT = create("playerKillCount");
  public static final Criteria TOTAL_KILL_COUNT = create("totalKillCount");
  public static final Criteria HEALTH = create("health", RenderType.HEARTS);
  public static final Criteria FOOD = create("food", RenderType.INTEGER);
  public static final Criteria AIR = create("air", RenderType.INTEGER);
  public static final Criteria ARMOR = create("armor", RenderType.INTEGER);
  public static final Criteria XP = create("xp", RenderType.INTEGER);
  public static final Criteria LEVEL = create("level", RenderType.INTEGER);
  public static final Criteria[] TEAM_KILL = new Criteria[]{
      create("teamkill." + ChatColor.BLACK.getLowerName()),
      create("teamkill." + ChatColor.DARK_BLUE.getLowerName()),
      create("teamkill." + ChatColor.DARK_GREEN.getLowerName()),
      create("teamkill." + ChatColor.DARK_AQUA.getLowerName()),
      create("teamkill." + ChatColor.DARK_RED.getLowerName()),
      create("teamkill." + ChatColor.DARK_PURPLE.getLowerName()),
      create("teamkill." + ChatColor.GOLD.getLowerName()),
      create("teamkill." + ChatColor.GRAY.getLowerName()),
      create("teamkill." + ChatColor.DARK_GRAY.getLowerName()),
      create("teamkill." + ChatColor.BLUE.getLowerName()),
      create("teamkill." + ChatColor.GREEN.getLowerName()),
      create("teamkill." + ChatColor.AQUA.getLowerName()),
      create("teamkill." + ChatColor.RED.getLowerName()),
      create("teamkill." + ChatColor.LIGHT_PURPLE.getLowerName()),
      create("teamkill." + ChatColor.YELLOW.getLowerName()),
      create("teamkill." + ChatColor.WHITE.getLowerName())};
  public static final Criteria[] KILLED_BY_TEAM = new Criteria[]{
      create("killedByTeam." + ChatColor.BLACK.getLowerName()),
      create("killedByTeam." + ChatColor.DARK_BLUE.getLowerName()),
      create("killedByTeam." + ChatColor.DARK_GREEN.getLowerName()),
      create("killedByTeam." + ChatColor.DARK_AQUA.getLowerName()),
      create("killedByTeam." + ChatColor.DARK_RED.getLowerName()),
      create("killedByTeam." + ChatColor.DARK_PURPLE.getLowerName()),
      create("killedByTeam." + ChatColor.GOLD.getLowerName()),
      create("killedByTeam." + ChatColor.GRAY.getLowerName()),
      create("killedByTeam." + ChatColor.DARK_GRAY.getLowerName()),
      create("killedByTeam." + ChatColor.BLUE.getLowerName()),
      create("killedByTeam." + ChatColor.GREEN.getLowerName()),
      create("killedByTeam." + ChatColor.AQUA.getLowerName()),
      create("killedByTeam." + ChatColor.RED.getLowerName()),
      create("killedByTeam." + ChatColor.LIGHT_PURPLE.getLowerName()),
      create("killedByTeam." + ChatColor.YELLOW.getLowerName()),
      create("killedByTeam." + ChatColor.WHITE.getLowerName())};

  /**
   * Retrieves the factory to create criterias.
   *
   * @return The criteria factory.
   */
  private static Criteria.Factory getFactory() {
    if (criteriaFactory == null) {
      criteriaFactory = InjectionHolder.getInjectedInstance(Criteria.Factory.class);
    }

    return criteriaFactory;
  }

  /**
   * Creates a new {@link Criteria} with the given {@code name}.
   *
   * @param name The name of the criteria.
   * @return A created criteria.
   */
  private static Criteria create(String name) {
    return getFactory().create(name);
  }

  /**
   * Creates a new {@link Criteria} with the given {@code name} and the {@code renderType}.
   *
   * @param name       The name of the criteria.
   * @param renderType The render type of the criteria.
   * @return A created critiera.
   */
  private static Criteria create(String name, RenderType renderType) {
    return getFactory().create(name, true, renderType);
  }

}
