package net.flintmc.util.mojang.internal.cache;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.util.mojang.internal.cache.object.CachedObject;
import net.flintmc.util.mojang.internal.cache.object.CachedObjectIO;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@Singleton
public class FileCache {

  private static final int MAX_SIZE = 1000;

  private final Logger logger;
  private final Path path;
  private final Map<String, CachedObjectIO<?>> io;
  private final Map<UUID, Map<Class<?>, CachedObject<?>>> objects;

  @Inject
  private FileCache(
      @InjectLogger Logger logger,
      @Named("flintRoot") File rootDirectory,
      ScheduledExecutorService scheduledService)
      throws IOException {
    this.logger = logger;

    Path path = rootDirectory.toPath();
    Files.createDirectories(path);
    this.path = path.resolve("mojangCache.bin");

    this.io = new HashMap<>();
    this.objects = new ConcurrentHashMap<>();

    this.init(scheduledService);
  }

  private void init(ScheduledExecutorService scheduledService) throws IOException {
    try {
      this.readFile();
    } catch (Exception exception) {
      logger.trace("Failed to read mojangCache.bin", exception);
      Files.delete(this.path);
    }

    scheduledService.scheduleAtFixedRate(
        () -> {
          this.invalidate();

          if (this.objects.isEmpty()) {
            return;
          }

          try {
            this.writeFile();
          } catch (IOException exception) {
            this.logger.trace("Failed to write the file cache to " + this.path, exception);
          }
        },
        5,
        5,
        TimeUnit.SECONDS);
  }

  public <T> void registerIO(Class<T> targetClass, CachedObjectIO<T> factory) {
    this.io.put(targetClass.getSimpleName(), factory);
  }

  @SuppressWarnings("unchecked")
  public <T> T getCached(Class<T> type, UUID uniqueId) {
    if (!this.objects.containsKey(uniqueId)) {
      return null;
    }
    CachedObject<?> object = this.objects.get(uniqueId).get(type);
    return object != null ? (T) object.getValue() : null;
  }

  @SuppressWarnings("unchecked")
  public <T> T getCached(Class<T> type, Predicate<T> tester) {
    for (Map<Class<?>, CachedObject<?>> objects : this.objects.values()) {
      for (CachedObject<?> object : objects.values()) {
        if (object.getValue() != null && object.getType().equals(type)) {
          T t = (T) object.getValue();
          if (tester.test(t)) {
            return t;
          }
        }
      }
    }

    return null;
  }

  public <T> void cache(UUID uniqueId, Class<T> type, T t, long validMillis) {
    Map<Class<?>, CachedObject<?>> objects =
        this.objects.computeIfAbsent(uniqueId, uuid -> new ConcurrentHashMap<>());

    CachedObject<T> object =
        new CachedObject<>(type, t, this.getIO(type), System.currentTimeMillis() + validMillis);
    objects.put(type, object);
  }

  @SuppressWarnings("unchecked")
  private <T> CachedObjectIO<T> getIO(Class<T> type) {
    CachedObjectIO<T> io = (CachedObjectIO<T>) this.io.get(type.getSimpleName());
    if (io == null) {
      throw new IllegalArgumentException("Cannot find registered IO for " + type.getName());
    }
    return io;
  }

  private void invalidate() {
    if (this.objects.isEmpty()) {
      return;
    }

    Collection<Map<Class<?>, CachedObject<?>>> types = this.objects.values();
    for (Map<Class<?>, CachedObject<?>> value : types) {
      Collection<CachedObject<?>> objects = value.values();

      objects.removeIf(object -> objects.size() > MAX_SIZE || !object.isValid());

      if (objects.isEmpty()) {
        types.remove(value);
      }
    }
  }

  private void writeFile() throws IOException {
    try (OutputStream outputStream = Files.newOutputStream(this.path);
        DataOutputStream output = new DataOutputStream(outputStream)) {
      this.write(output);
    }
  }

  private void readFile() throws IOException {
    if (Files.notExists(this.path)) {
      return;
    }

    try (InputStream inputStream = Files.newInputStream(this.path);
        DataInputStream input = new DataInputStream(inputStream)) {
      this.read(input);
    }
  }

  public void write(DataOutput output) throws IOException {
    Map<UUID, Map<Class<?>, CachedObject<?>>> copy = ImmutableMap.copyOf(this.objects);
    output.writeShort(copy.size());

    for (Map.Entry<UUID, Map<Class<?>, CachedObject<?>>> entry : copy.entrySet()) {
      UUID uniqueId = entry.getKey();
      output.writeLong(uniqueId.getMostSignificantBits());
      output.writeLong(uniqueId.getLeastSignificantBits());

      output.writeShort(entry.getValue().size());
      for (CachedObject<?> object : entry.getValue().values()) {
        DataStreamHelper.writeString(output, object.getType().getSimpleName());
        output.writeLong(object.getValidUntil());

        CachedObjectIO io = object.getIO();

        Object value = object.getValue();
        output.writeBoolean(value != null);
        if (value != null) {
          io.write(uniqueId, value, output);
        }
      }
    }
  }

  public void read(DataInput input) throws IOException {
    int size = input.readShort();
    if (size < 0) {
      return;
    }
    Map<UUID, Map<Class<?>, CachedObject<?>>> target = new HashMap<>(size);

    for (int i = 0; i < size; i++) {
      UUID uniqueId = new UUID(input.readLong(), input.readLong());

      int entrySize = input.readShort();
      if (entrySize < 0) {
        continue;
      }

      Map<Class<?>, CachedObject<?>> objects = new ConcurrentHashMap<>(entrySize);

      for (int j = 0; j < entrySize; j++) {
        String objectName = DataStreamHelper.readString(input);
        CachedObjectIO<?> io = this.io.get(objectName);
        if (io == null) {
          this.logger.trace(
              "Factory for object {} not found while reading for UUID {}", objectName, uniqueId);
          continue;
        }

        long validUntil = input.readLong();

        Object value = input.readBoolean() ? io.read(uniqueId, input) : null;
        objects.put(io.getType(), new CachedObject(io.getType(), value, io, validUntil));
      }

      target.put(uniqueId, objects);
    }

    this.objects.clear();
    this.objects.putAll(target);
  }
}
