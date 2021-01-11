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

package net.flintmc.mcapi.v1_16_4.entity.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeIterator;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.MethodMapping;

@Singleton
public class VersionedEntityRenderNameEventInjectorTransformer {

  // fontRenderer.renderString(displayName, f2, (float)i, -1, false, matrix4f, bufferIn, false, 0,
  // packedLight)
  private static final int[] NAME_RENDER_SEQUENCE =
      new int[]{
          Bytecode.ILOAD,
          Bytecode.IFEQ,
          Bytecode.ALOAD,
          Bytecode.ALOAD_2,
          Bytecode.FLOAD,
          Bytecode.ILOAD,
          Bytecode.I2F,
          Bytecode.ICONST_M1,
          Bytecode.ICONST_0,
          Bytecode.ALOAD,
          Bytecode.ALOAD,
          Bytecode.ICONST_0,
          Bytecode.ICONST_0,
          Bytecode.ILOAD,
          Bytecode.INVOKEVIRTUAL,
          Bytecode.POP
      };

  private final ClassPool pool;
  private final ClassMappingProvider mappingProvider;
  private final InjectedFieldBuilder.Factory fieldBuilderFactory;

  @Inject
  private VersionedEntityRenderNameEventInjectorTransformer(
      ClassPool pool,
      ClassMappingProvider mappingProvider,
      InjectedFieldBuilder.Factory fieldBuilderFactory) {
    this.fieldBuilderFactory = fieldBuilderFactory;
    this.pool = pool;
    this.mappingProvider = mappingProvider;
  }

  @ClassTransform(value = "net.minecraft.client.renderer.entity.EntityRenderer", version = "1.16.4")
  public void transformRenderName(ClassTransformContext context)
      throws NotFoundException, CannotCompileException, BadBytecode {
    CtClass[] params =
        new CtClass[]{
            this.pool.get(this.mappingProvider.get("net.minecraft.entity.Entity").getName()),
            this.pool.get("net.minecraft.util.text.ITextComponent"),
            this.pool.get(
                this.mappingProvider.get("com.mojang.blaze3d.matrix.MatrixStack").getName()),
            this.pool.get(
                this.mappingProvider
                    .get("net.minecraft.client.renderer.IRenderTypeBuffer")
                    .getName()),
            this.pool.get("int")
        };

    CtClass transforming = context.getCtClass();
    ClassMapping mapping = this.mappingProvider.get(transforming.getName());
    MethodMapping methodMapping = mapping != null ? mapping.getMethod("renderName", params) : null;

    CtField injected =
        this.fieldBuilderFactory
            .create()
            .target(transforming)
            .inject(VersionedEntityRenderNameEventInjector.class)
            .generate();

    CtMethod method =
        transforming.getDeclaredMethod(
            methodMapping != null ? methodMapping.getName() : "renderName", params);

    this.insert(method, injected);
  }

  private void insert(CtMethod method, CtField injected)
      throws BadBytecode, CannotCompileException {
    int i = 0;

    CodeIterator iterator = method.getMethodInfo().getCodeAttribute().iterator();
    while (iterator.hasNext()) {
      int opcode = iterator.byteAt(iterator.next());
      if (NAME_RENDER_SEQUENCE[i] != opcode) {
        i = 0;
        continue;
      }

      ++i;
      if (i < NAME_RENDER_SEQUENCE.length - 1) {
        continue;
      }

      // found the sequence of opcodes that matches the renderString method in the if clause whether
      // the player is sneaking
      int line = method.getMethodInfo().getLineNumber(iterator.next());

      ClassMapping matrixStack = this.mappingProvider.get("com.mojang.blaze3d.matrix.MatrixStack");
      ClassMapping matrixStackEntry =
          this.mappingProvider.get("com.mojang.blaze3d.matrix.MatrixStack$Entry");
      // Matrix4f matrix = matrixStack.getLast().getMatrix()
      String matrix =
          "$3."
              + matrixStack.getMethod("getLast").getName()
              + "()."
              + matrixStackEntry.getMethod("getMatrix").getName()
              + "()";
      String y = "\"deadmau5\".equals($2) ? -10 : 0";

      // this will be fired whenever the entity will be rendered
      method.insertAt(
          line - 2,
          String.format("%s.renderName($args, %s, %s, false);", injected.getName(), matrix, y));

      // this will only be fired when the entity is not sneaking
      // if the name will not be rendered with the textBackgroundColor being 0,
      // it will not be shown through walls and as if the player would be sneaking
      method.insertAt(
          line,
          String.format("this.%s.renderName($args, %s, %s, true);", injected.getName(), matrix, y));
    }
  }
}
