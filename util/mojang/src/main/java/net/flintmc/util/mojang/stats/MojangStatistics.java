package net.flintmc.util.mojang.stats;

public interface MojangStatistics {

  long getTotal();

  long getTotalInLast24H();

  double getSalesPerSecond();
}
