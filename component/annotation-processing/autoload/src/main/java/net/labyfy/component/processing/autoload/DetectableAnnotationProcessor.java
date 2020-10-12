package net.labyfy.component.processing.autoload;

import com.google.auto.common.SimpleAnnotationMirror;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableMap;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import net.labyfy.component.commons.annotation.AnnotationMirrorUtil;
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
      String parsedAnnotation = parseAnnotation(annotationType, annotatedElement);
      if (parsedAnnotation.isEmpty()) continue;

      this.found.add("list.add(" + parsedAnnotation + ")");
    }

  }

  private String parseAnnotation(TypeElement annotationType, Element annotatedElement) {
    if (annotationType.getAnnotation(DetectableAnnotation.class) == null) return "";

    String annotationTemplate = createAnnotationTemplate(annotationType, collectAnnotationData(annotationType, annotatedElement), annotationType.toString());
    StringBuilder output = new StringBuilder();
    output
        .append("new net.labyfy.component.processing.autoload.AnnotationMeta(javax.lang.model.element.ElementKind.")
        .append(annotatedElement.getKind().name())
        .append(", ");

    switch (annotatedElement.getKind()) {
      case CLASS:
      case ANNOTATION_TYPE:
      case INTERFACE:
        output.append("new net.labyfy.component.processing.autoload.AnnotationMeta.ClassIdentifier(\"")
            .append(ProcessorState.getInstance().getProcessingEnvironment().getElementUtils().getBinaryName((TypeElement) annotatedElement).toString()
                .replace("$", "$$")
            )
            .append("\"), ");
        break;
      case METHOD:
        output
            .append("new net.labyfy.component.processing.autoload.AnnotationMeta.MethodIdentifier(\"")
            .append(ProcessorState.getInstance().getProcessingEnvironment().getElementUtils().getBinaryName((TypeElement) annotatedElement.getEnclosingElement()).toString()
                .replace("$", "$$")
            )
            .append("\",\"")
            .append(annotatedElement.getSimpleName())
            .append("\"");

        if (((ExecutableElement) annotatedElement).getParameters().size() > 0) {
          output.append(", ");
        }

        boolean semicolon = false;
        for (VariableElement parameter : ((ExecutableElement) annotatedElement).getParameters()) {
          if (semicolon) {
            output.append(", ");
          }

          String parameterName;

          if (parameter.asType() instanceof DeclaredType) {
            Element element = ((DeclaredType) parameter.asType()).asElement();
            parameterName = ProcessorState.getInstance().getProcessingEnvironment().getElementUtils().getBinaryName(((TypeElement) element)).toString();
          } else {
            parameterName = parameter.asType().toString();
          }

          parameterName = parameterName.replace("$", "$$");

          output.append("\"")
              .append(parameterName)
              .append("\"");
          semicolon = true;
        }

        output
            .append("), ");
        break;
      default:
        throw new IllegalStateException();
    }

    output
        .append(annotationTemplate);

    for (TypeElement metaElement : getAnnotationSubMetaTypes(annotationType)) {
      AnnotationMirror annotationMirror = AnnotationMirrorUtil.getAnnotationMirror(annotatedElement, metaElement.getQualifiedName().toString());
      if (annotationMirror == null) continue;

      output.append(", ")
          .append(parseAnnotation(metaElement, annotatedElement));
    }

    output.append(")");
    return output.toString();
  }


  private Collection<TypeElement> getAnnotationSubMetaTypes(TypeElement annotationType) {
    TypeElement metaTypeElement = ProcessorState.getInstance().getProcessingEnvironment().getElementUtils().getTypeElement(DetectableAnnotation.class.getName());
    AnnotationMirror metaAnnotationMirror = annotationType.getAnnotationMirrors().stream().filter(annotationMirror -> annotationMirror.getAnnotationType().asElement().asType().equals(metaTypeElement.asType())).findAny().orElse(null);

    List<TypeElement> metaClasses = new ArrayList<>();
    if (metaAnnotationMirror != null) {
      AnnotationMirror annotationMirror = SimpleAnnotationMirror.of(metaTypeElement, metaAnnotationMirror.getElementValues().entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getSimpleName().toString(), Map.Entry::getValue)));
      AnnotationValue metaData = annotationMirror.getElementValues().get(annotationMirror.getElementValues().keySet().stream().filter(executableElement -> executableElement.getSimpleName().toString().equals("metaData")).findAny().get());
      if (metaData != null) {
        ((List<AnnotationValue>) metaData.getValue())
            .stream()
            .map(AnnotationValue::getValue)
            .map(DeclaredType.class::cast)
            .map(DeclaredType::asElement)
            .map(TypeElement.class::cast)
            .forEach(metaClasses::add);
      }
    }
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


  /**
   * {@inheritDoc}
   */
  public void finish(MethodSpec.Builder targetMethod) {

    // Add a statement for all classes
    this.found.forEach(string -> {
      targetMethod.addStatement(string);
    });

  }


}
