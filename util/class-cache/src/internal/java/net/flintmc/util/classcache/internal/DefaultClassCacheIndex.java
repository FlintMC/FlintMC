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
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.util.classcache.CachedClass;
import net.flintmc.util.classcache.ClassCacheIndex;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

@Singleton
@Implement(ClassCacheIndex.class)
public class DefaultClassCacheIndex implements ClassCacheIndex {

  private final Gson gson;
  private final Logger logger;
  private final CachedClass.Factory cachedClassFactory;

  private IndexModel data;

  @Inject
  private DefaultClassCacheIndex(@InjectLogger Logger logger,
      CachedClass.Factory cachedClassFactory) {
    this.gson = new Gson();
    this.logger = logger;
    this.cachedClassFactory = cachedClassFactory;
    try {
      File indexFile = new File(INDEX_FILE_PATH);
      //noinspection ResultOfMethodCallIgnored
      indexFile.getParentFile().mkdirs();
      this.data = this.gson
          .fromJson(new FileReader(indexFile), IndexModel.class);
      this.data.elements.forEach((name, element) -> {
        element.setName(name);
        element.cachedClasses = new HashMap<>();
      });
    } catch (FileNotFoundException e) {
      this.logger
          .info("Failed to read class cache index. Creating a new index.");
      this.data = new IndexModel();
      this.write();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getLatestId(String name) {
    return this.data.getOrInsert(name).getLatest();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CachedClass getCachedClass(String name, long id) {
    return this.data.getOrInsert(name)
        .getCachedClass(id, this.cachedClassFactory::create);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write() {
    try (FileWriter out = new FileWriter(INDEX_FILE_PATH)) {
      out.write(this.gson.toJson(this.data));
      out.flush();
    } catch (IOException e) {
      this.logger.error("Failed to write class cache index.", e);
    }
  }

  private static class IndexModel {

    private final Map<String, IndexElement> elements;

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

  private static class IndexElement {

    private long latest;
    private final Map<Long, String> uuids;

    private transient String name;
    transient Map<Long, CachedClass> cachedClasses;

    IndexElement(String name) {
      this.uuids = new HashMap<>();
      this.name = name;
      this.cachedClasses = new HashMap<>();
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

    CachedClass getCachedClass(long id,
        BiFunction<String, UUID, CachedClass> factory) {
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

        CachedClass cachedClass = factory.apply(this.name, uuid);
        this.cachedClasses.put(id, cachedClass);
        return cachedClass;
      }

    }

  }

}
