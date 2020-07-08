package net.labyfy.component.mappings;

import com.google.common.collect.ImmutableMap;
import com.google.inject.name.Named;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.inject.implement.Implement;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.util.Map;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.*;

@Singleton
@AutoLoad(round = DEFAULT_MAPPING_FILE_PROVIDER_PRIORITY, priority = DEFAULT_MAPPING_FILE_PROVIDER_ROUND)
@Implement(MappingFileProvider.class)
public class DefaultMappingFileProvider implements MappingFileProvider {

  private final File labyfyRoot;

  @Inject
  private DefaultMappingFileProvider(@Named("labyfyRoot") File labyfyRoot) {
    this.labyfyRoot = labyfyRoot;
  }

  @Override
  public Map<String, InputStream> getMappings(String version) throws IOException {
    return
        ImmutableMap.of(
            "methods.csv",
            new FileInputStream(
                new File(labyfyRoot, "assets/" + version + "/methods.csv").getAbsoluteFile()),
            "fields.csv",
            new FileInputStream(
                new File(labyfyRoot, "assets/" + version + "/fields.csv").getAbsoluteFile()),
            "joined.tsrg",
            new FileInputStream(
                new File(labyfyRoot, "assets/" + version + "/joined.tsrg").getAbsoluteFile()));
  }
}
