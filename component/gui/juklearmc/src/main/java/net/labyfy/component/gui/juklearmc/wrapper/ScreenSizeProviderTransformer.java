package net.labyfy.component.gui.juklearmc.wrapper;

import javassist.*;
import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@AutoLoad
public class ScreenSizeProviderTransformer {
  private final ClassMappingProvider mappingProvider;

  @Inject
  private ScreenSizeProviderTransformer(ClassMappingProvider mappingProvider) {
    this.mappingProvider = mappingProvider;
  }

  @ClassTransform(
      value = "net.minecraft.client.gui.screen.Screen"
  )
  public void transformScreen(ClassTransformContext context) throws NotFoundException, CannotCompileException {
    CtClass screenClass = context.getCtClass();
    ClassPool classPool = screenClass.getClassPool();
    CtClass screenSizeProviderClass = classPool.get("net.labyfy.component.gui.juklearmc.wrapper.ScreenSizeProvider");

    screenClass.addInterface(screenSizeProviderClass);

    ClassMapping screenMapping = mappingProvider.get("net.minecraft.client.gui.screen.Screen");
    String widthField = screenMapping.getField("width").getName();
    String heightField = screenMapping.getField("height").getName();

    CtMethod getWidthMethod = CtMethod.make(
        "public int getWidth() { return this." + widthField + "; }",
        screenClass
    );

    CtMethod getHeightMethod = CtMethod.make(
        "public int getHeight() { return this." + heightField + "; }",
        screenClass
    );

    screenClass.addMethod(getWidthMethod);
    screenClass.addMethod(getHeightMethod);
  }
}
