package net.flintmc.mcapi.v1_15_2.world.event;

import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;

@Singleton
public class WorldLoadEventInjector {

  private static final String LISTENER = "net.minecraft.world.chunk.listener.IChunkStatusListener";

  @ClassTransform("net.minecraft.world.server.ChunkManager")
  public void transformChunkManager(ClassTransformContext context) throws CannotCompileException {
    CtClass transforming = context.getCtClass();
    CtConstructor constructor = transforming.getDeclaredConstructors()[0];
    constructor.insertBeforeBody(
        String.format(
            "%s delegate = $9; $9 = new %s(delegate, $1.getWorldInfo().getWorldName());",
            LISTENER, DelegatingChunkStatusListener.class.getName()));
  }
}
