package net.flintmc.mcapi.v1_15_2.player.transformer;

import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import net.flintmc.mcapi.player.FieldOfViewModifier;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;

public class VersionedAbstractClientPlayerTransformer {

  private static final String FOV_MODIFIER_CLASS = FieldOfViewModifier.class.getName();
  private static final int LOCAL_FOV = 1;

  @ClassTransform("net.minecraft.client.entity.player.AbstractClientPlayerEntity")
  public void transform(ClassTransformContext context) throws NotFoundException, BadBytecode {
    CtMethod fovModifierMethod = context.getDeclaredMethod("getFovModifier");

    MethodInfo fovModifierMethodInfo = fovModifierMethod.getMethodInfo();
    CodeAttribute codeAttribute = fovModifierMethodInfo.getCodeAttribute();
    CodeIterator codeIterator = codeAttribute.iterator();

    int lastIndex = -1;
    while (codeIterator.hasNext()) {
      int index = codeIterator.next();
      int opcode = codeIterator.byteAt(index);

      if (opcode == Opcode.FRETURN) {

        Bytecode bytecode = new Bytecode(fovModifierMethodInfo.getConstPool(), 0, 0);

        bytecode.addLdc(fovModifierMethodInfo.getConstPool().addClassInfo(FOV_MODIFIER_CLASS));
        bytecode.addInvokestatic(
            "net.flintmc.framework.inject.primitive.InjectionHolder",
            "getInjectedInstance",
            "(Ljava/lang/Class;)Ljava/lang/Object;");
        bytecode.addCheckcast(FOV_MODIFIER_CLASS);
        bytecode.addFload(LOCAL_FOV);
        bytecode.addInvokeinterface(FOV_MODIFIER_CLASS, "fieldOfView", "(F)F", 2);
        bytecode.addFstore(LOCAL_FOV);

        if (lastIndex != -1) {
          codeIterator.insert(lastIndex, bytecode.get());
        }
      }

      lastIndex = index;
    }
  }
}
