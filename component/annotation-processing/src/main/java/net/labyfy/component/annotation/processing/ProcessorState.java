package net.labyfy.component.annotation.processing;

import com.squareup.javapoet.*;
import net.labyfy.component.annotation.processing.autoload.AutoLoadProcessor;
import net.labyfy.component.annotation.processing.util.ServiceFile;

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
import java.util.HashSet;
import java.util.Set;

public class ProcessorState {
  private final AutoLoadProcessor autoLoadProcessor;

  private ProcessingEnvironment processingEnvironment;
  private RoundEnvironment currentRoundEnvironment;

  public ProcessorState() {
    this.autoLoadProcessor = new AutoLoadProcessor(this);
  }

  public void init(ProcessingEnvironment processingEnvironment) {
    if(this.processingEnvironment != null) {
      throw new IllegalStateException("ProcessorState initialized already");
    }

    this.processingEnvironment = processingEnvironment;
  }

  public void round(RoundEnvironment environment) {
    currentRoundEnvironment = environment;
  }

  public void processAutoLoad(TypeElement autoLoadAnnotation) {
    autoLoadProcessor.accept(autoLoadAnnotation);
  }

  public ProcessingEnvironment getProcessingEnvironment() {
    return processingEnvironment;
  }

  public RoundEnvironment getCurrentRoundEnvironment() {
    return currentRoundEnvironment;
  }

  public void finish() {
    Filer filer = processingEnvironment.getFiler();

    ClassName listClass = ClassName.get("java.util", "List");
    ClassName classClass = ClassName.get("java.lang", "Class");
    TypeName listOfClasses = ParameterizedTypeName.get(listClass, classClass);

    ClassName autoLoadProviderClass =
        ClassName.get("net.labyfy.component.initializer", "AutoLoadProvider");

    MethodSpec.Builder registerAutoLoadMethodBuilder = MethodSpec.methodBuilder("registerAutoLoad")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addParameter(listOfClasses, "autoLoadClasses")
        .returns(void.class);

    MethodSpec registerAutoLoadMethod = registerAutoLoadMethodBuilder.build();

    String generatedClassName = "AutoLoadProvider" + System.currentTimeMillis();
    TypeSpec generatedType = TypeSpec.classBuilder(generatedClassName)
        .addAnnotation(Generated.class)
        .addSuperinterface(autoLoadProviderClass)
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
    try(InputStream stream =
            filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile).openInputStream()) {
      services.addAll(ServiceFile.read(stream));
    } catch (IOException ignored) {}

    services.add("net.labyfy.autogen." + generatedClassName);

    try(OutputStream stream =
            filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile).openOutputStream()) {
      ServiceFile.write(services, stream);
    } catch (IOException e) {
      throw new ProcessingException("Failed to update " + resourceFile, e);
    }
  }
}
