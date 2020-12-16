package net.flintmc.mcapi.internal.world.scoreboard.listener;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

public interface ObjectiveChangeListener {

  void changeDisplayName(Objective objective, ChatComponent displayName);

  void changeRenderType(Objective objective, RenderType renderType);
}
