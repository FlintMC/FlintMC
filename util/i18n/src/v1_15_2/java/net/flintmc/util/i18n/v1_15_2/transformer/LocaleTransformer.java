package net.flintmc.util.i18n.v1_15_2.transformer;

import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.i18n.Localization;

/**
 * A transformer that adds the {@link Localization} interface to Minecraft locale.
 */
@Singleton
public class LocaleTransformer {

  @ClassTransform("net.minecraft.client.resources.Locale")
  public void transform(ClassTransformContext context)
      throws NotFoundException, CannotCompileException {
    CtClass ctClass = context.getCtClass();

    CtClass localization = ctClass.getClassPool().get(Localization.class.getName());
    ctClass.addInterface(localization);

    ctClass.addMethod(
        CtMethod.make(
            "public java.util.Map getProperties() {" + "return " + context.getField("properties")
                .getName() + ";}", ctClass));
    ctClass.addMethod(
        CtMethod.make(
            "public void add(String key, String translation) {" + context.getField("properties")
                .getName() + ".put($$);}", ctClass));
  }
}
