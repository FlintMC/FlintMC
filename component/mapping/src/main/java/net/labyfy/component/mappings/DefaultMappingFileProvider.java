package net.labyfy.component.mappings;

import com.google.common.collect.ImmutableMap;
import com.google.inject.name.Named;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.processing.autoload.AutoLoad;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.DEFAULT_MAPPING_FILE_PROVIDER_PRIORITY;
import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.DEFAULT_MAPPING_FILE_PROVIDER_ROUND;

@Singleton
@AutoLoad(round = DEFAULT_MAPPING_FILE_PROVIDER_PRIORITY, priority = DEFAULT_MAPPING_FILE_PROVIDER_ROUND)
@Implement(MappingFileProvider.class)
public class DefaultMappingFileProvider implements MappingFileProvider {

  private final File labyfyRoot;
  private final McpMappingIndexProvider mcpMappingIndexProvider;

  @Inject
  private DefaultMappingFileProvider(@Named("labyfyRoot") File labyfyRoot, McpMappingIndexProvider mcpMappingIndexProvider) {
    this.labyfyRoot = labyfyRoot;
    this.mcpMappingIndexProvider = mcpMappingIndexProvider;
  }

  @Override
  public Map<String, InputStream> getMappings(String version) throws IOException {
    File assetRoot = new File(labyfyRoot, "assets/" + version);
    File methodsFile = new File(assetRoot, "methods.csv").getAbsoluteFile();
    File fieldsFile = new File(assetRoot, "fields.csv").getAbsoluteFile();
    File joinedFile = new File(assetRoot, "joined.tsrg").getAbsoluteFile();

    if (!methodsFile.exists() || !fieldsFile.exists() || !joinedFile.exists()) {
      assetRoot.mkdirs();
      Version.Mapping index = this.mcpMappingIndexProvider.fetch().get(version).getModCoderPack();

      Files.write(methodsFile.toPath(), IOUtils.toByteArray(new URL("jar:" + index.getMappingsDownload() + "!/methods.csv")));
      Files.write(fieldsFile.toPath(), IOUtils.toByteArray(new URL("jar:" + index.getMappingsDownload() + "!/fields.csv")));
      Files.write(joinedFile.toPath(), IOUtils.toByteArray(new URL("jar:" + index.getConfigDownload() + "!/config/joined.tsrg")));
    }

    return
        ImmutableMap.of(
            "methods.csv",
            new FileInputStream(methodsFile),
            "fields.csv",
            new FileInputStream(fieldsFile),
            "joined.tsrg",
            new FileInputStream(joinedFile));
  }
}
