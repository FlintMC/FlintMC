package net.flintmc.transform.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class ASMUtils {

  /**
   * Gets the bytes of a given ClassNode.
   *
   * @param cn
   * @param useMaxs
   * @return
   */
  public static byte[] getNodeBytes(ClassNode cn, boolean useMaxs) {
    ClassWriter cw =
        new ClassWriter(useMaxs ? ClassWriter.COMPUTE_MAXS : ClassWriter.COMPUTE_FRAMES);
    cn.accept(cw);
    byte[] b = cw.toByteArray();
    return b;
  }

  public static byte[] getNodeBytes0(ClassNode cn) {
    ClassWriter cw = new ClassWriter(0);
    cn.accept(cw);
    byte[] b = cw.toByteArray();
    return b;
  }

  /**
   * Parse class node.
   *
   * @param bytes Raw class data.
   * @return The class node constructed from the raw bytes.
   */
  public static ClassNode getNode(final byte[] bytes) {
    ClassReader cr = new ClassReader(bytes);
    ClassNode cn = new ClassNode();

    try {
      cr.accept(cn, ClassReader.EXPAND_FRAMES);
    } catch (Exception exception) {
      cr.accept(cn, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
    }

    return cn;
  }

  /**
   * Generates a getter method for the specified field
   *
   * @param fieldName the name of the field
   * @param className the internal class name
   * @param fieldDesc the field descriptor
   * @param methodName the name of the method to create
   * @return the method as a {@link org.objectweb.asm.tree.MethodNode}
   * @author Xerces
   */
  public static MethodNode generateGetter(
      String methodName, String fieldName, String className, String fieldDesc) {
    MethodNode methodNode = new MethodNode(ACC_PUBLIC, methodName, "()" + fieldDesc, null, null);
    methodNode.instructions.insert(new VarInsnNode(ALOAD, 0));
    methodNode.instructions.insert(new FieldInsnNode(GETFIELD, className, fieldName, fieldDesc));
    methodNode.instructions.insert(new InsnNode(Type.getType(fieldDesc).getOpcode(IRETURN)));
    return methodNode;
  }

  /**
   * Generates a setter method for the specified field
   *
   * @param fieldName the name of the field
   * @param className the internal class name
   * @param fieldDesc the field descriptor
   * @param methodName the name of the method to create
   * @return the method as a {@link org.objectweb.asm.tree.MethodNode}
   * @author Xerces
   */
  public static MethodNode generateSetter(
      String methodName, String fieldName, String className, String fieldDesc) {
    MethodNode methodNode =
        new MethodNode(ACC_PUBLIC, methodName, "(" + fieldDesc + ")V", null, null);
    methodNode.instructions.insert(new VarInsnNode(ALOAD, 0));
    methodNode.instructions.insert(new VarInsnNode(Type.getType(fieldDesc).getOpcode(ILOAD), 1));
    methodNode.instructions.insert(new FieldInsnNode(PUTFIELD, className, fieldName, fieldDesc));
    methodNode.instructions.insert(new InsnNode(RETURN));
    return methodNode;
  }

  /**
   * Adds interfaces to a class
   *
   * @param classNode the {@link org.objectweb.asm.tree.ClassNode} to add the interfaces too
   * @param interfaces a {@link Class} array of the interfaces to add
   * @author Xerces
   */
  public static void addInterfaces(ClassNode classNode, Class<?>[] interfaces) {
    for (Class<?> interfaceClass : interfaces) {
      if (interfaceClass.isInterface()) {
        classNode.interfaces.add(interfaceClass.getName().replaceAll(".", "/"));
      }
    }
  }
}