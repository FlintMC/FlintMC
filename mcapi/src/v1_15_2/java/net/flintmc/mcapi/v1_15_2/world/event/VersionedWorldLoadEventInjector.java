package net.flintmc.mcapi.v1_15_2.world.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.mappings.ClassMappingProvider;

@Singleton
public class VersionedWorldLoadEventInjector {

  private static final String LISTENER = "net.minecraft.world.chunk.listener.IChunkStatusListener";

  private final ClassMappingProvider mappingProvider;

  @Inject
  private VersionedWorldLoadEventInjector(ClassMappingProvider mappingProvider) {
    this.mappingProvider = mappingProvider;
  }

  @ClassTransform("net.minecraft.world.server.ChunkManager")
  public void transformChunkManager(ClassTransformContext context) throws CannotCompileException {
    CtClass transforming = context.getCtClass();
    CtConstructor constructor = transforming.getDeclaredConstructors()[0];

    constructor.insertBeforeBody(
        String.format(
            "%s delegate = $9; $9 = new %s(delegate, $1.%s().%s());",
            LISTENER,
            DelegatingChunkStatusListener.class.getName(),
            this.mappingProvider
                .get("net.minecraft.world.World")
                .getMethod("getWorldInfo")
                .getName(),
            this.mappingProvider
                .get("net.minecraft.world.storage.WorldInfo")
                .getMethod("getWorldName")
                .getName()));
  }
}
