package net.labyfy.component.i18n.transformer;

import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.i18n.Localization;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

/**
 * A transformer that adds the {@link Localization} interface to Minecraft locale.
 */
@Singleton
@AutoLoad
public class LocaleTransformer {

  @ClassTransform("net.minecraft.client.resources.Locale")
  public void transform(ClassTransformContext context) throws NotFoundException, CannotCompileException {
    CtClass ctClass = context.getCtClass();

    CtClass localization = ctClass.getClassPool().get(Localization.class.getName());
    ctClass.addInterface(localization);

    ctClass.addMethod(CtMethod.make("public java.util.Map getProperties() {" + "return properties;}", ctClass));
    ctClass.addMethod(CtMethod.make("public void add(String key, String translation) {" + "properties.put($$);}", ctClass));
  }

}
