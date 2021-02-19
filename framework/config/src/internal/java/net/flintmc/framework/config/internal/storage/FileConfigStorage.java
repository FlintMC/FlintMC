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

package net.flintmc.framework.config.internal.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.storage.ConfigStorage;
import net.flintmc.framework.config.storage.StoragePriority;
import net.flintmc.framework.config.storage.serializer.JsonConfigSerializer;
import net.flintmc.framework.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;

/**
 * The default file storage in the flint/configs directory.
 */
@Singleton
@StoragePriority
public class FileConfigStorage implements ConfigStorage {

  private static final String NAME = "local"; // unique name of this storage, shouldn't be changed

  private final Logger logger;
  private final File directory;
  private final Gson gson;
  private final JsonConfigSerializer serializer;

  private final Predicate<ConfigObjectReference> filter;

  @Inject
  public FileConfigStorage(
      @InjectLogger Logger logger,
      @Named("flintRoot") File rootDirectory,
      JsonConfigSerializer serializer) {
    this.logger = logger;
    this.directory = new File(rootDirectory, "configs");
    this.directory.mkdirs();
    this.gson = new GsonBuilder().setPrettyPrinting().create();
    this.serializer = serializer;

    this.filter = reference -> reference.appliesTo(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(ParsedConfig config) {
    JsonObject object = new JsonObject();
    this.serializer.serialize(config, object, this.filter);

    File file = this.getFile(config);

    if (object.size() == 0) {
      if (file.exists() && !file.delete()) {
        this.logger.trace("Failed to delete file " + file.getAbsolutePath());
      }
      return;
    }

    try (OutputStream outputStream = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
      this.gson.toJson(object, writer);
    } catch (IOException e) {
      this.logger.error("Failed to write a config to " + file, e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void read(ParsedConfig config) {
    File file = this.getFile(config);
    if (!file.exists()) {
      return;
    }

    JsonObject object;
    try (InputStream inputStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      JsonElement element = JsonParser.parseReader(reader);
      if (!element.isJsonObject()) {
        return;
      }

      object = element.getAsJsonObject();
    } catch (IOException e) {
      this.logger.error("Failed to read the config from " + file, e);
      return;
    }

    this.serializer.deserialize(object, config, this.filter);
  }

  private File getFile(ParsedConfig config) {
    return new File(this.directory, config.getConfigName() + ".json");
  }
}
