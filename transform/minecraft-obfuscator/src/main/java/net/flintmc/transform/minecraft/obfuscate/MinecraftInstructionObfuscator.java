package net.flintmc.transform.minecraft.obfuscate;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.launcher.classloading.RootClassLoader;
import net.flintmc.launcher.classloading.common.ClassInformation;
import net.flintmc.launcher.classloading.common.CommonClassLoaderHelper;
import net.flintmc.transform.asm.ASMUtils;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;
import net.flintmc.transform.minecraft.MinecraftTransformer;
import net.flintmc.transform.minecraft.obfuscate.remap.MinecraftClassRemapper;
import net.flintmc.util.mappings.ClassMappingProvider;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

/** Deobfuscates all minecraft classes for which mappings are provided */
@Singleton
@MinecraftTransformer(priority = Integer.MIN_VALUE)
public class MinecraftInstructionObfuscator implements LateInjectedTransformer {

  private final MinecraftClassRemapper minecraftClassRemapper;
  private final RootClassLoader rootClassLoader;
  private final ClassMappingProvider classMappingProvider;
  private final boolean obfuscated;

  @Inject
  private MinecraftInstructionObfuscator(
      MinecraftClassRemapper minecraftClassRemapper,
      ClassMappingProvider classMappingProvider,
      @Named("obfuscated") boolean obfuscated) {
    this.minecraftClassRemapper = minecraftClassRemapper;
    this.classMappingProvider = classMappingProvider;
    this.obfuscated = obfuscated;
    assert this.getClass().getClassLoader() instanceof RootClassLoader;
    this.rootClassLoader = (RootClassLoader) getClass().getClassLoader();
  }

  @Override
  public byte[] transform(String className, byte[] classData) throws ClassTransformException {
    if (!obfuscated) return classData;
    if (!className.startsWith("net.flintmc")) return classData;

    ClassInformation classInformation;

    try {
      classInformation = CommonClassLoaderHelper.retrieveClass(this.rootClassLoader, className);
    } catch (IOException exception) {
      throw new ClassTransformException(
          "Unable to retrieve class metadata: " + className, exception);
    }

    if (classInformation == null) return classData;

    ClassNode classNode = ASMUtils.getNode(classInformation.getClassBytes());
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    ClassVisitor classRemapper = new ClassRemapper(classWriter, minecraftClassRemapper);
    classNode.accept(classRemapper);
    return classWriter.toByteArray();
  }
}
