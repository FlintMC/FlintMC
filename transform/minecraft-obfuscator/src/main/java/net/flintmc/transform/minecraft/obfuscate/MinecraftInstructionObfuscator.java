/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.transform.minecraft.obfuscate;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import javassist.ClassPool;
import net.flintmc.launcher.classloading.RootClassLoader;
import net.flintmc.launcher.classloading.common.ClassInformation;
import net.flintmc.launcher.classloading.common.CommonClassLoaderHelper;
import net.flintmc.transform.asm.ASMUtils;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;
import net.flintmc.transform.minecraft.MinecraftTransformer;
import net.flintmc.transform.minecraft.obfuscate.remap.MinecraftClassRemapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Deobfuscates all minecraft classes for which mappings are provided
 */
@Singleton
@MinecraftTransformer(priority = Integer.MIN_VALUE)
public class MinecraftInstructionObfuscator implements LateInjectedTransformer {

  private final MinecraftClassRemapper minecraftClassRemapper;
  private final RootClassLoader rootClassLoader;
  private final boolean obfuscated;

  @Inject
  private MinecraftInstructionObfuscator(
      MinecraftClassRemapper minecraftClassRemapper, @Named("obfuscated") boolean obfuscated) {
    this.minecraftClassRemapper = minecraftClassRemapper;
    this.obfuscated = obfuscated;
    assert this.getClass().getClassLoader() instanceof RootClassLoader;
    this.rootClassLoader = (RootClassLoader) getClass().getClassLoader();
  }

  @Override
  public byte[] transform(String className, byte[] classData) throws ClassTransformException {
    if (!obfuscated) {
      return classData;
    }
    if (!className.startsWith("net.flintmc")) {
      return classData;
    }

    ClassInformation classInformation;

    try {
      classInformation = CommonClassLoaderHelper.retrieveClass(this.rootClassLoader, className);
    } catch (IOException exception) {
      throw new ClassTransformException(
          "Unable to retrieve class metadata: " + className, exception);
    }

    if (classInformation == null) {
      return classData;
    }

    ClassNode classNode = ASMUtils.getNode(classInformation.getClassBytes());
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    ClassVisitor classRemapper = new ClassRemapper(classWriter, minecraftClassRemapper);
    classNode.accept(classRemapper);
    return classWriter.toByteArray();
  }
}
