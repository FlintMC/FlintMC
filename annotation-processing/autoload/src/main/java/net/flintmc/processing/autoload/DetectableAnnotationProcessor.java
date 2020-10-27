package net.flintmc.processing.autoload;

import com.google.auto.common.SimpleAnnotationMirror;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableMap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import net.flintmc.processing.Processor;
import net.flintmc.processing.ProcessorState;
import net.flintmc.util.commons.Pair;
import net.flintmc.util.commons.annotation.AnnotationMirrorUtil;

import javax.lang.model.element.*;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Repeatable;
import java.util.*;
import java.util.stream.Collectors;

@AutoService(Processor.class)
public class DetectableAnnotationProcessor implements Processor {

  /** Template to instantiate an annotation. */
  private static final String ANNOTATION_TEMPLATE =
      ""
          + "new ${TYPE_NAME}() {\n"
          + "${METHODS}\n"
          + "    \n"
          + "    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType(){\n"
          + "        return ${TYPE_NAME}.class;\n"
          + "    }\n"
          + "}";

  /** Template to implement an annotation method. */
  private static final String ANNOTATION_METHOD_TEMPLATE =
      ""
          + "    public ${RETURN_TYPE_NAME} ${NAME}(){\n"
          + "        return ${RETURN_VALUE}; \n"
          + "    }";

  /** Template to instantiate AnnotationMeta */
  private static final String ANNOTATION_META_TEMPLATE =
      ""
          + "new AnnotationMeta(\n"
          + "   javax.lang.model.element.ElementKind.${ELEMENT_KIND}, \n"
          + "   ${IDENTIFIER}, \n"
          + "   ${ANNOTATION}, \n"
          + "new AnnotationMeta[]{${META_DATA}})";

  /**
   * Template to instantiate a class identifier
   */
  private static final String ANNOTATION_META_CLASS_IDENTIFIER_TEMPLATE =
          "" + "new net.flintmc.processing.autoload.identifier.ClassIdentifier(\"${TYPE_NAME}\")";

    /**
     * Template to instantiate a method identifier
     */
    private static final String ANNOTATION_META_METHOD_IDENTIFIER_TEMPLATE =
            "" + "new net.flintmc.processing.autoload.identifier.MethodIdentifier(\"${OWNER_NAME}\", \"${NAME}\", new String[]{${PARAMETERS}})";

  private final Collection<String> found;

  /**
   * Constructs a new {@link DetectableAnnotationProcessor}, expected to be called by a {@link
   * java.util.ServiceLoader}
   */
  public DetectableAnnotationProcessor() {
    this.found = new ArrayList<>();
  }

  /** {@inheritDoc} */
  public MethodSpec.Builder createMethod() {
    ClassName listClass = ClassName.get(List.class);
    ClassName foundAnnotationClass = ClassName.get(AnnotationMeta.class);

    // Create a method with the signature
    return MethodSpec.methodBuilder("register")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(ParameterizedTypeName.get(listClass, foundAnnotationClass), "list")
        .returns(void.class);
  }

  /** {@inheritDoc} */
  public ClassName getGeneratedClassSuperClass() {
    return ClassName.get(DetectableAnnotationProvider.class);
  }

  /** {@inheritDoc} */
  public void accept(TypeElement annotationType) {

    // We dont want to discover annotation types without DetectableAnnotation
    if (annotationType.getAnnotation(DetectableAnnotation.class) != null)
      acceptDetectableAnnotation(annotationType);

    // We dont want to discover annotation types without RepeatingDetectableAnnotation
    if (annotationType.getAnnotation(RepeatingDetectableAnnotation.class) != null)
      acceptRepeatableDetectableAnnotation(annotationType);
  }

