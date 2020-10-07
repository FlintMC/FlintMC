package net.labyfy.component.blub123;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import javax.inject.Singleton;

@Implement(Test123.class)
@AutoLoad
@Singleton
public class Test1234 implements Test123 {

  @ClassTransform("net.minecraft.client.gui.screen.MainMenuScreen")
  public void transform(ClassTransformContext classTransformContext) throws NotFoundException, CannotCompileException {
    classTransformContext.getCtClass().getDeclaredMethod("render", new CtClass[]{CtClass.intType, CtClass.intType, CtClass.floatType}).setBody("{}");
  }

}
