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

package net.flintmc.transform.minecraft.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.launcher.LaunchController;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.ClassIdentifier;
import net.flintmc.transform.launchplugin.FlintLauncherPlugin;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;
import net.flintmc.transform.minecraft.MinecraftTransformer;

@Singleton
@Service(value = MinecraftTransformer.class, priority = -20000, state = Service.State.AFTER_IMPLEMENT)
public class MinecraftTransformerService implements ServiceHandler<MinecraftTransformer> {

  @Inject
  private MinecraftTransformerService() {
    LaunchController.getInstance()
        .getRootLoader()
        .excludeFromModification("net.flintmc.transform.");
  }

  @Override
  public void discover(AnnotationMeta<MinecraftTransformer> identifierMeta)
      throws ServiceNotFoundException {
    CtClass target = identifierMeta.<ClassIdentifier>getIdentifier().getLocation();
    try {
      if (!target.subtypeOf(target.getClassPool().get(LateInjectedTransformer.class.getName()))) {
        throw new ServiceNotFoundException(
            new IllegalStateException(
                "Class "
                    + target.getName()
                    + " does not implement "
                    + LateInjectedTransformer.class.getName()));
      }
      FlintLauncherPlugin.getInstance()
          .registerTransformer(
              identifierMeta.getAnnotation().priority(),
              InjectionHolder.getInjectedInstance(
                  CtResolver.get(identifierMeta.<ClassIdentifier>getIdentifier().getLocation())));
    } catch (NotFoundException e) {
      throw new ServiceNotFoundException(e);
    }
  }
}
