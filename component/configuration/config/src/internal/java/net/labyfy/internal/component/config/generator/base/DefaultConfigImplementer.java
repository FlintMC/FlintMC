package net.labyfy.internal.component.config.generator.base;

import com.google.inject.Singleton;
import javassist.*;
import net.labyfy.component.config.generator.ConfigImplementer;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.inject.implement.Implement;

@Singleton
@Implement(ConfigImplementer.class)
public class DefaultConfigImplementer implements ConfigImplementer {

  private final ClassPool pool;

  public DefaultConfigImplementer() {
    this.pool = ClassPool.getDefault();
  }

  @Override
  public void implementParsedConfig(CtClass implementation, String name) throws NotFoundException, CannotCompileException {
    for (CtClass anInterface : implementation.getInterfaces()) {
      if (anInterface.getName().equalsIgnoreCase(ParsedConfig.class.getName())) {
        return;
      }
    }

    implementation.addInterface(this.pool.get(ParsedConfig.class.getName()));

    CtField referencesField = CtField.make("private final transient java.util.Collection references = new java.util.HashSet();", implementation);
    implementation.addField(referencesField);
    implementation.addMethod(CtNewMethod.getter("getConfigReferences", referencesField));

    String escapedName = name.replace("\\", "\\\\").replace("\"", "\\\"");
    implementation.addMethod(CtNewMethod.make("public java.lang.String getConfigName() { return \"" + escapedName + "\"; }", implementation));
  }
}
