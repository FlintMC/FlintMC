package net.flintmc.util.mojang.stats;

public enum MojangStatisticsType {
  MINECRAFT_SOLD_ITEMS("item_sold_minecraft"),
  PREPAID_CARD_REDEEMED_MINECRAFT("prepaid_card_redeemed_minecraft"),
  COBALT_SOLD_ITEMS("item_sold_cobalt"),
  SCROLLS_SOLD_ITEMS("item_sold_scrolls"),
  PREPAID_CRAD_REDEEMED_COBALT("prepaid_card_redeemed_cobalt"),
  MINECRAFT_DUNGEONS_SOLD_ITEMS("item_sold_dungeons");

  private final String mojangName;

  MojangStatisticsType(String mojangName) {
    this.mojangName = mojangName;
  }

  public String getMojangName() {
    return this.mojangName;
  }
}