  private void acceptRepeatableDetectableAnnotation(TypeElement annotationType) {
    // Get the RepeatingDetectableAnnotation as a type
      TypeElement repeatingDetectableAnnotationType =
              ProcessorState.getInstance()
                      .getProcessingEnvironment()
                      .getElementUtils()
                      .getTypeElement("net.flintmc.processing.autoload.RepeatingDetectableAnnotation");

    // Get the values of the RepeatingDetectableAnnotation at the instance on the annotationType
    Map<String, AnnotationValue> repeatingDetectableAnnotationValues =
        AnnotationMirrorUtil.getElementValuesByName(
            AnnotationMirrorUtil.getAnnotationMirror(
                annotationType, repeatingDetectableAnnotationType));

    // Get the annotation type that is repeated by @param annotationType
    TypeElement repeatedAnnotationType =
        ((TypeElement)
            ((DeclaredType) repeatingDetectableAnnotationValues.get("value").getValue())
                .asElement());
    System.out.println("Repeating " + repeatedAnnotationType + " with " + annotationType);

    for (Element annotatedElement :
        ProcessorState.getInstance()
            .getCurrentRoundEnvironment()
            .getElementsAnnotatedWith(annotationType)) {
      // The root annotation as a mirror
      AnnotationMirror annotationMirror =
          AnnotationMirrorUtil.getAnnotationMirror(annotatedElement, annotationType);
      List<AnnotationValue> repeatedAnnotations =
          (List<AnnotationValue>)
              AnnotationMirrorUtil.getElementValuesByName(annotationMirror).get("value").getValue();
      for (AnnotationValue repeatedAnnotation : repeatedAnnotations) {

        String parsedAnnotation =
            parseAnnotation(
                repeatedAnnotationType,
                annotatedElement,
                (Map<ExecutableElement, AnnotationValue>)
                    SimpleAnnotationMirror.of(
                            repeatedAnnotationType,
                            AnnotationMirrorUtil.getElementValuesByName(
                                ((AnnotationMirror) repeatedAnnotation)))
                        .getElementValues());
        if (parsedAnnotation.isEmpty()) return;
        this.found.add(("list.add(" + parsedAnnotation + ")").replace("$", "$$"));
        System.out.println(parsedAnnotation);
      }
    }
  }

  public void acceptDetectableAnnotation(TypeElement annotationType) {
    // Get the DetectableAnnotation as a type
      TypeElement detectableAnnotationType =
              ProcessorState.getInstance()
                      .getProcessingEnvironment()
                      .getElementUtils()
                      .getTypeElement("net.flintmc.processing.autoload.DetectableAnnotation");

    // Get the values of the DetectableAnnotation at the instance on the annotationType
    Map<String, AnnotationValue> detectableAnnotationValues =
        AnnotationMirrorUtil.getElementValuesByName(
            AnnotationMirrorUtil.getAnnotationMirror(annotationType, detectableAnnotationType));

    // meta that requires a parent will not be handled standalone
    if (((boolean) detectableAnnotationValues.get("requiresParent").getValue())) return;

    for (Element annotatedElement :
        ProcessorState.getInstance()
            .getCurrentRoundEnvironment()
            .getElementsAnnotatedWith(annotationType)) {

      // get the discovered annotation as a mirror
      AnnotationMirror annotationMirror =
          AnnotationMirrorUtil.getAnnotationMirror(annotatedElement, annotationType);

      String parsedAnnotation =
          parseAnnotation(
              annotationType,
              annotatedElement,
              (Map<ExecutableElement, AnnotationValue>) annotationMirror.getElementValues());
      if (parsedAnnotation.isEmpty()) continue;
      this.found.add(("list.add(" + parsedAnnotation + ")").replace("$", "$$"));
    }
  }

  /**
   * Parse a given annotation to java syntax that will instantiate the target annotation with the
   * exact same values.
   *
   * @param annotationType the annotation type to look for
   * @param annotatedElement the element to look at for the annotationType
   * @param annotationValues the value of the annotation to write
   * @return the java code to instantiate the annotation
   */
  private String parseAnnotation(
      TypeElement annotationType,
      Element annotatedElement,
      Map<ExecutableElement, AnnotationValue> annotationValues) {
    // meta is optional, so if it is not present, we dont take any action
    if (annotationType.getAnnotation(DetectableAnnotation.class) == null) return "";

    return handleTemplate(
        ImmutableMap.<String, String>builder()
            .put("ELEMENT_KIND", annotatedElement.getKind().name())
            .put("IDENTIFIER", createAnnotationIdentifier(annotatedElement))
            .put(
                "ANNOTATION",
                createAnnotation(annotationType, annotationValues, annotationType.toString()))
            .put("META_DATA", createMetaData(annotationType, annotatedElement))
            .build(),
        ANNOTATION_META_TEMPLATE);
  }

