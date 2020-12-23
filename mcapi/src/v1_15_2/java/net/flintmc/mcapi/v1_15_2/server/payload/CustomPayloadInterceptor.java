package net.flintmc.mcapi.v1_15_2.server.payload;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.mcapi.server.ConnectedServer;
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
  private final InjectedFieldBuilder.Factory fieldBuilderFactory;

  @Inject
  private CustomPayloadInterceptor(
      ClassPool pool,
      ClassMappingProvider classMappingProvider,
      InjectedFieldBuilder.Factory fieldBuilderFactory) {
    this.pool = pool;
    this.customPayloadPacketMapping =
        classMappingProvider.get("net.minecraft.network.play.server.SCustomPayloadPlayPacket");
    this.fieldBuilderFactory = fieldBuilderFactory;
  }

  @ClassTransform("net.minecraft.client.network.play.ClientPlayNetHandler")
  public void transform(ClassTransformContext context)
      throws NotFoundException, BadBytecode, CannotCompileException {

    CtMethod method =
        context
            .getCtClass()
            .getDeclaredMethod(
                "handleCustomPayload",
                new CtClass[] {
                  this.pool.get("net.minecraft.network.play.server.SCustomPayloadPlayPacket")
                });

    MethodInfo methodInfo = method.getMethodInfo();
    CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
    CodeIterator codeIterator = codeAttribute.iterator();

    int count = 0;

    CtField connectedInjectedService =
        this.fieldBuilderFactory
            .create()
            .target(method.getDeclaringClass())
            .inject(ConnectedServer.class)
            .generate();

    method.insertBefore(
        String.format(
            "{%s.retrieveCustomPayload($1.%s().toString(), $1.%s());}",
            connectedInjectedService.getName(),
            this.customPayloadPacketMapping.getMethod("getChannelName").getName(),
            this.customPayloadPacketMapping.getMethod("getBufferData").getName()));

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

      CtField injectedService =
          this.fieldBuilderFactory
              .create()
              .target(method.getDeclaringClass())
              .inject(PayloadChannelService.class)
              .generate();

      method.insertAt(
          line,
          String.format(
              "if (%s.shouldListen($1.%s().toString(), $1.%s())) {return;}",
              injectedService.getName(),
              this.customPayloadPacketMapping.getMethod("getChannelName").getName(),
              this.customPayloadPacketMapping.getMethod("getBufferData").getName()));
      break;
    }
  }
}
