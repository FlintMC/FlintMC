package net.labyfy.component.processing.autoload;

import com.google.auto.common.SimpleAnnotationMirror;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableMap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import net.labyfy.component.processing.Processor;
import net.labyfy.component.processing.ProcessorState;

import javax.lang.model.element.*;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AutoService(Processor.class)
public class DetectableAnnotationProcessor implements Processor {

  public static final String ANNOTATION_TEMPLATE = "new ${TYPE_NAME}() {\n" +
      "${METHODS}\n" +
      "    \n" +
      "    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType(){\n" +
      "        return ${TYPE_NAME}.class;\n" +
      "    }\n" +
      "}";
  public static final String METHOD_TEMPLATE = "" +
      "    public ${RETURN_TYPE_NAME} ${NAME}(){\n" +
      "        return ${RETURN_VALUE}; \n" +
      "    }";
  private final Map<Element, String> autoFoundClasses;

  /**
   * Constructs a new {@link AutoLoadProcessor}, expected to be called by a {@link java.util.ServiceLoader}
   */
  public DetectableAnnotationProcessor() {
    this.autoFoundClasses = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  public MethodSpec.Builder createMethod() {
    ClassName listClass = ClassName.get(List.class);
    ClassName foundAnnotationClass = ClassName.get(DetectableAnnotationProvider.DetectableAnnotationMeta.class);

    // Create a method with the signature
    return MethodSpec.methodBuilder("registerAutoFound")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(
            ParameterizedTypeName.get(listClass, foundAnnotationClass),
            "consumer")
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
    if (annotationType.getAnnotation(DetectableAnnotation.class) == null) return;
    for (Element annotatedElement : ProcessorState.getInstance().getCurrentRoundEnvironment().getElementsAnnotatedWith(annotationType)) {
      Map<ExecutableElement, AnnotationValue> annotationValues = collectAnnotationData(annotationType, annotatedElement);
      System.out.println(annotatedElement + " " + annotationType);
      String annotationTemplate = createAnnotationTemplate(annotationType, annotationValues, annotationType.toString());
      this.autoFoundClasses.put(annotatedElement, annotationTemplate);
    }
  }

  private Map<ExecutableElement, AnnotationValue> collectAnnotationData(TypeElement typeElement, Element annotatedElement) {
    Map<ExecutableElement, AnnotationValue> classValues = new HashMap<>(SimpleAnnotationMirror.of(typeElement).getElementValues());
    if (annotatedElement != null) {
      Map<ExecutableElement, AnnotationValue> instanceValues = new HashMap<>(annotatedElement.getAnnotationMirrors().stream().filter(annotationMirror -> annotationMirror.getAnnotationType().asElement().asType().equals(typeElement.asType())).map(AnnotationMirror::getElementValues).findAny().orElse(new HashMap<>()));
      classValues.putAll(instanceValues);
    }
    return classValues;
  }

  private String createMethodTemplate(ExecutableElement executableElement, Map<ExecutableElement, AnnotationValue> annotationValues) {
    return handleTemplate(ImmutableMap.<String, String>builder()
        .put("NAME", executableElement.getSimpleName().toString())
        .put("RETURN_TYPE_NAME", executableElement.getReturnType().toString())
        .put("RETURN_VALUE", createMethodReturnTemplate(executableElement, annotationValues))
        .build(), METHOD_TEMPLATE);
  }

  private String createMethodReturnTemplate(ExecutableElement executableElement, Map<ExecutableElement, AnnotationValue> annotationValues) {
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
        return createAnnotationTemplate(((TypeElement) a.getAnnotationType().asElement()), classValues, a.getAnnotationType().toString());
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

  private String createAnnotationTemplate(TypeElement annotationType, Map<ExecutableElement, AnnotationValue> annotationValues, String typeName) {
    StringBuilder methods = new StringBuilder();

    for (Element enclosedElement : annotationType.getEnclosedElements()) {
      if (!(enclosedElement instanceof ExecutableElement)) continue;
      ExecutableElement executableElement = (ExecutableElement) enclosedElement;
      if (executableElement.getSimpleName().toString().equals("<init>") || enclosedElement.getSimpleName().toString().equals("<clinit>"))
        continue;

      methods.append(createMethodTemplate(executableElement, annotationValues))
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

  private void handleTypeElement(TypeElement element, AnnotationMirror annotation) {
    System.out.println("Handle " + element + " with " + annotation);
  }

  /**
   * {@inheritDoc}
   */
  public void finish(MethodSpec.Builder targetMethod) {

    // Add a statement for all classes
    this.autoFoundClasses.forEach((element, string) -> {
      String identifier;
      switch (element.getKind()) {
        case CLASS:
        case ANNOTATION_TYPE:
        case INTERFACE:
          identifier = element.toString();
          break;
        case METHOD:
          identifier = element.getEnclosingElement().toString() + "." + element.toString();
          break;
        default:
          throw new IllegalStateException();
      }
      targetMethod.addStatement("consumer.add(new net.labyfy.component.processing.autoload.DetectableAnnotationProvider.DetectableAnnotationMeta(" + ElementKind.class.getName() + "." + element.getKind().name() + ", $S, " + string + "))", identifier);
    });

  }


}
