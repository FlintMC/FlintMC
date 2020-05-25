package net.labyfy.component.annotation.processing.util;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceFile {
  public static Set<String> read(InputStream stream) throws IOException {
    Set<String> services = new HashSet<>();
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      String line;
      while ((line = reader.readLine()) != null) {
        services.add(line);
      }
    }

    return services;
  }

  public static void write(Set<String> services, OutputStream stream) throws IOException {
    try(OutputStreamWriter writer = new OutputStreamWriter(stream)) {
      for(String service : services) {
        writer.write(service);
      }
    }
  }
}
