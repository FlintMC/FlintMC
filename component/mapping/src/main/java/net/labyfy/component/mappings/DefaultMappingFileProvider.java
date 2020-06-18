package net.labyfy.component.mappings;

import com.google.common.collect.ImmutableMap;
import com.google.inject.name.Named;
import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.component.inject.implement.Implement;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import static net.labyfy.base.structure.AutoLoadPriorityConstants.*;

@Singleton
@AutoLoad(round = DEFAULT_MAPPING_FILE_PROVIDER_PRIORITY, priority = DEFAULT_MAPPING_FILE_PROVIDER_ROUND)
@Implement(MappingFileProvider.class)
public class DefaultMappingFileProvider implements MappingFileProvider {

  private final File labyfyRoot;

  @Inject
  private DefaultMappingFileProvider(@Named("labyfyRoot") File labyfyRoot) {
    this.labyfyRoot = labyfyRoot;
  }

  public Map<String, InputStream> getMappings(String version) {
    try {
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
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }
}
