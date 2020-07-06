package net.labyfy.component.transform.minecraft.obfuscate;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.launcher.classloading.RootClassLoader;
import net.labyfy.component.launcher.classloading.common.ClassInformation;
import net.labyfy.component.launcher.classloading.common.CommonClassLoaderHelper;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.transform.asm.ASMUtils;
import net.labyfy.component.transform.launchplugin.LateInjectedTransformer;
import net.labyfy.component.transform.minecraft.MinecraftTransformer;
import net.labyfy.component.transform.minecraft.obfuscate.remap.MinecraftClassRemapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.tree.ClassNode;

import javax.inject.Named;
import java.io.IOException;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.MINECRAFT_INSTRUCTION_OBFUSCATOR_PRIORITY;
import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.MINECRAFT_INSTRUCTION_OBFUSCATOR_ROUND;

/**
 * Deobfuscates all minecraft classes for which mappings are provided
 */
@Singleton
@MinecraftTransformer(priority = Integer.MIN_VALUE)
@AutoLoad(priority = MINECRAFT_INSTRUCTION_OBFUSCATOR_PRIORITY, round = MINECRAFT_INSTRUCTION_OBFUSCATOR_ROUND)
public class MinecraftInstructionObfuscator implements LateInjectedTransformer {

  private final MinecraftClassRemapper minecraftClassRemapper;
  private final RootClassLoader rootClassLoader;
  private final ClassMappingProvider classMappingProvider;
  private final boolean obfuscated;

  @Inject
  private MinecraftInstructionObfuscator(
      MinecraftClassRemapper minecraftClassRemapper, ClassMappingProvider classMappingProvider, @Named("obfuscated") boolean obfuscated) {
    this.minecraftClassRemapper = minecraftClassRemapper;
    this.classMappingProvider = classMappingProvider;
    this.obfuscated = obfuscated;
    assert this.getClass().getClassLoader() instanceof RootClassLoader;
    this.rootClassLoader = (RootClassLoader) getClass().getClassLoader();
  }

  public byte[] transform(String className, byte[] classData) {
    try {

      if (!obfuscated) return classData;
      if (!className.startsWith("net.labyfy")) return classData;

      ClassInformation classInformation =
          CommonClassLoaderHelper.retrieveClass(this.rootClassLoader, className);
      if (classInformation == null) return classData;

      ClassNode classNode = ASMUtils.getNode(classInformation.getClassBytes());
      ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
      ClassVisitor classRemapper = new ClassRemapper(classWriter, minecraftClassRemapper);
      classNode.accept(classRemapper);
      return classWriter.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return classData;
  }

}
