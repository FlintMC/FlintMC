package net.flintmc.mcapi.v1_15_2.server.payload;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.server.payload.PayloadChannelService;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
;

@Singleton
public class CustomPayloadInterceptor {

  private static final int[] LOGGER_WARN_SEQUENCE =
      new int[] {Opcode.GETSTATIC, Opcode.LDC_W, Opcode.ALOAD_2, Opcode.INVOKEINTERFACE};

  private final ClassPool pool;
  private final ClassMapping customPayloadPacketMapping;

  @Inject
  private CustomPayloadInterceptor(ClassPool pool, ClassMappingProvider classMappingProvider) {
    this.pool = pool;
    this.customPayloadPacketMapping =
        classMappingProvider.get("net.minecraft.network.play.server.SCustomPayloadPlayPacket");
  }

  @ClassTransform("net.minecraft.client.network.play.ClientPlayNetHandler")
  public void transform(ClassTransformContext context)
      throws NotFoundException, BadBytecode, CannotCompileException {

    CtMethod handleCustomPayloadMethod =
        context
            .getCtClass()
            .getDeclaredMethod(
                "handleCustomPayload",
                new CtClass[] {
                  this.pool.get("net.minecraft.network.play.server.SCustomPayloadPlayPacket")
                });

    MethodInfo methodInfo = handleCustomPayloadMethod.getMethodInfo();
    CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
    CodeIterator codeIterator = codeAttribute.iterator();

    int count = 0;

    while (codeIterator.hasNext()) {
      int index = codeIterator.next();
      int opcode = codeIterator.byteAt(index);

      if (LOGGER_WARN_SEQUENCE[count] != opcode) {
        count = 0;
        continue;
      }

      ++count;
      if (count < LOGGER_WARN_SEQUENCE.length - 1) {
        continue;
      }

      int line = methodInfo.getLineNumber(index);

      handleCustomPayloadMethod.insertAt(
          line,
          String.format(
              "if(((%s)%s.getInjectedInstance(%s.class)).shouldListen($1.%s().toString(), $1.%s())) {return;}",
              PayloadChannelService.class.getName(),
              InjectionHolder.class.getName(),
              PayloadChannelService.class.getName(),
              this.customPayloadPacketMapping.getMethod("getChannelName").getName(),
              this.customPayloadPacketMapping.getMethod("getBufferData").getName()));
      break;
    }
  }
}
