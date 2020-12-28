/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
