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

package net.flintmc.mcapi.internal.server.payload;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.util.commons.javassist.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.mcapi.server.payload.PayloadChannel;
import net.flintmc.mcapi.server.payload.PayloadChannelService;
import net.flintmc.metaprogramming.AnnotationMeta;
import net.flintmc.metaprogramming.identifier.MethodIdentifier;
import org.apache.logging.log4j.Logger;

@Singleton
@Service(PayloadChannel.class)
public class DefaultPayloadChannelServiceHandler implements ServiceHandler<PayloadChannel> {

  private static final String PAYLOAD_CHANNEL_LISTENER_CLASS = "net.flintmc.mcapi.server.payload.PayloadChannelListener";
  private final Logger logger;
  private final PayloadChannelService payloadChannelService;

  @Inject
  private DefaultPayloadChannelServiceHandler(@InjectLogger Logger logger,
      PayloadChannelService payloadChannelService) {
    this.logger = logger;
    this.payloadChannelService = payloadChannelService;
  }

  @Override
  public void discover(AnnotationMeta<PayloadChannel> annotationMeta)
      throws ServiceNotFoundException {

    PayloadChannel payloadChannel = annotationMeta.getAnnotation();
    MethodIdentifier methodIdentifier = annotationMeta.getMethodIdentifier();
    CtClass declaringClass = methodIdentifier.getLocation().getDeclaringClass();

    boolean shouldRegister = false;

    try {
      for (CtClass anInterface : declaringClass.getInterfaces()) {
        if (anInterface.getName().equals(PAYLOAD_CHANNEL_LISTENER_CLASS)) {
          shouldRegister = true;
        }
      }
    } catch (NotFoundException exception) {
      this.logger.error(
          "No interfaces were found for the given class {}", declaringClass.getName(), exception);
      return;
    }

    if (!shouldRegister) {
      this.logger.warn(
          "The payload channel listener `{}` cannot be registered because it does not implement the interface: {}!",
          declaringClass.getName(),
          PAYLOAD_CHANNEL_LISTENER_CLASS);
      return;
    }

    this.payloadChannelService.registerPayloadChannel(
        payloadChannel.namespace(),
        payloadChannel.path(),
        InjectionHolder.getInjectedInstance(CtResolver.get(declaringClass)));

    this.logger.info("The payload channel {}:{} was registered!", payloadChannel.namespace(),
        payloadChannel.path());
  }
}