  private String createMetaData(TypeElement annotationType, Element annotatedElement) {
    StringBuilder output = new StringBuilder();
    boolean semicolon = false;
    for (Pair<Element, AnnotationMirror> pair :
        getAnnotationMetaMirrors(annotationType, annotatedElement)) {
      if (pair.getSecond() == null) continue;

      if (semicolon) {
        output.append(", ");
      }
      output.append(
          parseAnnotation(
              ((TypeElement) pair.getSecond().getAnnotationType().asElement()),
              pair.getFirst(),
              (Map<ExecutableElement, AnnotationValue>) pair.getSecond().getElementValues()));
      semicolon = true;
    }
    return output.toString();
  }

  private String createAnnotationIdentifier(Element annotatedElement) {
    switch (annotatedElement.getKind()) {
      case CLASS:
      case ANNOTATION_TYPE:
      case INTERFACE:
        return createAnnotationMetaClassIdentifier((TypeElement) annotatedElement);
      case METHOD:
        return createAnnotationMetaMethodIdentifier(annotatedElement);
      default:
        throw new IllegalStateException(
            "annotation target type " + annotatedElement.getKind() + " not supported yet.");
    }
  }

  private String createAnnotationMetaMethodIdentifier(Element annotatedElement) {
    StringBuilder parameters = new StringBuilder();
    boolean semicolon = false;
    for (VariableElement parameter : ((ExecutableElement) annotatedElement).getParameters()) {
      if (semicolon) {
        parameters.append(", ");
      }

      parameters.append("\"");
      if (parameter.asType() instanceof DeclaredType) {
        Element element = ((DeclaredType) parameter.asType()).asElement();
        parameters.append(
            ProcessorState.getInstance()
                .getProcessingEnvironment()
                .getElementUtils()
                .getBinaryName(((TypeElement) element))
                .toString());
      } else {
        parameters.append(parameter.asType().toString());
      }
      parameters.append("\"");
      semicolon = true;
    }

    return handleTemplate(
        ImmutableMap.<String, String>builder()
            .put(
                "OWNER_NAME",
                ProcessorState.getInstance()
                    .getProcessingEnvironment()
                    .getElementUtils()
                    .getBinaryName((TypeElement) annotatedElement.getEnclosingElement())
                    .toString())
            .put("NAME", annotatedElement.getSimpleName().toString())
            .put("PARAMETERS", parameters.toString())
            .build(),
        ANNOTATION_META_METHOD_IDENTIFIER_TEMPLATE);
  }

  private String createAnnotationMetaClassIdentifier(TypeElement typeElement) {
    return handleTemplate(
        ImmutableMap.<String, String>builder()
            .put(
                "TYPE_NAME",
                ProcessorState.getInstance()
                    .getProcessingEnvironment()
                    .getElementUtils()
                    .getBinaryName(typeElement)
                    .toString())
            .build(),
        ANNOTATION_META_CLASS_IDENTIFIER_TEMPLATE);
  }

