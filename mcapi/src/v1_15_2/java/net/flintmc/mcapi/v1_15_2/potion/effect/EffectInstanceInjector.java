package net.flintmc.mcapi.v1_15_2.potion.effect;

import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.minecraft.potion.Effect;

@Singleton
public class EffectInstanceInjector {
  /*
    @Hook(
            className = "net.minecraft.potion.EffectInstance",
            methodName = "<init>",
            parameters = {
                    @Type(reference = Effect.class),
                    @Type(reference = int.class),
                    @Type(reference = int.class),
                    @Type(reference = boolean.class),
                    @Type(reference = boolean.class),
                    @Type(reference = boolean.class)
            }
    )
    public void hookConstructor(@Named("args") Object[] args) {
      System.out.println("Injected!");
    }
  */
  @ClassTransform("net.minecraft.potion.EffectInstance")
  public void hookEffectInstance(ClassTransformContext classTransformContext) throws NotFoundException, CannotCompileException {
    ClassPool pool = classTransformContext.getCtClass().getClassPool();
    CtClass ctClass = classTransformContext.getCtClass();
    CtClass livingEntity = pool.get("net.minecraft.entity.LivingEntity");

    livingEntity.getDeclaredMethod("onNewPotionEffect", new CtClass[] {pool.get("net.minecraft.potion.EffectInstance")}).insertAfter(
            "java.lang.System.out.println(\"A new potion!\");"
    );


    ctClass.getDeclaredConstructor(
            new CtClass[] {
                    pool.get("net.minecraft.potion.Effect"),
                    CtClass.intType,
                    CtClass.intType,
                    CtClass.booleanType,
                    CtClass.booleanType,
                    CtClass.booleanType
            }
    ).insertAfter("java.lang.System.out.println(\"Injected!\" + $2);");
  }

}
