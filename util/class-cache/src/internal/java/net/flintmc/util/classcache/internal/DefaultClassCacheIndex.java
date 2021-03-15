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

package net.flintmc.util.classcache.internal;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.util.classcache.CachedClass;
import net.flintmc.util.classcache.ClassCacheIndex;
import org.apache.logging.log4j.Logger;

@Singleton
@Implement(ClassCacheIndex.class)
public class DefaultClassCacheIndex implements ClassCacheIndex {

  private final Gson gson;
  private final Logger logger;
  private final CachedClass.Factory cachedClassFactory;

  private IndexModel data;

  @Inject
  private DefaultClassCacheIndex(@InjectLogger Logger logger,
      CachedClass.Factory cachedClassFactory)
      throws FileNotFoundException {
    this.gson = new Gson();
    this.logger = logger;
    this.cachedClassFactory = cachedClassFactory;
    try {
      File indexFile = new File(INDEX_FILE_PATH);
      indexFile.getParentFile().mkdirs();
      this.data = this.gson
          .fromJson(new FileReader(indexFile), IndexModel.class);
      this.data.elements.forEach((name, element) -> element.setName(name));
    } catch (FileNotFoundException e) {
      this.logger.error("Failed to read class cache index.", e);
      this.data = new IndexModel();
    }
  }

  @Override
  public long getLatestId(String name) {
    return this.data.getOrInsert(name).getLatest();
  }

  @Override
  public CachedClass getCachedClass(String name, long id) {
    return this.data.getOrInsert(name).getCachedClass(id);
  }

  @Override
  public void write() {
    try (FileWriter out = new FileWriter(INDEX_FILE_PATH)) {
      out.write(this.gson.toJson(this.data));
      out.flush();
    } catch (IOException e) {
      this.logger.error("Failed to write class cache index.", e);
    }
  }

  private class IndexModel {

    private Map<String, IndexElement> elements;

    IndexModel() {
      this.elements = new HashMap<>();
    }

    IndexElement getOrInsert(String name) {
      if (this.elements.containsKey(name)) {
        return this.elements.get(name);
      } else {
        IndexElement element = new IndexElement(name);
        this.elements.put(name, element);
        return element;
      }
    }


  }

  private class IndexElement {

    private long latest;
    private Map<Long, String> uuids;

    private transient String name;
    private transient Map<Long, CachedClass> cachedClasses;

    IndexElement(String name) {
      this.uuids = new HashMap<>();
      this.cachedClasses = new HashMap<>();
      this.name = name;
    }

    void setName(String name) {
      this.name = name;
    }

    void setLatest(long id, String uuid) {
      this.latest = id;
      this.set(id, uuid);
    }

    void set(long id, String uuid) {
      this.uuids.put(id, uuid);
    }

    long getLatest() {
      return this.latest;
    }

    CachedClass getCachedClass(long id) {
      if (this.cachedClasses.containsKey(id)) {
        return this.cachedClasses.get(id);
      } else {
        UUID uuid;
        if (this.uuids.containsKey(id)) {
          uuid = UUID.fromString(uuids.get(id));
        } else {
          uuid = UUID.randomUUID();
          this.setLatest(id, uuid.toString());
        }

        CachedClass cachedClass = cachedClassFactory.create(this.name, uuid);
        this.cachedClasses.put(id, cachedClass);
        return cachedClass;
      }

    }

  }

}
