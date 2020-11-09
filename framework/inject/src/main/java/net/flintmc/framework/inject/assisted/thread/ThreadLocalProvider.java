package net.flintmc.framework.inject.assisted.thread;

import com.google.inject.Provider;

public class ThreadLocalProvider extends ThreadLocal<Object> implements Provider<Object> {

  @Override
  protected Object initialValue() {
    throw new IllegalStateException(
        "Cannot use optimized @Assisted provider outside the scope of the constructor.");
  }
}