  /**
   * Collects all meta data of an annotation associated with their target. This can be used to
   * implement meta that is not present on the same {@link Element} but on one of its children.
   *
   * <p>Currently only metadata that is present on the same element as their parent can be obtained.
   *
   * @param annotationType the parent annotation type to look for. Must be annotated with {@link
   *     DetectableAnnotation}
   * @param annotatedElement the location where to look for annotationType
   * @return the direct metadata for annotationType on annotatedElement
   */
  private Collection<Pair<Element, AnnotationMirror>> getAnnotationMetaMirrors(
      TypeElement annotationType, Element annotatedElement) {
    List<Pair<Element, AnnotationMirror>> metaClasses = new ArrayList<>();

    // Get the DetectableAnnotation mirror of annotationType
      AnnotationMirror detectableAnnotationMirror =
              AnnotationMirrorUtil.getAnnotationMirror(
                      annotationType,
                      ProcessorState.getInstance()
                              .getProcessingEnvironment()
                              .getElementUtils()
                              .getTypeElement("net.flintmc.processing.autoload.DetectableAnnotation"));

    // Collect all possible meta types
    Collection<TypeElement> annotationMetaTypes =
        ((List<AnnotationValue>)
                AnnotationMirrorUtil.getElementValuesByName(detectableAnnotationMirror)
                    .get("metaData")
                    .getValue())
            .stream()
                .map(
                    annotationValue ->
                        ((TypeElement) ((DeclaredType) annotationValue.getValue()).asElement()))
                .collect(Collectors.toSet());

    Collection<Element> potentialElements = new HashSet<>();

    potentialElements.add(annotatedElement);
    potentialElements.addAll(annotatedElement.getEnclosedElements());

    for (TypeElement annotationMetaType : annotationMetaTypes) {
      for (Element potentialElement : potentialElements) {

        if (annotationMetaType.getAnnotation(Repeatable.class) != null) {
          AnnotationMirror repeatableAnnotationMirror =
              AnnotationMirrorUtil.getAnnotationMirror(
                  annotationMetaType,
                  ProcessorState.getInstance()
                      .getProcessingEnvironment()
                      .getElementUtils()
                      .getTypeElement("java.lang.annotation.Repeatable"));
          TypeElement repeatingAnnotationType =
              (TypeElement)
                  ((DeclaredType)
                          AnnotationMirrorUtil.getElementValuesByName(repeatableAnnotationMirror)
                              .get("value")
                              .getValue())
                      .asElement();
          if (repeatingAnnotationType.getAnnotation(RepeatingDetectableAnnotation.class) == null) {
            throw new IllegalStateException(
                "Repeating annotation "
                    + repeatingAnnotationType
                    + " must have @RepeatingDetectableAnnotation");
          }

          AnnotationMirror repeatingAnnotationMirror =
              AnnotationMirrorUtil.getAnnotationMirror(potentialElement, repeatingAnnotationType);
          if (repeatingAnnotationMirror != null) {
            List<AnnotationMirror> annotationMirrors =
                (List<AnnotationMirror>)
                    AnnotationMirrorUtil.getElementValuesByName(repeatingAnnotationMirror)
                        .get("value")
                        .getValue();
            for (AnnotationMirror annotationMirror : annotationMirrors) {
              metaClasses.add(
                  new Pair<>(
                      potentialElement,
                      SimpleAnnotationMirror.of(
                          ((TypeElement) annotationMirror.getAnnotationType().asElement()),
                          AnnotationMirrorUtil.getElementValuesByName(annotationMirror))));
            }
          }
        }

        AnnotationMirror annotationMetaMirror =
            AnnotationMirrorUtil.getAnnotationMirror(potentialElement, annotationMetaType);
        if (annotationMetaMirror == null) continue;

        if (annotationMetaType.getAnnotation(DetectableAnnotation.class) != null) {
          metaClasses.add(new Pair<>(potentialElement, annotationMetaMirror));
        }
      }
    }

    return metaClasses;
  }

  private String createMethod(
      ExecutableElement executableElement,
      Map<ExecutableElement, AnnotationValue> annotationValues) {
    return handleTemplate(
        ImmutableMap.<String, String>builder()
            .put("NAME", executableElement.getSimpleName().toString())
            .put("RETURN_TYPE_NAME", executableElement.getReturnType().toString())
            .put("RETURN_VALUE", createMethodReturn(executableElement, annotationValues))
            .build(),
        ANNOTATION_METHOD_TEMPLATE);
  }

