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
  // Singleton instance
  private static ProcessorState instance;

  // The child processors of the FlintAnnotationProcessor, discovered via a ServiceLoader
  private final Collection<Processor> processors;

  // State of the java processing environment
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment currentRoundEnvironment;

  /**
   * Constructs a new {@link ProcessorState}, setting the instance field and loading all available
   * {@link Processor} using a {@link ServiceLoader}
   */
  public ProcessorState() {
    this.processors = new HashSet<>();
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
   *     processing
   * @throws IllegalStateException If the processing environment has been set already
   */
  public void init(ProcessingEnvironment processingEnvironment) {
    if (this.processingEnvironment != null) {
      throw new IllegalStateException("ProcessorState initialized already");
    }

    this.processingEnvironment = processingEnvironment;
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
  public void finish() {
    for (Processor processor : processors) {
      MethodSpec.Builder method = processor.createMethod();
      Filer filer = processingEnvironment.getFiler();
      ClassName autoLoadProviderClass = processor.getGeneratedClassSuperClass();

      // Adding a public constructor is important, else the ServiceLoader might fail
      MethodSpec constructor =
          MethodSpec.methodBuilder("<init>").addModifiers(Modifier.PUBLIC).build();

      processor.finish(method);

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
              + Math.abs(System.nanoTime())
              + "_"
              + System.currentTimeMillis()
              + "_"
              + UUID.randomUUID().toString().replace("-", "");

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
}
