package net.flintmc.util.taskexecutor.internal;

import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.taskexecutor.TickEvent;

@Implement(TickEvent.class)
public class InternalTickEvent implements TickEvent {}
