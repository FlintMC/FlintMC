package net.flintmc.mcapi.v1_15_2.server.payload;

import com.google.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javassist.CannotCompileException;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.server.ConnectedServer;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.util.ResourceLocation;

@Singleton
public class CustomPayloadInterceptor {

  private static final String CONNECTED_SERVER_CLASS = ConnectedServer.class.getName();
  private static final String INJECTION_HOLDER_CLASS = InjectionHolder.class.getName();

  @ClassTransform("net.minecraft.client.network.play.ClientPlayNetHandler")
  public void transform(ClassTransformContext context)
      throws NotFoundException, BadBytecode, CannotCompileException {
    CtMethod handleCustomPayloadMethod =
        context.getDeclaredMethod("handleCustomPayload", SCustomPayloadPlayPacket.class);

    MethodInfo methodInfo = handleCustomPayloadMethod.getMethodInfo();
    CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
    CodeIterator iterator = codeAttribute.iterator();

    while (iterator.hasNext()) {
      int index = iterator.next();
      int opcode = iterator.byteAt(index);

      if (opcode == Opcode.INVOKEINTERFACE) {
        String name = methodInfo.getConstPool().getMethodrefName(iterator.u16bitAt(index + 1));

        System.out.println(name);

        if (name.equals("warn")) {
          System.out.println("Found!");
          Bytecode bytecode = new Bytecode(methodInfo.getConstPool(), 0, 0);
          bytecode.addLdc(methodInfo.getConstPool().addClassInfo(CONNECTED_SERVER_CLASS));
          bytecode.addInvokestatic(
              INJECTION_HOLDER_CLASS,
              "getInjectedInstance",
              "(Ljava/lang/Class;)Ljava/lang/Object;");
          bytecode.addCheckcast(CONNECTED_SERVER_CLASS);

          bytecode.addNew("java/lang/StringBuilder");
          bytecode.add(Opcode.DUP);
          bytecode.addInvokespecial("java.lang/StringBuilder", "<init>", "()V");

          bytecode.addAload(2);
          bytecode.addInvokevirtual(
              "net/minecraft/util/ResourceLocation", "getNamespace", "()Ljava/lang/String;");
          bytecode.addInvokevirtual(
              "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");

          bytecode.addLdc(":");
          bytecode.addInvokevirtual(
              "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");

          bytecode.addAload(2);
          bytecode.addInvokevirtual(
              "net/minecraft/util/ResourceLocation", "getPath", "()Ljava/lang/String;");
          bytecode.addInvokevirtual(
              "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
          bytecode.addInvokevirtual("java/lang/StringBuilder", "toString", "()Ljava/lang/String;");


          bytecode.addAload(3);
          bytecode.addInvokevirtual("net/minecraft/network/PacketBuffer", "array", "()[B");

          bytecode.addInvokeinterface(
              CONNECTED_SERVER_CLASS, "retrieveCustomPayload", "(Ljava/lang/String;[B)V", 3);

          System.out.println("Insert..");
          iterator.insert(index, bytecode.get());
          System.out.println("Inserted!");
        }
      }
    }

    try {
      Files.write(
          Paths.get(context.getCtClass().getName() + ".class"), context.getCtClass().toBytecode());
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  private void test(SCustomPayloadPlayPacket packet) {
    ResourceLocation resourceLocation = packet.getChannelName();
    PacketBuffer packetBuffer = packet.getBufferData();

    InjectionHolder.getInjectedInstance(ConnectedServer.class)
        .retrieveCustomPayload(
            resourceLocation.getNamespace() + ":" + resourceLocation.getPath(),
            packetBuffer.readByteArray());
  }
}
