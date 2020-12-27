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

package net.flintmc.framework.eventbus.internal.method.subscribable;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.internal.method.handler.EventMethodHandler;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.eventbus.internal.method.handler.EventMethodRegistry;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.Service.State;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;
import net.flintmc.transform.minecraft.MinecraftTransformer;

/**
 * Finds and transforms events, this needs to be called before the assisted factories and
 * implementations are called because they would load classes that should get transformed.
 */
@Singleton
@MinecraftTransformer(implementations = false)
@Service(value = Subscribable.class, priority = -150000, state = State.PRE_INIT)
public class SubscribableService implements LateInjectedTransformer, ServiceHandler<Subscribable> {

  private static final String PHASE_NAME = Subscribe.Phase.class.getName();

  private final ClassPool pool;
  private final EventMethodRegistry methodRegistry;

  private final CtClass eventClass;

  private final Map<String, EventTransform> pendingTransforms;
  private final Map<String, Phase[]> missingFields;
  private final AtomicInteger idCounter;

  @Inject
  private SubscribableService(EventMethodRegistry methodRegistry)
      throws NotFoundException {
    this.methodRegistry = methodRegistry;

    // create our own ClassPool to not clash with the ClassTransformService
    this.pool = new ClassPool();
    this.pool.appendSystemPath();
    this.eventClass = this.pool.get(Event.class.getName());

    this.idCounter = new AtomicInteger();
    this.pendingTransforms = new ConcurrentHashMap<>();
    this.missingFields = new ConcurrentHashMap<>();
  }

  @Override
  public void discover(AnnotationMeta<Subscribable> meta) throws ServiceNotFoundException {
    CtClass annotated = meta.getClassIdentifier().getLocation();
    try {
      if (!annotated.subtypeOf(this.eventClass)) {
        throw new ServiceNotFoundException(
            "@Subscribable is only valid on classes that implement " + this.eventClass.getName()
                + ", not on " + annotated.getName());
      }
    } catch (NotFoundException exception) {
      throw new ServiceNotFoundException(
          "CtClass not found while discovering @Subscribable for " + annotated.getName(),
          exception);
    }

    this.missingFields.put(annotated.getName(), meta.getAnnotation().value());
  }

  @Override
  public byte[] transform(String className, byte[] bytes) throws ClassTransformException {
    if (!this.shouldGenerateFields(className) && !this.shouldTransformEventClass(className)) {
      return bytes;
    }

    CtClass ctClass;

    try {
      ctClass =
          this.pool.makeClass(
              new ClassFile(new DataInputStream(new ByteArrayInputStream(bytes))), false);

      this.generateFields(ctClass);
      this.transformEventClass(ctClass);

    } catch (IOException | CannotCompileException | ReflectiveOperationException | NotFoundException exception) {
      throw new ClassTransformException("unable to read class", exception);
    }

    try {
      return ctClass.toBytecode();
    } catch (IOException exception) {
      // Basically unreachable.
      throw new ClassTransformException(
          "Unable to write class bytecode to byte array: " + className, exception);
    } catch (CannotCompileException exception) {
      throw new ClassTransformException("Unable to transform class: " + className, exception);
    }
  }

  private boolean shouldGenerateFields(String name) {
    return !this.missingFields.isEmpty() && this.missingFields.containsKey(name);
  }

  private void generateFields(CtClass transforming)
      throws IOException, CannotCompileException, ReflectiveOperationException, NotFoundException {
    if (transforming.getDeclaringClass() != null) {
      this.generateFields(transforming.getDeclaringClass());
      return;
    }

    if (!this.shouldGenerateFields(transforming.getName())) {
      return;
    }

    if (!transforming.subtypeOf(this.eventClass)) {
      return;
    }

    CtClass generating = this.pool
        .makeClass("GeneratedEventContainer_" + this.idCounter.incrementAndGet() + "_"
            + UUID.randomUUID().toString().replace("-", ""));
    generating.addInterface(this.pool.get(EventMethodHandler.class.getName()));

    Map<String, EventTransform> transformsByEvent = this.generateEventMethods(generating);
    this.generateHandlerMethods(generating, transformsByEvent);

    this.defineHandler(generating);

    this.missingFields.clear();
  }

