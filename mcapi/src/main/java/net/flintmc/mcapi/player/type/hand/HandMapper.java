package net.flintmc.mcapi.player.type.hand;

/** Mapper between the Minecraft hand, handSide and the Flint {@link Hand}, {@link Hand.Side}. */
public interface HandMapper {

  /**
   * Retrieves a {@link Hand} constant by using the given Minecraft hand.
   *
   * @param hand The non-null minecraft hand.
   * @return The {@link Hand} constant.
   * @throws IllegalArgumentException If the given object is not a Minecraft hand.
   */
  Hand fromMinecraftHand(Object hand);

  /**
   * Retrieves a Minecraft hand constant by using the given {@link Hand} .
   *
   * @param hand The non-null hand.
   * @return The hand constant.
   */
  Object toMinecraftHand(Hand hand);

  /**
   * Retrieves a {@link Hand.Side} constant by using the given Minecraft hand side.
   *
   * @param handSide The non-null minecraft hand side.
   * @return The {@link Hand.Side} constant.
   * @throws IllegalArgumentException If the given object is not a Minecraft hand side.
   */
  Hand.Side fromMinecraftHandSide(Object handSide);

  /**
   * Retrieves a Minecraft hand side constant by using the given {@link Hand.Side} .
   *
   * @param handSide The non-null minecraft hand side.
   * @return The hand side constant.
   */
  Object toMinecraftHandSide(Hand.Side handSide);
}
