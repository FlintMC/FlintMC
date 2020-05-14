package net.labyfy.component.annotation.processing.autoload;

import com.squareup.javapoet.MethodSpec;
import net.labyfy.component.annotation.processing.ProcessingException;
import net.labyfy.component.annotation.processing.ProcessorState;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.*;

public class AutoLoadProcessor {
  private final ProcessorState state;
  private final List<String> autoLoadClasses;

  public AutoLoadProcessor(ProcessorState state) {
    this.state = state;
    this.autoLoadClasses = new ArrayList<>();
  }

  public void accept(TypeElement autoLoadAnnotation) {
    Messager messager = state.getProcessingEnvironment().getMessager();

    state
        .getCurrentRoundEnvironment()
        .getElementsAnnotatedWith(autoLoadAnnotation)
        .stream()
        .map((element) -> {
          if(!(element instanceof TypeElement)) {
            throw new ProcessingException(
                "Constraint violated: Found @AutoLoad annotation on something else than a type", element);
          }

          return (TypeElement) element;
        }).filter((element) -> {
          if(element.getKind() != ElementKind.CLASS) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Only classes can be annotated with @AutoLoad", element);
            return false;
          } else {
            return true;
          }
        }).filter((element) -> {
          String name = element.getQualifiedName().toString();
          if(name == null || name.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                "Anonymous classes can not be annotated with @AutoLoad", element);
            return false;
          } else {
            return true;
          }
        }).map((element) -> element.getQualifiedName().toString()).forEach(autoLoadClasses::add);
  }

  public void finish(MethodSpec.Builder targetMethod) {
    autoLoadClasses.forEach((toLoad) -> targetMethod.addStatement("autoLoadClasses.add($L.class);", toLoad));
  }
}
