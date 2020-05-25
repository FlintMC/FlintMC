package net.labyfy.component.annotation.processing.autoload;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import net.labyfy.component.annotation.processing.ProcessingConstants;
import net.labyfy.component.annotation.processing.ProcessingException;
import net.labyfy.component.annotation.processing.ProcessorState;
import net.labyfy.component.annotation.processing.util.AnnotationMirrorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AutoLoadProcessor {

  private final ProcessorState state;
  private final Map<Integer, Map<String, Integer>> autoLoadClasses;

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
            element -> {

              Integer priority = (Integer)
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
                      .getValue();

              Integer round = (Integer)
                  AnnotationMirrorUtil.getAnnotationValue(
                      AnnotationMirrorUtil.getTransitiveAnnotationMirror(
                          element, ProcessingConstants.AUTO_LOAD_ANNOTATION_NAME),
                      "round",
                      new AnnotationValue() {
                        public Integer getValue() {
                          return 1;
                        }

                        public <R, P> R accept(
                            AnnotationValueVisitor<R, P> visitor, P parameter) {
                          return visitor.visitInt(getValue(), parameter);
                        }
                      })
                      .getValue();

              String name = element.getEnclosingElement() instanceof TypeElement ?
                  StringUtils.reverse(StringUtils.reverse(element.getQualifiedName().toString())
                      .replaceFirst("\\.", "\\$"))
                  : element.getQualifiedName().toString();

              if (!this.autoLoadClasses.containsKey(round)) {
                this.autoLoadClasses.put(round, new ConcurrentHashMap<>());
              }

              this.autoLoadClasses.get(round).put(name, priority);

            }
        );
  }

  public void finish(MethodSpec.Builder targetMethod) {
    autoLoadClasses.forEach((round, map) -> map.forEach((name, priority) -> {
      targetMethod.addStatement("autoLoadClasses.accept($L, $L, $S)", round, priority, name);
    }));
  }
}