  private void defineHandler(CtClass generated)
      throws IOException, CannotCompileException, ReflectiveOperationException {
    Class<?> definedClass = CtResolver.defineClass(generated);

    this.methodRegistry
        .registerHandler((EventMethodHandler) definedClass.getDeclaredConstructor().newInstance());
  }

  private void generateHandlerMethods(CtClass generating,
      Map<String, EventTransform> transformsByEvent) throws CannotCompileException {
    StringBuilder builder = new StringBuilder();
    builder.append("public java.util.List getMethods(String eventClass) {");
    builder.append("int hash = eventClass.hashCode();");

    transformsByEvent.forEach((eventClass, transform) -> {
      builder.append("if (hash == ").append(eventClass.hashCode()).append(") {");
      builder.append("return ").append(transform.getMethodsField().getName()).append(';');
      builder.append('}');
    });

    builder.append("return null;");
    builder.append('}');

    generating.addMethod(CtNewMethod.make(builder.toString(), generating));
    builder.setLength(0);

    builder.append("public void forEachEvent(java.util.function.BiConsumer consumer) {");

    transformsByEvent.forEach((eventClass, transform) -> {
      builder.append("consumer.accept(\"").append(eventClass).append("\", ");
      builder.append(transform.getMethodsField().getName()).append(");");
    });

    builder.append('}');

    generating.addMethod(CtNewMethod.make(builder.toString(), generating));
  }

  private Map<String, EventTransform> generateEventMethods(CtClass generating)
      throws CannotCompileException {
    Map<String, EventTransform> transformsByEvent = new HashMap<>();

    for (Entry<String, Phase[]> entry : this.missingFields.entrySet()) {
      String eventClass = entry.getKey();
      Phase[] phases = entry.getValue();

      EventTransform transform = this.generateFields(generating, eventClass, phases);

      this.pendingTransforms.put(eventClass, transform);
      transformsByEvent.put(eventClass, transform);
    }

    return transformsByEvent;
  }

  private EventTransform generateFields(CtClass generating, String eventClass, Phase[] phases)
      throws CannotCompileException {
    String suffix = eventClass.replace(".", "_").replace("$", "_");

    CtField methodsField = CtField.make(String.format(
        "public static final java.util.List subscribeMethods_%s"
            + "= new java.util.concurrent.CopyOnWriteArrayList();", suffix),
        generating);
    generating.addField(methodsField);

    CtField unmodifiableMethodsField = CtField.make(String.format(
        "public static final java.util.List unmodifiableSubscribeMethods_%s"
            + "= java.util.Collections.unmodifiableList(%s);", suffix, methodsField.getName()),
        generating);
    generating.addField(unmodifiableMethodsField);

    StringBuilder builder = new StringBuilder("public static final java.util.Collection phases_")
        .append(suffix).append(" = java.util.Arrays.asList(new Object[]{");

    for (int i = 0; i < phases.length; i++) {
      builder.append(PHASE_NAME).append('.').append(phases[i].name());
      if (i != phases.length - 1) {
        builder.append(',');
      }
    }

    builder.append("});");
    CtField phasesField = CtField.make(builder.toString(), generating);

    generating.addField(phasesField);

    return new EventTransform(methodsField, unmodifiableMethodsField, phasesField);
  }

  private boolean shouldTransformEventClass(String name) {
    return !this.pendingTransforms.isEmpty() && this.pendingTransforms.containsKey(name);
  }

  private void transformEventClass(CtClass transforming)
      throws NotFoundException, CannotCompileException {
    if (transforming.getDeclaringClass() != null) {
      this.transformEventClass(transforming.getDeclaringClass());
      return;
    }

    EventTransform transform = this.pendingTransforms.remove(transforming.getName());
    if (transform == null) {
      return;
    }

    transforming.addMethod(
        this.makeGetter("getMethods", transforming, transform.getUnmodifiableMethodsField()));
    transforming.addMethod(
        this.makeGetter("getSupportedPhases", transforming, transform.getPhasesField()));
  }

  private CtMethod makeGetter(String name, CtClass declaring, CtField target)
      throws NotFoundException, CannotCompileException {
    return CtNewMethod.make(String
        .format("public %s %s() { return %s.%s; }", target.getType().getName(), name,
            target.getDeclaringClass().getName(), target.getName()), declaring);
  }

}
