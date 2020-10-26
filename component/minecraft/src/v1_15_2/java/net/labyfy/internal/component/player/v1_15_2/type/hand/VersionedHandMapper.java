package net.labyfy.internal.component.player.v1_15_2.type.hand;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.type.hand.Hand;
import net.labyfy.component.player.type.hand.HandMapper;
import net.minecraft.util.HandSide;

/**
 * 1.15.2 implementation of {@link HandMapper}.
 */
@Singleton
@Implement(value = HandMapper.class, version = "1.15.2")
public class VersionedHandMapper implements HandMapper {

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand fromMinecraftHand(Object hand) {
    if (!(hand instanceof net.minecraft.util.Hand)) {
      throw new IllegalArgumentException("");
    }

    net.minecraft.util.Hand minecraftHand = (net.minecraft.util.Hand) hand;

    return minecraftHand == net.minecraft.util.Hand.OFF_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftHand(Hand hand) {
    return hand == Hand.OFF_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand.Side fromMinecraftHandSide(Object handSide) {
    if (!(handSide instanceof HandSide)) {
      throw new IllegalArgumentException("");
    }

    HandSide minecraftHandSide = (HandSide) handSide;

    return minecraftHandSide == HandSide.RIGHT ? Hand.Side.RIGHT : Hand.Side.LEFT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftHandSide(Hand.Side handSide) {
    return handSide == Hand.Side.RIGHT ? Hand.Side.RIGHT : Hand.Side.LEFT;
  }
}
