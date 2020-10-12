package net.labyfy.component.processing.autoload;

import com.google.auto.common.SimpleAnnotationMirror;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableMap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import net.labyfy.component.commons.annotation.AnnotationMirrorUtil;
import net.labyfy.component.commons.util.Pair;
import net.labyfy.component.processing.Processor;
import net.labyfy.component.processing.ProcessorState;

import javax.lang.model.element.*;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;
import java.util.stream.Collectors;

@AutoService(Processor.class)
public class DetectableAnnotationProcessor implements Processor {

  /**
   * Template to instantiate an annotation.
   */
  private static final String ANNOTATION_TEMPLATE = "" +
      "new ${TYPE_NAME}() {\n" +
      "${METHODS}\n" +
      "    \n" +
      "    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType(){\n" +
      "        return ${TYPE_NAME}.class;\n" +
      "    }\n" +
      "}";

  /**
   * Template to implement an annotation method.
   */
  private static final String ANNOTATION_METHOD_TEMPLATE = "" +
      "    public ${RETURN_TYPE_NAME} ${NAME}(){\n" +
      "        return ${RETURN_VALUE}; \n" +
      "    }";

  /**
   * Template to instantiate AnnotationMeta
   */
  private static final String ANNOTATION_META_TEMPLATE = "" +
      "new net.labyfy.component.processing.autoload.AnnotationMeta(\n" +
      "   javax.lang.model.element.ElementKind.${ELEMENT_KIND}, \n" +
      "   ${IDENTIFIER}, \n" +
      "   ${ANNOTATION}, \n" +
      "new net.labyfy.component.processing.autoload.AnnotationMeta[]{${SUB_METADATA}})";

  /**
   * Template to instantiate a class identifier
   */
  private static final String ANNOTATION_META_CLASS_IDENTIFIER_TEMPLATE = ""
      + "new net.labyfy.component.processing.autoload.AnnotationMeta.ClassIdentifier(\"${TYPE_NAME}\")";

  /**
   * Template to instantiate a method identifier
   */
  private static final String ANNOTATION_META_METHOD_IDENTIFIER_TEMPLATE = ""
      + "new net.labyfy.component.processing.autoload.AnnotationMeta.MethodIdentifier(\"${OWNER_NAME}\", \"${NAME}\", new String[]{${PARAMETERS}})";


  private final Collection<String> found;

  /**
   * Constructs a new {@link DetectableAnnotationProcessor}, expected to be called by a {@link java.util.ServiceLoader}
   */
  public DetectableAnnotationProcessor() {
    this.found = new HashSet<>();
  }

  /**
   * {@inheritDoc}
   */
  public MethodSpec.Builder createMethod() {
    ClassName listClass = ClassName.get(List.class);
    ClassName foundAnnotationClass = ClassName.get(AnnotationMeta.class);

    // Create a method with the signature
    return MethodSpec.methodBuilder("register")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(
            ParameterizedTypeName.get(listClass, foundAnnotationClass),
            "list")
        .returns(void.class);
  }

  /**
   * {@inheritDoc}
   */
  public ClassName getGeneratedClassSuperClass() {
    return ClassName.get(DetectableAnnotationProvider.class);
  }

  /**
   * {@inheritDoc}
   */
  public void accept(TypeElement annotationType) {
    for (Element annotatedElement : ProcessorState.getInstance().getCurrentRoundEnvironment().getElementsAnnotatedWith(annotationType)) {
      String parsedAnnotation = parseAnnotationMeta(annotationType, annotatedElement);
      if (parsedAnnotation.isEmpty()) continue;
      this.found.add(("list.add(" + parsedAnnotation + ")").replace("$", "$$"));
    }

  }

  /**
   * Parse a given annotation to java syntax that will instantiate the target annotation with the exact same values.
   *
   * @param annotationType   the annotation type to look for
   * @param annotatedElement the element to look at for the annotationType
   * @return the java code to instantiate the annotation
   */
  private String parseAnnotationMeta(TypeElement annotationType, Element annotatedElement) {
    //meta is optional, so if it is not present, we dont take any action
    if (annotationType.getAnnotation(DetectableAnnotation.class) == null) return "";

    return handleTemplate(
        ImmutableMap.<String, String>builder()
            .put("ELEMENT_KIND", annotatedElement.getKind().name())
            .put("IDENTIFIER", createAnnotationMetaIdentifier(annotatedElement))
            .put("ANNOTATION", createAnnotation(annotationType, collectAnnotationData(annotationType, annotatedElement), annotationType.toString()))
            .put("SUB_METADATA", createSubMetaData(annotationType, annotatedElement))
            .build(),
        ANNOTATION_META_TEMPLATE);

  }

