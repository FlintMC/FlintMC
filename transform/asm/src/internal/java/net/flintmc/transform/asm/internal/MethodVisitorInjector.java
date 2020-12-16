package net.flintmc.transform.asm.internal;

import net.flintmc.transform.asm.MethodVisitorContext;

public interface MethodVisitorInjector {

  void notify(MethodVisitorContext context);
}
