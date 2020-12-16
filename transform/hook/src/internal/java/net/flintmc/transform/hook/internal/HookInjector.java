package net.flintmc.transform.hook.internal;

import com.google.inject.name.Named;
import net.flintmc.transform.hook.Hook;

public interface HookInjector {

  Object notifyHook(
      @Named("instance") Object instance,
      @Named("args") Object[] args,
      Hook.ExecutionTime executionTime);
}
