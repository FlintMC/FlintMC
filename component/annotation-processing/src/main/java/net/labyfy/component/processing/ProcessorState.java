package net.labyfy.component.processing;

import com.squareup.javapoet.*;
import net.labyfy.component.processing.exception.ProcessingException;
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
import java.util.function.Consumer;

public class ProcessorState {
  private static ProcessorState instance;
  private final Collection<Processor> processors;
  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment currentRoundEnvironment;

  public ProcessorState() {
    this.processors = new HashSet<>();
    ServiceLoader.load(Processor.class, getClass().getClassLoader()).forEach(processors::add);
    instance = this;
  }

  public static ProcessorState getInstance() {
    return instance;
  }

  public void init(ProcessingEnvironment processingEnvironment) {
    if (this.processingEnvironment != null) {
      throw new IllegalStateException("ProcessorState initialized already");
    }

    this.processingEnvironment = processingEnvironment;
  }

  public void round(RoundEnvironment environment) {
    currentRoundEnvironment = environment;
  }

  public void processAutoLoad(TypeElement autoLoadAnnotation) {
    processors.forEach(processor -> processor.accept(autoLoadAnnotation));
  }

  public ProcessingEnvironment getProcessingEnvironment() {
    return processingEnvironment;
  }

  public RoundEnvironment getCurrentRoundEnvironment() {
    return currentRoundEnvironment;
  }

  public void finish() {

    for (Processor processor : processors) {
      MethodSpec.Builder method = processor.createMethod();
      Filer filer = processingEnvironment.getFiler();
      ClassName autoLoadProviderClass = processor.getGeneratedClassSuperClass();
      MethodSpec constructor =
          MethodSpec.methodBuilder("<init>").addModifiers(Modifier.PUBLIC).build();

      processor.finish(method);

      MethodSpec registerAutoLoadMethod = method.build();
      AnnotationSpec generatedAnnotation =
          AnnotationSpec.builder(Generated.class)
              .addMember("value", "$S", LabyfyAnnotationProcessor.class.getName())
              .build();

      String generatedClassName =
          "AutoLoadProvider"
              + System.nanoTime()
              + "_"
              + UUID.randomUUID().toString().replace("-", "");

      TypeSpec generatedType =
          TypeSpec.classBuilder(generatedClassName)
              .addAnnotation(generatedAnnotation)
              .addModifiers(Modifier.PUBLIC)
              .addSuperinterface(autoLoadProviderClass)
              .addMethod(constructor)
              .addMethod(registerAutoLoadMethod)
              .build();

      JavaFile finishedFile = JavaFile.builder("net.labyfy.autogen", generatedType).build();
      try {
        finishedFile.writeTo(filer);
      } catch (IOException e) {
        throw new ProcessingException("Failed to write to final file due to IOException", e);
      }

      String resourceFile = "META-INF/services/" + autoLoadProviderClass.reflectionName();

      Set<String> services = new HashSet<>();
      try (InputStream stream =
          filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile).openInputStream()) {
        services.addAll(IOUtils.readLines(stream, StandardCharsets.UTF_8));
      } catch (IOException ignored) {
      }

      services.add("net.labyfy.autogen." + generatedClassName);

      try (OutputStream stream =
          filer
              .createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile)
              .openOutputStream()) {
        IOUtils.writeLines(services, System.lineSeparator(), stream, StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new ProcessingException("Failed to update " + resourceFile, e);
      }
    }
  }
}
