package net.labyfy.downloader;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.mappings.FieldMapping;
import net.labyfy.component.mappings.MethodMapping;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class LabyDeobfuscator {

  private final ClassMappingProvider classMappingProvider;
  private final File input;
  private final File output;
  private final Map<String, String> mappings = Maps.newConcurrentMap();

  private Map<String, ClassNode> classes;
  private Map<String, byte[]> bytes;

  @Inject
  private LabyDeobfuscator(
      @Named("input") File input,
      @Named("output") File output,
      ClassMappingProvider classMappingProvider)
      throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    this.classMappingProvider = classMappingProvider;
    this.input = input;
    this.output = output;
    this.loadJar();
    this.loadMappings();
    this.applyClassMappings();
    this.clearManifest();
    this.saveJar();
  }

  private void clearManifest() {
    this.bytes.remove("META-INF/MANIFEST.MF");
    this.bytes.remove("META-INF/MOJANGCS.RSA");
    this.bytes.remove("META-INF/MOJANGCS.SF");
  }

  private void saveJar() {
    JarUtils.saveAsJar(this.bytes, this.output);
  }

  private void applyClassMappings()
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException,
          InvocationTargetException {
    Remapper mapper =
        SimpleSimpleRemapper.create(
            this.mappings, new ReflectionSuperClassProvider(input, "versioned/labyfy-1.15.1/libraries"));

    this.classes.forEach(
        (name, classNode) -> {
          if (this.mappings.containsKey(classNode.name)
              && !this.mappings.get(classNode.name).equals(classNode.name)) {
            System.out.println(
                " -> rewrite "
                    + classNode.name
                    + " to "
                    + this.mappings.get(classNode.name).replace('/', '.'));
          }
          ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
          ClassVisitor classRemapper = new ClassRemapper(classWriter, mapper);
          classNode.accept(classRemapper);

          this.bytes.put(
              (this.mappings.containsKey(classNode.name)
                      ? this.mappings.get(classNode.name)
                      : classNode.name)
                  + ".class",
              classWriter.toByteArray());
        });
  }

  private void loadMappings() {
    this.loadClassMappings();
    this.loadMethodMappings();
    this.loadFieldMappings();
  }

  private void loadFieldMappings() {
    this.mappings.putAll(
        this.classMappingProvider.getObfuscatedClassMappings().values().stream()
            .flatMap(classMapping -> classMapping.getFields().stream())
            .collect(
                Collectors.toMap(
                    field ->
                        field.getClassMapping().getObfuscatedName()
                            + "."
                            + field.getObfuscatedFieldName(),
                    FieldMapping::getUnObfuscatedFieldName)));
  }

  private void loadMethodMappings() {
    this.mappings.putAll(
        this.classMappingProvider.getObfuscatedClassMappings().values().stream()
            .flatMap(classMapping -> classMapping.getMethods().stream())
            .collect(
                Collectors.toMap(
                    method ->
                        method.getClassMapping().getObfuscatedName()
                            + "."
                            + method.getObfuscatedMethodIdentifier(),
                    MethodMapping::getUnObfuscatedMethodName)));
  }

  private void loadClassMappings() {
    this.classMappingProvider
        .getObfuscatedClassMappings()
        .forEach(
            (obfuscatedName, mapping) ->
                this.mappings.put(
                    obfuscatedName.replace('.', '/'),
                    mapping.getUnObfuscatedName().replace('.', '/')));
  }

  private void loadJar() throws IOException {
    this.classes = JarUtils.loadClasses(input);
    this.bytes = JarUtils.loadNonClassEntries(input);
  }
}
