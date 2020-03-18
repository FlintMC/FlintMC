package net.labyfy.component.gui.v1_15_1;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import javassist.CtMethod;
import net.labyfy.component.gui.GuiMethodResolver;
import net.labyfy.component.gui.GuiRenderState;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;

@Singleton
@Implement(GuiMethodResolver.class)
public class VersionedGuiMethodResolver implements GuiMethodResolver {

  private final ClassMappingProvider mappingProvider;

  @Inject
  private VersionedGuiMethodResolver(ClassMappingProvider mappingProvider) {
    this.mappingProvider = mappingProvider;
  }

  public CtMethod resolve(CtClass ctClass, GuiRenderState.Type renderState) {
    try {
      ClassMapping classMapping = mappingProvider.get(ctClass.getName());
      switch (renderState) {
        case INIT:
          return ctClass.getDeclaredMethod(classMapping.getMethod("init").getName());

        case RENDER:
          return ctClass.getDeclaredMethod(
              classMapping.getMethod("render", int.class, int.class, float.class).getName(),
              new CtClass[] {CtClass.intType, CtClass.intType, CtClass.floatType});
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }
}
