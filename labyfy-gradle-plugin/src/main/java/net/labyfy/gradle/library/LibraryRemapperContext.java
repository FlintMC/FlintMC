package net.labyfy.gradle.library;

import com.google.common.collect.Maps;
import net.labyfy.gradle.util.JarUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LibraryRemapperContext {

  private final LibraryRemapNameProvider libraryRemapNameProvider;
  private final LibraryRemapper remapper;
  private final File file;
  private final Map<String, ClassNode> classes;
  private final Map<String, byte[]> bytes;

  protected LibraryRemapperContext(LibraryRemapNameProvider libraryRemapNameProvider, LibraryRemapper remapper, File file) throws IOException {
    this.libraryRemapNameProvider = libraryRemapNameProvider;
    this.remapper = remapper;
    this.file = file;
    this.classes = Maps.newHashMap();
    this.bytes = Maps.newHashMap();

    this.loadJar();
    this.applyClassMappings();
    this.clearManifest();
    this.addMarkerFile();
    this.saveJar();
  }

  private void addMarkerFile() {
    this.bytes.put(".deobfuscated", new byte[]{});
  }

  private void loadJar() throws IOException {
    this.classes.putAll(JarUtils.loadClasses(file));
    this.bytes.putAll(JarUtils.loadNonClassEntries(file));
  }

  private void saveJar() {
    JarUtils.saveAsJar(this.bytes, this.file);
  }

  private void applyClassMappings() {
    this.classes.forEach(
        (name, classNode) -> {
          try {
            if (this.remapper.getMappings().containsKey(classNode.name)) {
              System.out.println(
                  " -> rewrite "
                      + classNode.name
                      + " to "
                      + this.remapper.getMappings().get(classNode.name).replace('/', '.'));
            }
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassVisitor classRemapper = new ClassRemapper(classWriter, this.libraryRemapNameProvider);
            classNode.accept(classRemapper);

            this.bytes.put(
                (this.remapper.getMappings().containsKey(classNode.name)
                    ? this.remapper.getMappings().get(classNode.name)
                    : classNode.name)
                    + ".class",
                classWriter.toByteArray());
          } catch (Throwable ignored) {
            if(classNode.name.equals("dbl")){
              ignored.printStackTrace();
            }
            System.out.println(" -> cannot rewrite " + classNode.name);
          }
        });
  }

  private void clearManifest() {
    this.bytes.remove("META-INF/MANIFEST.MF");
    this.bytes.remove("META-INF/MOJANGCS.RSA");
    this.bytes.remove("META-INF/MOJANGCS.SF");
  }


}
