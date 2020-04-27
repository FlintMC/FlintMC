package net.labyfy.component.packages.impl.source;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileSource implements PackageSource {
  private final File file;
  private final JarFile jar;

  FileSource(File file) {
    try {
      this.file = file;
      this.jar = new JarFile(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws IOException {
    jar.close();
  }

  @Override
  public URL findResource(String path) {
    JarEntry entry = jar.getJarEntry(path);
    if(entry == null) {
      return null;
    }

    try {
      URI uri = new URI(
          "jar:file",
          file.getAbsolutePath() + "!",
          "/" + path,
          null
      );

      return uri.toURL();
    } catch (URISyntaxException | MalformedURLException e) {
      throw new IllegalArgumentException("Path is invalid", e);
    }
  }
}
