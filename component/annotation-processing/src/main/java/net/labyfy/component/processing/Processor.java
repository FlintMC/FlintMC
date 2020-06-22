package net.labyfy.component.processing;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.TypeElement;

public interface Processor {
  void accept(TypeElement typeElement);

  MethodSpec.Builder createMethod();

  ClassName getGeneratedClassSuperClass();

  void finish(MethodSpec.Builder targetMethod);


}
