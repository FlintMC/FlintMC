package net.labyfy.component.annotation.processing;

import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class LabyfyAnnotationProcessor extends AbstractProcessor {
  private final ProcessorState state;

  public LabyfyAnnotationProcessor() {
    this.state = new ProcessorState();
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    state.init(processingEnv);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    state.round(roundEnv);

    try {
      for (TypeElement element : annotations) {
        //        if
        // (element.getQualifiedName().contentEquals(ProcessingConstants.AUTO_LOAD_ANNOTATION_NAME))
        // {
        state.processAutoLoad(element);
        //        }
      }
    } catch (ProcessingException e) {
      processingEnv
          .getMessager()
          .printMessage(
              Diagnostic.Kind.ERROR,
              "Exception thrown while processing annotations: " + e.getMessage(),
              e.getSourceElement());
      throw e;
    }

    if (roundEnv.processingOver()) {
      state.finish();
    }

    return true;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return new HashSet<>(Collections.singletonList("*"));
  }
}
