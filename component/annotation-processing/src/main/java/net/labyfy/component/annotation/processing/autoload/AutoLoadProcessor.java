package net.labyfy.component.annotation.processing.autoload;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import net.labyfy.component.annotation.processing.ProcessingConstants;
import net.labyfy.component.annotation.processing.ProcessingException;
import net.labyfy.component.annotation.processing.ProcessorState;
import net.labyfy.component.annotation.processing.util.AnnotationMirrorUtil;

import javax.lang.model.element.*;
import java.util.*;

public class AutoLoadProcessor {

  private final ProcessorState state;
  private final Map<String, Integer> autoLoadClasses;

  public AutoLoadProcessor(ProcessorState state) {
    this.state = state;
    this.autoLoadClasses = new HashMap<>();
  }

  public void accept(TypeElement typeElement) {

    if (AnnotationMirrorUtil.collectTransitiveAnnotations(typeElement).stream()
            .noneMatch(
                annotationMirror ->
                    ((TypeElement) annotationMirror.getAnnotationType().asElement())
                        .getQualifiedName()
                        .toString()
                        .equals("net.labyfy.base.structure.annotation.AutoLoad"))
        && !typeElement
            .getQualifiedName()
            .toString()
            .equals("net.labyfy.base.structure.annotation.AutoLoad")) return;

    state.getCurrentRoundEnvironment().getElementsAnnotatedWith(typeElement).stream()
        .map(
            (element) -> {
              if (!(element instanceof TypeElement)) return null;
              return (TypeElement) element;
            })
        .filter(Objects::nonNull)
        .filter((element) -> element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.INTERFACE)
        .filter(
            (element) -> {
              String name = element.getQualifiedName().toString();
              return name != null && !name.isEmpty();
            })
        .forEach(
            element ->
                this.autoLoadClasses.put(
                    element.getQualifiedName().toString(),
                    ((Integer)
                        AnnotationMirrorUtil.getAnnotationValue(
                                AnnotationMirrorUtil.getTransitiveAnnotationMirror(
                                    element, ProcessingConstants.AUTO_LOAD_ANNOTATION_NAME),
                                "priority",
                                new AnnotationValue() {
                                  public Integer getValue() {
                                    return 0;
                                  }

                                  public <R, P> R accept(
                                      AnnotationValueVisitor<R, P> visitor, P parameter) {
                                    return visitor.visitInt(getValue(), parameter);
                                  }
                                })
                            .getValue())));
  }

  public void finish(MethodSpec.Builder targetMethod) {
    autoLoadClasses.forEach(
        (className, priority) ->
            targetMethod.addStatement("autoLoadClasses.put($L, $L.class);", priority, className));
  }
}
