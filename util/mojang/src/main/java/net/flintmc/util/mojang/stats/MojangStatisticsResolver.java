package net.flintmc.util.mojang.stats;

import java.util.concurrent.CompletableFuture;

public interface MojangStatisticsResolver {

  CompletableFuture<MojangStatistics> resolve(MojangStatisticsType type);
}