  private String createSubMetaData(TypeElement annotationType, Element annotatedElement) {
    StringBuilder output = new StringBuilder();
    boolean semicolon = false;
    for (Pair<Element, AnnotationMirror> pair : getAnnotationSubMetaMirrors(annotationType, annotatedElement)) {
      if (pair.getSecond() == null) continue;

      if (semicolon) {
        output.append(", ");
      }
      output.append(parseAnnotationMeta(((TypeElement) pair.getSecond().getAnnotationType().asElement()), pair.getFirst()));
      semicolon = true;
    }
    return output.toString();
  }

  private String createAnnotationMetaIdentifier(Element annotatedElement) {
    switch (annotatedElement.getKind()) {
      case CLASS:
      case ANNOTATION_TYPE:
      case INTERFACE:
        return createAnnotationMetaClassIdentifier((TypeElement) annotatedElement);
      case METHOD:
        return createAnnotationMetaMethodIdentifier(annotatedElement);
      default:
        throw new IllegalStateException("annotation target type " + annotatedElement.getKind() + " not supported yet.");
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
        parameters.append(ProcessorState.getInstance().getProcessingEnvironment().getElementUtils().getBinaryName(((TypeElement) element)).toString());
      } else {
        parameters.append(parameter.asType().toString());
      }
      parameters.append("\"");
      semicolon = true;
    }

