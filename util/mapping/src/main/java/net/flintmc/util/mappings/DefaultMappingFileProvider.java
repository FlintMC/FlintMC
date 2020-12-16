package net.flintmc.util.mappings;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import org.apache.commons.io.IOUtils;

@Singleton
public class DefaultMappingFileProvider implements MappingFileProvider {

  private final File flintRoot;
  private final McpMappingIndexProvider mcpMappingIndexProvider;

  @Inject
  private DefaultMappingFileProvider(
      @Named("flintRoot") File flintRoot, McpMappingIndexProvider mcpMappingIndexProvider) {
    this.flintRoot = flintRoot;
    this.mcpMappingIndexProvider = mcpMappingIndexProvider;
  }

  @Override
  public Map<String, InputStream> getMappings(String version) throws IOException {
    File assetRoot = new File(flintRoot, "assets/" + version);
    File methodsFile = new File(assetRoot, "methods.csv").getAbsoluteFile();
    File fieldsFile = new File(assetRoot, "fields.csv").getAbsoluteFile();
    File joinedFile = new File(assetRoot, "joined.tsrg").getAbsoluteFile();

    try {

      if (!methodsFile.exists() || !fieldsFile.exists() || !joinedFile.exists()) {
        assetRoot.mkdirs();
        Version.Mapping index = this.mcpMappingIndexProvider.fetch().get(version).getModCoderPack();

        Files.write(
            methodsFile.toPath(),
            IOUtils.toByteArray(new URL("jar:" + index.getMappingsDownload() + "!/methods.csv")));
        Files.write(
            fieldsFile.toPath(),
            IOUtils.toByteArray(new URL("jar:" + index.getMappingsDownload() + "!/fields.csv")));
        Files.write(
            joinedFile.toPath(),
            IOUtils.toByteArray(
                new URL("jar:" + index.getConfigDownload() + "!/config/joined.tsrg")));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return ImmutableMap.of(
        "methods.csv",
        new FileInputStream(methodsFile),
        "fields.csv",
        new FileInputStream(fieldsFile),
        "joined.tsrg",
        new FileInputStream(joinedFile));
  }
}
