package net.flintmc.mcapi.v1_15_2.server.event;

import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Opcode;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;

@Singleton
public class ServerListUpdateEventInjector {

  @ClassTransform("net.minecraft.client.multiplayer.ServerList")
  public void transformServerList(ClassTransformContext context)
      throws NotFoundException, CannotCompileException, BadBytecode {
    CtClass transforming = context.getCtClass();
    CtField field = transforming.getDeclaredField("servers");
    field.setModifiers(field.getModifiers() & ~Modifier.FINAL);

    CtMethod method = transforming.getDeclaredMethod("loadServerList");

    String base = String.format("((%s) this.servers).setEnabled(", ModServerList.class.getName());
    String disable = base + "false);";
    String enable = base + "true);";

    method.insertBefore(disable);

    CodeIterator iterator = method.getMethodInfo().getCodeAttribute().iterator();
    while (iterator.hasNext()) {
      int index = iterator.next();
      int opcode = iterator.byteAt(index);

      if (opcode == Opcode.RETURN) {
        int line = method.getMethodInfo().getLineNumber(index);

        // insert reset before the first return statement
        method.insertAt(line, enable);
        break;
      }
    }

    // insert reset after anything else
    method.insertAfter(enable);

    // before anything else, change the ArrayList to our own List which will fire events
    method.insertBefore(
        String.format(
            "this.servers = (%s) %s.getInjectedInstance(%1$s.class);",
            ModServerList.class.getName(), InjectionHolder.class.getName()));
  }
}
