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

package net.flintmc.render.gui.v1_16_5;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.render.gui.event.ScreenChangedEvent;
import net.flintmc.render.gui.event.ScreenRenderEvent;
import net.flintmc.render.gui.internal.windowing.DefaultWindowManager;
import net.flintmc.render.gui.screen.ScreenName;
import net.flintmc.render.gui.screen.ScreenNameMapper;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.CtClassFilter;
import net.flintmc.transform.javassist.CtClassFilters;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.MethodMapping;
import net.minecraft.client.Minecraft;

/**
 * 1.16.5 Implementation of the gui interceptor
 */
@Singleton
public class VersionedGuiInterceptor {

  private final ClassMappingProvider mappingProvider;
  private final DefaultWindowManager windowManager;
  private final ScreenNameMapper nameMapper;
  private final InjectedFieldBuilder.Factory fieldBuilder;

  @Inject
  private VersionedGuiInterceptor(
      ClassMappingProvider mappingProvider,
      DefaultWindowManager windowManager,
      ScreenNameMapper nameMapper,
      InjectedFieldBuilder.Factory fieldBuilder) {
    this.mappingProvider = mappingProvider;
    this.windowManager = windowManager;
    this.nameMapper = nameMapper;
    this.fieldBuilder = fieldBuilder;
  }

  @PostSubscribe(version = "1.16.5")
  public void hookMinecraftWindowRender(ScreenRenderEvent event) {
    this.windowManager.renderMinecraftWindow();
  }

  @ClassTransform(version = "1.16.5")
  @CtClassFilter(
      className = "net.minecraft.client.gui.screen.Screen",
      value = CtClassFilters.SUBCLASS_OF)
  private void hookScreenRender(ClassTransformContext context)
      throws CannotCompileException, NotFoundException {
    MethodMapping renderMapping =
        mappingProvider
            .get("net.minecraft.client.gui.IRenderable")
            .getMethod("render",
                ClassPool.getDefault().get("com.mojang.blaze3d.matrix.MatrixStack"),
                CtClass.intType,
                CtClass.intType,
                CtClass.floatType);

    CtClass screenClass = context.getCtClass();

    CtField field =
        this.fieldBuilder
            .create()
            .target(screenClass)
            .inject(DefaultWindowManager.class)
            .generate();

    for (CtMethod method : screenClass.getDeclaredMethods()) {
      if (!method.getName().equals(renderMapping.getName())) {
        continue;
      }

      method.insertBefore(
          "if("
              + field.getName()
              + ".isMinecraftWindowRenderedIntrusively()) {"
              + "   return;"
              + "}");
      break;
    }
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "displayGuiScreen",
      parameters = @Type(typeName = "net.minecraft.client.gui.screen.Screen"),
      executionTime = Hook.ExecutionTime.AFTER,
      version = "1.16.5")
  public void hookScreenChanged() {
    ScreenName screenName = this.nameMapper.fromObject(Minecraft.getInstance().currentScreen);
    this.windowManager.fireEvent(-1, window -> new ScreenChangedEvent(window, screenName));
  }
}
