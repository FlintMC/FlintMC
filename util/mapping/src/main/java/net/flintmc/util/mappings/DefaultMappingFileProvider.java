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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class DefaultMappingFileProvider implements MappingFileProvider {

  private final String[] mappingPath;

  @Inject
  private DefaultMappingFileProvider(@Named("mappingPath") String[] mappingPath) {
    this.mappingPath = mappingPath;
  }

  @Override
  public Map<MappingType, InputStream> getMappings(String version) throws IOException {

    File methodsFile = null;
    File fieldsFile = null;
    File joinedFile = null;

    for (String path : this.mappingPath) {
      File file = new File(path, "methods.csv");
      if(file.exists()){
        methodsFile = file;
      }
      file = new File(path, "fields.csv");
      if(file.exists()){
        fieldsFile = file;
      }
       file = new File(path, "joined.tsrg");
      if(file.exists()){
        joinedFile = file;
      }
    }

    if (methodsFile == null || fieldsFile == null || joinedFile == null) {
      return Collections.emptyMap();
    }

    Map<MappingType, InputStream> streams = new HashMap<>();

    streams.put(MappingType.METHODS, new FileInputStream(methodsFile));
    streams.put(MappingType.FIELDS, new FileInputStream(fieldsFile));
    streams.put(MappingType.JOINED, new FileInputStream(joinedFile));

    return streams;
  }
}
