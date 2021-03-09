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

package net.flintmc.processing;

import com.squareup.javapoet.*;
import net.flintmc.processing.exception.ProcessingException;
import org.apache.commons.io.IOUtils;
import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Representation of the internal state of the {@link FlintAnnotationProcessor} to provide a more
 * convenient interface than the java processing environment.
 *
 * <p>This class is a singleton.
 */
public class ProcessorState {

  /**
   * This option is passed to the processor by the gradle plugin, SO IF THIS IS EVER CHANGED, KEEP
   * IT IN SYNC WITH THE PLUGIN!
   *
   * <p>Contains the group id of the package currently being compiled
   */
  public static final String PACKAGE_GROUP_OPTION = "net.flintmc.package.group";

  /**
   * This option is passed to the processor by the gradle plugin, SO IF THIS IS EVER CHANGED, KEEP
   * IT IN SYNC WITH THE PLUGIN!
   *
   * <p>Contains the name of the package currently being compiled
   */
  public static final String PACKAGE_NAME_OPTION = "net.flintmc.package.name";

  /**
   * This option is passed to the processor by the gradle plugin, SO IF THIS IS EVER CHANGED, KEEP
   * IT IN SYNC WITH THE PLUGIN!
   *
   * <p>Contains the version of the package currently being compiled
   */
  public static final String PACKAGE_VERSION_OPTION = "net.flintmc.package.version";

  /**
   * This option is passed to the processor by the gradle plugin, SO IF THIS IS EVER CHANGED, KEEP
   * IT IN SYNC WITH THE PLUGIN!
   *
   * <p>Contains the source set currently being compiled
   */
  public static final String SOURCE_SET_OPTION = "net.flintmc.sourceSet";

  // Singleton instance
  private static ProcessorState instance;

  // The child processors of the FlintAnnotationProcessor, discovered via a ServiceLoader
  private final Collection<Processor> processors;
  private final Map<Processor, Set<String>> registeredOptions;

  // State of the java processing environment
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment currentRoundEnvironment;

  private String packageName;
  private String packageGroup;
  private String packageVersion;
  private String sourceSet;

  private int generationCounter = 0;

  /**
   * Constructs a new {@link ProcessorState}, setting the instance field and loading all available
   * {@link Processor} using a {@link ServiceLoader}
   */
  public ProcessorState() {
    this.processors = new HashSet<>();
    this.registeredOptions = new HashMap<>();
    ServiceLoader.load(Processor.class, getClass().getClassLoader()).forEach(processors::add);
    instance = this;
  }

  /**
   * Getter for retrieving the singleton instance of the class
   *
   * @return The singleton instance
   */
  public static ProcessorState getInstance() {
    return instance;
  }

  /**
   * Called by the {@link FlintAnnotationProcessor} to set the {@link ProcessingEnvironment}. May
   * only be called once.
   *
   * @param processingEnvironment The processing environment used for the entire duration of
   *                              processing
   * @throws IllegalStateException If the processing environment has been set already
   */
  public void init(ProcessingEnvironment processingEnvironment) {
    if (this.processingEnvironment != null) {
      throw new IllegalStateException("ProcessorState initialized already");
    }

    this.processingEnvironment = processingEnvironment;
    registerOptions();

    Map<String, String> environmentOptions = processingEnvironment.getOptions();
    this.packageGroup = toJavaIdentifier(
        environmentOptions.getOrDefault(PACKAGE_GROUP_OPTION, "undefined"));
    this.packageName = toJavaIdentifier(
        environmentOptions.getOrDefault(PACKAGE_NAME_OPTION, "undefined"));
    this.packageVersion = toJavaIdentifier(
        environmentOptions.getOrDefault(PACKAGE_VERSION_OPTION, "undefined"));
    this.sourceSet = toJavaIdentifier(environmentOptions.getOrDefault(SOURCE_SET_OPTION, "main"));

    // Let registered processors handle their respective options
    for (Map.Entry<Processor, Set<String>> entry : registeredOptions.entrySet()) {
      Processor processor = entry.getKey();
      Set<String> options = entry.getValue();

      Map<String, String> optionValues = new HashMap<>();

      // Copy all requested values over
      for (String requested : options) {
        if (environmentOptions.containsKey(requested)) {
          optionValues.put(requested, environmentOptions.get(requested));
        }
      }

      // Notify the processor
      processor.handleOptions(optionValues);
    }
  }

  /**
   * Called by the {@link FlintAnnotationProcessor} to begin a new round of processing and update
   * the used {@link RoundEnvironment} to the new one for this round.
   *
   * @param environment The environment used for this round
   */
  public void round(RoundEnvironment environment) {
    currentRoundEnvironment = environment;
  }

