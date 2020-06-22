package net.labyfy.component.processing.autoload;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import net.labyfy.component.processing.Processor;
import net.labyfy.component.commons.annotation.AnnotationMirrorUtil;
import net.labyfy.component.processing.ProcessorState;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(Processor.class)
public class AutoLoadProcessor implements Processor {

  public static final String AUTO_LOAD_ANNOTATION_NAME =
      "net.labyfy.base.structure.annotation.AutoLoad";

  private final Map<Integer, Map<String, Integer>> autoLoadClasses;

  public AutoLoadProcessor() {
    this.autoLoadClasses = new HashMap<>();
  }

  public MethodSpec.Builder createMethod() {
    ClassName triConsumerClass = ClassName.get("net.labyfy.base.structure.util", "TriConsumer");
    ClassName integerClass = ClassName.get(Integer.class);
    ClassName stringClass = ClassName.get(String.class);

    return MethodSpec.methodBuilder("registerAutoLoad")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(
            ParameterizedTypeName.get(triConsumerClass, integerClass, integerClass, stringClass),
            "autoLoadClasses")
        .returns(void.class);
  }

  public ClassName getGeneratedClassSuperClass() {
    return ClassName.get("net.labyfy.base.structure", "AutoLoadProvider");
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

    ProcessorState.getInstance().getCurrentRoundEnvironment().getElementsAnnotatedWith(typeElement).stream()
        .map(
            (element) -> {
              if (!(element instanceof TypeElement)) return null;
              return (TypeElement) element;
            })
        .filter(Objects::nonNull)
        .filter(
            (element) ->
                element.getKind() == ElementKind.CLASS
                    || element.getKind() == ElementKind.INTERFACE)
        .filter(
            (element) -> {
              String name = element.getQualifiedName().toString();
              return name != null && !name.isEmpty();
            })
        .forEach(
            element -> {
              Integer priority =
                  (Integer)
                      AnnotationMirrorUtil.getAnnotationValue(
                              AnnotationMirrorUtil.getTransitiveAnnotationMirror(
                                  element, AUTO_LOAD_ANNOTATION_NAME),
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

              Integer round =
                  (Integer)
                      AnnotationMirrorUtil.getAnnotationValue(
                              AnnotationMirrorUtil.getTransitiveAnnotationMirror(
                                  element, AUTO_LOAD_ANNOTATION_NAME),
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

              String name =
                  element.getEnclosingElement() instanceof TypeElement
                      ? StringUtils.reverse(
                          StringUtils.reverse(element.getQualifiedName().toString())
                              .replaceFirst("\\.", "\\$"))
                      : element.getQualifiedName().toString();

              if (!this.autoLoadClasses.containsKey(round)) {
                this.autoLoadClasses.put(round, new ConcurrentHashMap<>());
              }

              this.autoLoadClasses.get(round).put(name, priority);
            });
  }

  public void finish(MethodSpec.Builder targetMethod) {
    autoLoadClasses.forEach(
        (round, map) ->
            map.forEach(
                (name, priority) -> {
                  targetMethod.addStatement(
                      "autoLoadClasses.accept($L, $L, $S)", round, priority, name);
                }));
  }
}
