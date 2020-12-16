package net.flintmc.framework.config.internal.generator.base;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.ConfigImplementer;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.inject.implement.Implement;

@Singleton
@Implement(ConfigImplementer.class)
public class DefaultConfigImplementer implements ConfigImplementer {

  private final ClassPool pool;

  @Inject
  private DefaultConfigImplementer(ClassPool pool) {
    this.pool = pool;
  }

  @Override
  public void implementParsedConfig(CtClass implementation, String name)
      throws NotFoundException, CannotCompileException {
    for (CtClass anInterface : implementation.getInterfaces()) {
      if (anInterface.getName().equalsIgnoreCase(ParsedConfig.class.getName())) {
        return;
      }
    }

    implementation.addInterface(this.pool.get(ParsedConfig.class.getName()));

    CtField referencesField =
        CtField.make(
            "private final transient java.util.Collection references = new java.util.HashSet();",
            implementation);
    implementation.addField(referencesField);
    implementation.addMethod(CtNewMethod.getter("getConfigReferences", referencesField));

    String escapedName = name.replace("\\", "\\\\").replace("\"", "\\\"");
    implementation.addMethod(
        CtNewMethod.make(
            "public java.lang.String getConfigName() { return \"" + escapedName + "\"; }",
            implementation));
  }
}