  private String createMethodReturn(
      ExecutableElement executableElement,
      Map<ExecutableElement, AnnotationValue> annotationValues) {
    AnnotationValue value = annotationValues.get(executableElement);
    if (value == null)
      throw new AssertionError(
          "Annotation value for "
              + executableElement.toString()
              + " must not be null. "
              + annotationValues);

    AnnotationValueVisitor<String, Void> annotationValueVisitor =
        new AnnotationValueVisitor<String, Void>() {
          @Override
          public String visit(AnnotationValue av, Void unused) {
            return av.accept(this, unused);
          }

          @Override
          public String visit(AnnotationValue av) {
            return av.accept(this, null);
          }

          @Override
          public String visitBoolean(boolean b, Void unused) {
            return String.valueOf(b);
          }

          @Override
          public String visitByte(byte b, Void unused) {
            return "((byte)" + b + ")";
          }

          @Override
          public String visitChar(char c, Void unused) {
            return "'" + c + "'";
          }

          @Override
          public String visitDouble(double d, Void unused) {
            return d + "d";
          }

          @Override
          public String visitFloat(float f, Void unused) {
            return f + "f";
          }

          @Override
          public String visitInt(int i, Void unused) {
            return String.valueOf(i);
          }

          @Override
          public String visitLong(long i, Void unused) {
            return String.valueOf(i);
          }

          @Override
          public String visitShort(short s, Void unused) {
            return String.valueOf(s);
          }

          @Override
          public String visitString(String s, Void unused) {
            return "\"" + s + "\"";
          }

          @Override
          public String visitType(TypeMirror t, Void unused) {
            return String.format("%s.class", t.toString());
          }

          @Override
          public String visitEnumConstant(VariableElement c, Void unused) {
            return ((TypeElement) c.getEnclosingElement()).getQualifiedName().toString()
                + "."
                + c.getSimpleName().toString();
          }

          @Override
          public String visitAnnotation(AnnotationMirror a, Void unused) {
            Map<ExecutableElement, AnnotationValue> classValues =
                new HashMap<>(
                    SimpleAnnotationMirror.of(
                            ((TypeElement) a.getAnnotationType().asElement()),
                            a.getElementValues().entrySet().stream()
                                .collect(
                                    Collectors.toMap(
                                        entry -> entry.getKey().getSimpleName().toString(),
                                        Map.Entry::getValue)))
                        .getElementValues());
            Map<ExecutableElement, AnnotationValue> instanceValues =
                new HashMap<>(a.getElementValues());
            classValues.putAll(instanceValues);
            return createAnnotation(
                ((TypeElement) a.getAnnotationType().asElement()),
                classValues,
                a.getAnnotationType().toString());
          }

          @Override
          public String visitArray(List<? extends AnnotationValue> vals, Void unused) {
            StringBuilder stringBuilder = new StringBuilder();

            boolean semicolon = false;
            for (AnnotationValue val : vals) {
              if (semicolon) {
                stringBuilder.append(", ");
              }
              stringBuilder.append(val.accept(this, null));
              semicolon = true;
            }

            String arrayType =
                ((ArrayType) executableElement.getReturnType()).getComponentType().toString();
            arrayType =
                arrayType.contains("<")
                    ? arrayType.substring(0, arrayType.indexOf('<'))
                    : arrayType;

            return "new " + arrayType + "[]{" + stringBuilder.toString() + "}";
          }

          @Override
          public String visitUnknown(AnnotationValue av, Void unused) {
            throw new IllegalStateException();
          }
        };
    return value.accept(annotationValueVisitor, null);
  }

  private String createAnnotation(
      TypeElement annotationType,
      Map<ExecutableElement, AnnotationValue> annotationValues,
      String typeName) {
    StringBuilder methods = new StringBuilder();

    for (Element enclosedElement : annotationType.getEnclosedElements()) {
      if (!(enclosedElement instanceof ExecutableElement)) continue;
      ExecutableElement executableElement = (ExecutableElement) enclosedElement;
      if (executableElement.getSimpleName().toString().equals("<init>")
          || enclosedElement.getSimpleName().toString().equals("<clinit>")) continue;

      methods
          .append(createMethod(executableElement, annotationValues))
          .append(System.lineSeparator());
    }

    return handleTemplate(
        ImmutableMap.<String, String>builder()
            .put("TYPE_NAME", typeName)
            .put("METHODS", methods.toString())
            .build(),
        ANNOTATION_TEMPLATE);
  }

  private String handleTemplate(Map<String, String> data, String template) {
    for (Map.Entry<String, String> entry : data.entrySet()) {
      template = template.replace("${" + entry.getKey() + "}", entry.getValue());
    }
    return template;
  }

  /** {@inheritDoc} */
  public void finish(MethodSpec.Builder targetMethod) {
    // Add sourcecode to auto generated class
    this.found.forEach(targetMethod::addStatement);
    this.found.clear();
  }
}
