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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Singleton
public class FileCache {

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
    this.objects = new HashMap<>();

    this.readFile();

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
        1,
        1,
        TimeUnit.SECONDS);
  }

  public <T> void registerIO(Class<T> targetClass, CachedObjectIO<T> factory) {
    this.io.put(targetClass.getSimpleName(), factory);
  }

  public <T> CompletableFuture<T> get(
      Predicate<T> tester,
      Class<T> type,
      Supplier<UUIDMappedValue<T>> optionalSupplier,
      long validMillis) {
    for (Map<Class<?>, CachedObject<?>> objects : this.objects.values()) {
      for (CachedObject<?> object : objects.values()) {
        if (object.getType().equals(type)) {
          T t = (T) object.getValue();
          if (tester.test(t)) {
            return CompletableFuture.completedFuture(t);
          }
        }
      }
    }

    CachedObjectIO<T> io = this.getIO(type);
    return CompletableFuture.supplyAsync(
        () -> {
          UUIDMappedValue<T> value = optionalSupplier.get();
          if (value == null) {
            return null;
          }

          T t = value.getValue();

          Map<Class<?>, CachedObject<?>> objects =
              this.objects.computeIfAbsent(value.getUniqueId(), uuid -> new HashMap<>());
          objects.put(
              type, new CachedObject<>(type, t, io, System.currentTimeMillis() + validMillis));

          return t;
        });
  }

  @SuppressWarnings("unchecked")
  public <T> CompletableFuture<T> get(
      UUID uniqueId, Class<T> type, Supplier<T> optionalSupplier, long validMillis) {
    Map<Class<?>, CachedObject<?>> objects =
        this.objects.computeIfAbsent(uniqueId, uuid -> new HashMap<>());

    if (!objects.containsKey(type)) {
      CachedObjectIO<T> io = this.getIO(type);
      return CompletableFuture.supplyAsync(
          () -> {
            T t = optionalSupplier.get();
            objects.put(
                type, new CachedObject<>(type, t, io, System.currentTimeMillis() + validMillis));
            return t;
          });
    }

    return CompletableFuture.completedFuture((T) objects.get(type));
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

      objects.removeIf(object -> !object.isValid());

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
        output.writeUTF(object.getType().getSimpleName());
        output.writeLong(object.getValidUntil());

        CachedObjectIO io = object.getIO();

        Object value = object.getValue();
        output.writeBoolean(value != null);
        if (value != null) {
          io.write(value, output);
        }
      }
    }
  }

  public void read(DataInput input) throws IOException {
    int size = input.readShort();
    Map<UUID, Map<Class<?>, CachedObject<?>>> target = new HashMap<>(size);

    for (int i = 0; i < size; i++) {
      UUID uniqueId = new UUID(input.readLong(), input.readLong());

      int entrySize = input.readShort();
      Map<Class<?>, CachedObject<?>> objects = new HashMap<>(entrySize);

      for (int j = 0; j < entrySize; j++) {
        String objectName = input.readUTF();
        CachedObjectIO<?> io = this.io.get(objectName);
        if (io == null) {
          this.logger.trace(
              "Factory for object {} not found while reading for UUID {}", objectName, uniqueId);
          continue;
        }

        long validUntil = input.readLong();

        Object value = input.readBoolean() ? io.read(input) : null;
        objects.put(io.getType(), new CachedObject(io.getType(), value, io, validUntil));
      }

      target.put(uniqueId, objects);
    }

    this.objects.clear();
    this.objects.putAll(target);
  }
}
