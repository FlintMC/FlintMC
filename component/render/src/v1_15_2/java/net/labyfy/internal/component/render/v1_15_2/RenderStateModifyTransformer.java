package net.labyfy.internal.component.render.v1_15_2;

import com.google.inject.Singleton;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

@Singleton
public class RenderStateModifyTransformer {

  @ClassTransform("net.minecraft.client.renderer.RenderType$State")
  public void transform(ClassTransformContext classTransformContext) throws NotFoundException {
    CtField render = classTransformContext.getCtClass().getDeclaredField("renderStates");
    render.setModifiers(Modifier.clear(render.getModifiers(), Modifier.FINAL));
  }

}
