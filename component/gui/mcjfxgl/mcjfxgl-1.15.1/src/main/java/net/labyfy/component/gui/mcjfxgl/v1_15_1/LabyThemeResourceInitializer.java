package net.labyfy.component.gui.mcjfxgl.v1_15_1;

import javassist.CannotCompileException;
import javassist.CtMethod;
import net.labyfy.component.gui.mcjfxgl.component.theme.ThemeResourceInitializer;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.FallbackResourceManager;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.SimpleReloadableResourceManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.function.Predicate;

@Singleton
@Implement(value = ThemeResourceInitializer.class, version = "1.15.1")
public class LabyThemeResourceInitializer implements ThemeResourceInitializer {

  private final ClassMappingProvider classMappingProvider;

  static {
    System.setProperty( "org.lwjgl.opengl.Window.undecorated", "true" );

  }

  @Inject
  private LabyThemeResourceInitializer(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  public void initialize() {
    Map<String, FallbackResourceManager> namespaceResourceManagers =
        this.classMappingProvider
            .get(SimpleReloadableResourceManager.class.getName())
            .getField("namespaceResourceManagers")
            .getValue(Minecraft.getInstance().getResourceManager());

    namespaceResourceManagers.put(
        "labyfy", new FallbackResourceManager(ResourcePackType.CLIENT_RESOURCES, "labyfy"));
  }

  @ClassTransform(
      value = "net.minecraft.client.resources.LegacyResourcePackWrapper",
      version = "1.15.1")
  public void transform(ClassTransformContext classTransformContext) throws CannotCompileException {
    CtMethod method =
        classTransformContext.getDeclaredMethod(
            "getAllResourceLocations",
            ResourcePackType.class,
            String.class,
            String.class,
            int.class,
            Predicate.class);
    method.setBody(
        "return this."
            + this.classMappingProvider
                .get("net.minecraft.client.resources.LegacyResourcePackWrapper")
                .getField("locationMap")
                .getName()
            + ".getAllResourceLocations($1, $2, $3, $4, $5);");
  }
}
