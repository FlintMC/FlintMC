package net.labyfy.internal.component.config.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.config.storage.ConfigStorage;
import net.labyfy.component.config.storage.LoadStorage;
import net.labyfy.component.config.storage.serializer.JsonConfigSerializer;
import net.labyfy.component.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

@Singleton
@LoadStorage
public class FileConfigStorage implements ConfigStorage {

  private static final String NAME = "local";

  private final Logger logger;
  private final File directory;
  private final Gson gson;
  private final JsonConfigSerializer serializer;

  private final Predicate<ConfigObjectReference> filter;

  @Inject
  public FileConfigStorage(@InjectLogger Logger logger, @Named("labyfyRoot") File rootDirectory, JsonConfigSerializer serializer) {
    this.logger = logger;
    this.directory = new File(rootDirectory, "configs");
    this.directory.mkdirs();
    this.gson = new GsonBuilder().setPrettyPrinting().create();
    this.serializer = serializer;

    this.filter = reference -> reference.appliesTo(this);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void write(ParsedConfig config) {
    JsonObject object = new JsonObject();
    this.serializer.serialize(config, object, this.filter);

    File file = this.getFile(config);

    try (OutputStream outputStream = new FileOutputStream(file);
         Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
      this.gson.toJson(object, writer);
    } catch (IOException e) {
      this.logger.error("Failed to write a config to " + file, e);
    }
  }

  @Override
  public void read(ParsedConfig config) {
    File file = this.getFile(config);
    if (!file.exists()) {
      return;
    }

    JsonObject object;
    try (InputStream inputStream = new FileInputStream(file);
         Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      object = JsonParser.parseReader(reader).getAsJsonObject();
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
