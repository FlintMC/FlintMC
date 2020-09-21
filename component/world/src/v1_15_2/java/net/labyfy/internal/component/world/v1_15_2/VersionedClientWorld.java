package net.labyfy.internal.component.world.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.Player;
import net.labyfy.component.world.ClientWorld;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 1.15.2 implementation of {@link ClientWorld}
 */
@Singleton
@Implement(value = ClientWorld.class, version = "1.15.2")
public class VersionedClientWorld implements ClientWorld {

  private final List<Player> players;
  private final Scoreboard scoreboard;

  @Inject
  public VersionedClientWorld(Scoreboard scoreboard) {
    this.scoreboard = scoreboard;
    this.players = new ArrayList<>();
  }

  @Override
  public int getEntityCount() {
    return Minecraft.getInstance().world.getCountLoadedEntities();
  }

  @Override
  public boolean addPlayer(Player player) {
    return this.players.add(player);
  }

  @Override
  public boolean removePlayer(UUID uniqueId) {
    return this.players.removeIf(predicatePlayer ->
            predicatePlayer
                    .getGameProfile()
                    .getUniqueId()
                    .equals(uniqueId)
    );
  }

  @Override
  public List<Player> getPlayers() {
    return this.players;
  }

  @Override
  public int getPlayerCount() {
    return Minecraft.getInstance().world.getPlayers().size();
  }

  @Override
  public long getTime() {
    return Minecraft.getInstance().world.getDayTime();
  }

  @Override
  public Scoreboard getScoreboard() {
    return this.scoreboard;
  }
}
