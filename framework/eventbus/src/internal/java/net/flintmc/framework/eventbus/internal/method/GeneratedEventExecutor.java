package net.flintmc.framework.eventbus.internal.method;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.eventbus.method.SubscribeMethod;

public interface GeneratedEventExecutor {

  void invoke(Event event, Phase phase, SubscribeMethod holderMethod);
}