    return handleTemplate(
        ImmutableMap.<String, String>builder()
            .put("OWNER_NAME", ProcessorState.getInstance().getProcessingEnvironment().getElementUtils().getBinaryName((TypeElement) annotatedElement.getEnclosingElement()).toString())
            .put("NAME", annotatedElement.getSimpleName().toString())
            .put("PARAMETERS", parameters.toString())
            .build(),
        ANNOTATION_META_METHOD_IDENTIFIER_TEMPLATE
    );
  }

  private String createAnnotationMetaClassIdentifier(TypeElement typeElement) {
    return handleTemplate(
        ImmutableMap.<String, String>builder()
            .put("TYPE_NAME", ProcessorState.getInstance().getProcessingEnvironment().getElementUtils().getBinaryName(typeElement).toString())
            .build(),
        ANNOTATION_META_CLASS_IDENTIFIER_TEMPLATE);
  }


  /**
   * Collects all meta data of an annotation associated with their target.
   * This can be used to implement meta that is not present on the same {@link Element}
   * but on one of its children.
   * <p>
   * Currently only metadata that is present on the same element as their parent can be obtained.
   *
   * @param annotationType   the parent annotation type to look for. Must be annotated with {@link DetectableAnnotation}
   * @param annotatedElement the location where to look for annotationType
   * @return the direct metadata for annotationType on annotatedElement
   */
  private Collection<Pair<Element, AnnotationMirror>> getAnnotationSubMetaMirrors(TypeElement annotationType, Element annotatedElement) {
    //get the type of DetectableAnnotation
    TypeElement detectableAnnotationType = ProcessorState.getInstance().getProcessingEnvironment().getElementUtils().getTypeElement(DetectableAnnotation.class.getName());

    //get the DetectableAnnotation of the target annotation type
    AnnotationMirror detectableAnnotationMirror = AnnotationMirrorUtil.getAnnotationMirror(annotationType, DetectableAnnotation.class.getName());

    if (detectableAnnotationMirror == null) {
      //we assert that this annotation is annotated with DetectableAnnotation
      throw new AssertionError(annotationType + " must be annotated with " + DetectableAnnotation.class);
    }

    List<Pair<Element, AnnotationMirror>> metaClasses = new ArrayList<>();
    //Merge detectableAnnotationMirror with default class values
    AnnotationMirror mergedDetectableAnnotationMirror = SimpleAnnotationMirror.of(detectableAnnotationType, AnnotationMirrorUtil.getElementValuesByName(detectableAnnotationMirror));
    AnnotationValue metaData = mergedDetectableAnnotationMirror.getElementValues().get(mergedDetectableAnnotationMirror.getElementValues().keySet().stream().filter(executableElement -> executableElement.getSimpleName().toString().equals("metaData")).findAny().get());
    if (metaData == null) return metaClasses;

    //collect annotated elements with its target elements
    ((List<AnnotationValue>) metaData.getValue())
        .stream()
        .map(AnnotationValue::getValue)
        .map(DeclaredType.class::cast)
        .map(DeclaredType::asElement)
        .map(TypeElement.class::cast)
        .map(typeElement -> new Pair<>(annotatedElement, AnnotationMirrorUtil.getAnnotationMirror(annotatedElement, typeElement.getQualifiedName().toString())))
        .forEach(metaClasses::add);
    return metaClasses;
  }

  private Map<ExecutableElement, AnnotationValue> collectAnnotationData(TypeElement typeElement, Element annotatedElement) {
    Map<String, AnnotationValue> collect = new HashMap<>(annotatedElement.getAnnotationMirrors().stream().filter(annotationMirror -> annotationMirror.getAnnotationType().asElement().asType().equals(typeElement.asType())).map(AnnotationMirror::getElementValues).findAny().orElse(new HashMap<>()))
        .entrySet()
        .stream()
        .collect(Collectors.toMap(entry -> entry.getKey().getSimpleName().toString(), Map.Entry::getValue));


    Map<ExecutableElement, AnnotationValue> classValues = new HashMap<>(SimpleAnnotationMirror.of(typeElement, collect).getElementValues());
    return classValues;
  }

  private String createMethod(ExecutableElement executableElement, Map<ExecutableElement, AnnotationValue> annotationValues) {
    return handleTemplate(ImmutableMap.<String, String>builder()
        .put("NAME", executableElement.getSimpleName().toString())
        .put("RETURN_TYPE_NAME", executableElement.getReturnType().toString())
        .put("RETURN_VALUE", createMethodReturn(executableElement, annotationValues))
        .build(), ANNOTATION_METHOD_TEMPLATE);
  }

  private String createMethodReturn(ExecutableElement executableElement, Map<ExecutableElement, AnnotationValue> annotationValues) {
    AnnotationValue value = annotationValues.get(executableElement);

    AnnotationValueVisitor<String, Void> annotationValueVisitor = new AnnotationValueVisitor<String, Void>() {
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
        return t.toString() + ".class";
      }

      @Override
      public String visitEnumConstant(VariableElement c, Void unused) {
        return ((TypeElement) c.getEnclosingElement()).getQualifiedName().toString() + "." + c.getSimpleName().toString();
      }

      @Override
      public String visitAnnotation(AnnotationMirror a, Void unused) {
        Map<ExecutableElement, AnnotationValue> classValues = new HashMap<>(SimpleAnnotationMirror.of(((TypeElement) a.getAnnotationType().asElement()), a.getElementValues().entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getSimpleName().toString(), Map.Entry::getValue))).getElementValues());
        Map<ExecutableElement, AnnotationValue> instanceValues = new HashMap<>(a.getElementValues());
        classValues.putAll(instanceValues);
        return createAnnotation(((TypeElement) a.getAnnotationType().asElement()), classValues, a.getAnnotationType().toString());
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

        String arrayType = ((ArrayType) executableElement.getReturnType()).getComponentType().toString();
        arrayType = arrayType.contains("<") ? arrayType.substring(0, arrayType.indexOf('<')) : arrayType;

        return "new " + arrayType + "[]{" + stringBuilder.toString() + "}";
      }

      @Override
      public String visitUnknown(AnnotationValue av, Void unused) {
        throw new IllegalStateException();
      }
    };
    return value.accept(annotationValueVisitor, null);
  }

  private String createAnnotation(TypeElement annotationType, Map<ExecutableElement, AnnotationValue> annotationValues, String typeName) {
    StringBuilder methods = new StringBuilder();

    for (Element enclosedElement : annotationType.getEnclosedElements()) {
      if (!(enclosedElement instanceof ExecutableElement)) continue;
      ExecutableElement executableElement = (ExecutableElement) enclosedElement;
      if (executableElement.getSimpleName().toString().equals("<init>") || enclosedElement.getSimpleName().toString().equals("<clinit>"))
        continue;

      methods.append(createMethod(executableElement, annotationValues))
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


  /**
   * {@inheritDoc}
   */
  public void finish(MethodSpec.Builder targetMethod) {
    // Add sourcecode to auto generated class
    this.found.forEach(targetMethod::addStatement);
  }


}
