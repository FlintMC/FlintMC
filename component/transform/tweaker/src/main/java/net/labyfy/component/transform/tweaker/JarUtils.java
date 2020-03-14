package net.labyfy.component.transform.tweaker;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarUtils {
  /**
   * Creates a map of <String(Class name), ClassNode> for a given jar file
   *
   * @param jarFile
   * @author Konloch (Bytecode Viewer)
   * @return
   * @throws IOException
   */
  public static Map<String, ClassNode> loadClasses(File jarFile) throws IOException {
    Map<String, ClassNode> classes = new HashMap<String, ClassNode>();
    JarFile jar = new JarFile(jarFile);
    Stream<JarEntry> str = jar.stream();
    // For some reason streaming = entries in messy jars
    // enumeration = no entries
    // Or if the jar is really big, enumeration = infinite hang
    // ...
    // Whatever. It works now!
    str.forEach(z -> readJar(jar, z, classes, null));
    jar.close();
    return classes;
  }

  public static Map<String, ClassNode> loadRT() throws IOException {
    Map<String, ClassNode> classes = new HashMap<String, ClassNode>();
    JarFile jar = new JarFile(getRT());
    Stream<JarEntry> str = jar.stream();
    // TODO: Make ignoring these packages optional
    str.forEach(
        z -> readJar(jar, z, classes, Arrays.asList("com/sun/", "com/oracle/", "jdk/", "sun/")));
    jar.close();
    return classes;
  }

  /**
   * This method is less fussy about the jar integrity.
   *
   * @param jar
   * @param en
   * @param classes
   * @return
   */
  private static Map<String, ClassNode> readJar(
      JarFile jar, JarEntry en, Map<String, ClassNode> classes, List<String> ignored) {
    String name = en.getName();
    try (InputStream jis = jar.getInputStream(en)) {
      if (name.endsWith(".class")) {
        if (ignored != null) {
          for (String s : ignored) {
            if (name.startsWith(s)) {
              return classes;
            }
          }
        }
        byte[] bytes = IOUtils.toByteArray(jis);
        String cafebabe = String.format("%02X%02X%02X%02X", bytes[0], bytes[1], bytes[2], bytes[3]);
        if (cafebabe.toLowerCase().equals("cafebabe")) {
          try {
            final ClassNode cn = ASMUtils.getNode(bytes);
            if (cn != null && (cn.name.equals("java/lang/Object") ? true : cn.superName != null)) {
              classes.put(cn.name, cn);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return classes;
  }

  /**
   * Creates a map of <String(entry name), byte[]> for a given jar file
   *
   * @param jarFile
   * @return
   * @throws IOException
   */
  public static Map<String, byte[]> loadNonClassEntries(File jarFile) throws IOException {
    Map<String, byte[]> entries = new HashMap<String, byte[]>();
    ZipInputStream jis = new ZipInputStream(new FileInputStream(jarFile));
    ZipEntry entry;
    while ((entry = jis.getNextEntry()) != null) {
      try {
        final String name = entry.getName();
        if (!name.endsWith(".class") && !entry.isDirectory()) {
          byte[] bytes = IOUtils.toByteArray(jis);
          entries.put(name, bytes);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        jis.closeEntry();
      }
    }
    jis.close();
    return entries;
  }

  /**
   * Saves a map of bytes to a jar file
   *
   * @param outBytes
   * @param fileName
   */
  public static void saveAsJar(Map<String, byte[]> outBytes, String fileName) {
    try {
      new File(fileName).delete();
      JarOutputStream out = new JarOutputStream(new FileOutputStream(fileName));
      for (String entry : outBytes.keySet()) {
        out.putNextEntry(new ZipEntry(entry));
        if (!entry.endsWith("/")) {
          out.write(outBytes.get(entry));
        }
        out.closeEntry();
      }

      /* FileWriter fileWriter = new FileWriter("./MANIFEST.MF");

            Collection<String> manifest = new LinkedList<>();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            outBytes.remove("META-INF/MANIFEST.MF");

            manifest.add("Manifest-Version: 1.0");
            manifest.add("Main-Class: net.minecraft.client.Main");
            manifest.add("");

            outBytes.forEach(
                (name, bytes) -> {
                  manifest.add("Name: " + name);
                  manifest.add(
                      "SHA-256-Digest: " + Base64.getEncoder().encodeToString(digest.digest(bytes)));
                  manifest.add("");
                });

            manifest.forEach(
                line -> {
                  try {
                    fileWriter.write(line + System.lineSeparator());
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                });

            fileWriter.flush();
            fileWriter.close();
            add(new File("./MANIFEST.MF"), "META-INF/MANIFEST.MF", out);
      */
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void add(File source, String path, JarOutputStream target) throws IOException {
    BufferedInputStream in = null;
    try {
      JarEntry entry = new JarEntry(path.replace("\\", "/"));
      entry.setTime(source.lastModified());
      target.putNextEntry(entry);
      in = new BufferedInputStream(new FileInputStream(source));

      byte[] buffer = new byte[1024];
      while (true) {
        int count = in.read(buffer);
        if (count == -1) break;
        target.write(buffer, 0, count);
      }
      target.closeEntry();
    } finally {
      if (in != null) in.close();
    }
  }

  @SuppressWarnings("resource")
  public static String getManifestMainClass(File jar) {
    try {
      return new JarFile(jar.getAbsolutePath())
          .getManifest()
          .getMainAttributes()
          .getValue("Main-class")
          .replace(".", "/");
    } catch (Exception e) {
    }
    return null;
  }

  public static File getRT() {
    return new File(
        System.getProperty("java.home") + File.separator + "lib" + File.separator + "rt.jar");
  }
}