  /**
   * Called by the {@link FlintAnnotationProcessor} to signal the child processors to accept the
   * given annotation.
   *
   * @param annotation The annotation found by the {@link FlintAnnotationProcessor}
   */
  public void process(TypeElement annotation) {
    processors.forEach(processor -> processor.accept(annotation));
  }

  /**
   * Retrieves the processing environment, this will always be the same one for the duration of the
   * compiler run.
   *
   * @return The processing environment
   */
  public ProcessingEnvironment getProcessingEnvironment() {
    return processingEnvironment;
  }

  /**
   * Retrieves the current round environment, this will change every once and then, so <b>cache the
   * result of this method only with caution</b>!
   *
   * @return The current round environment
   */
  public RoundEnvironment getCurrentRoundEnvironment() {
    return currentRoundEnvironment;
  }

  /**
   * Called by the {@link FlintAnnotationProcessor} to signal that the last round is running and
   * everything should be finalized and written to disk.
   */
  public void flushRound() {
    for (Processor processor : processors) {
      if (!processor.shouldFlush()) {
        continue;
      }

      MethodSpec.Builder method = processor.createMethod();
      Filer filer = processingEnvironment.getFiler();
      ClassName autoLoadProviderClass = processor.getGeneratedClassSuperClass();

      // Adding a public constructor is important, else the ServiceLoader might fail
      MethodSpec constructor =
          MethodSpec.methodBuilder("<init>").addModifiers(Modifier.PUBLIC).build();

      processor.flush(method);

      MethodSpec registerAutoLoadMethod = method.build();

      // Create an @Generated annotation and fill it with the full
      // qualified name of the FlintAnnotationProcessor
      AnnotationSpec generatedAnnotation =
          AnnotationSpec.builder(Generated.class)
              .addMember("value", "$S", FlintAnnotationProcessor.class.getName())
              .build();

      // Generate a class with a random name to avoid collisions
      String generatedClassName =
          processor.getGeneratedClassSuperClass().simpleName()
              + "_"
              + packageGroup
              + "$"
              + packageName
              + "$"
              + packageVersion
              + "$"
              + sourceSet
              + "_"
              + (generationCounter++);

      // Generate the final class
      TypeSpec generatedType =
          TypeSpec.classBuilder(generatedClassName)
              .addAnnotation(generatedAnnotation)
              .addModifiers(Modifier.PUBLIC)
              .addSuperinterface(autoLoadProviderClass)
              .addMethod(constructor)
              .addMethod(registerAutoLoadMethod)
              .build();

      // Write the class into the net.flintmc.autogen package, the random name should ensure no
      // collisions
      JavaFile finishedFile = JavaFile.builder("net.flintmc.autogen", generatedType).build();
      try {
        finishedFile.writeTo(filer);
      } catch (IOException exception) {
        throw new ProcessingException(
            "Failed to write to final file due to IOException", exception);
      }

      // We also need to generate the service file for auto loading
      String resourceFile = "META-INF/services/" + autoLoadProviderClass.reflectionName();

      Set<String> services = new HashSet<>();

      // Try to get a stream of the existing file to merge it our new class
      try (InputStream stream =
          filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile).openInputStream()) {
        services.addAll(IOUtils.readLines(stream, StandardCharsets.UTF_8));
      } catch (IOException ignored) {
        // File does not exist or is not readable, there is no way to check that
      }

      services.add("net.flintmc.autogen." + generatedClassName);

      // Write the merged file back to disk
      try (OutputStream stream =
          filer
              .createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile)
              .openOutputStream()) {
        IOUtils.writeLines(services, System.lineSeparator(), stream, StandardCharsets.UTF_8);
      } catch (IOException exception) {
        throw new ProcessingException("Failed to update " + resourceFile, exception);
      }
    }
  }

  /**
   * Collects all options supported by all sub-processors.
   *
   * @return The collected options
   */
  public Set<String> collectSupportedOptions() {
    Set<String> allOptions = new HashSet<>();

    for (Set<String> otherOptions : registeredOptions.values()) {
      allOptions.addAll(otherOptions);
    }

    // Own options
    allOptions.add(PACKAGE_GROUP_OPTION);
    allOptions.add(PACKAGE_NAME_OPTION);
    allOptions.add(PACKAGE_VERSION_OPTION);
    allOptions.add(SOURCE_SET_OPTION);

    return allOptions;
  }

  /**
   * Registers all options supported by processors.
   */
  private void registerOptions() {
    for (Processor processor : processors) {
      Set<String> processorOptions = processor.options();

      if (!processorOptions.isEmpty()) {
        registeredOptions.put(processor, processorOptions);
      }
    }
  }

  /**
   * Converts an arbitrary string into a Java class literal using best-effort replacement.
   *
   * @param literal The literal to convert
   * @return The converted literal as an identifier
   */
  private String toJavaIdentifier(String literal) {
    return literal.replace('.', '$').replaceAll("[^A-z0-9$]", "_");
  }
}
