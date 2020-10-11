package net.labyfy.internal.webgui.ultralight.filesystem;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.labyfy.component.commons.util.Pair;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.webgui.WebFileSystemHandler;
import net.labyfy.webgui.WebFileSystemService;
import net.labyfy.webgui.WebResource;
import net.labymedia.ultralight.plugin.filesystem.UltralightFileSystem;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Implements the ultralight file systems and proxies the calls to web filesystem implementations
 * accordingly
 */
@Singleton
public class UltralightFileSystemBridge implements UltralightFileSystem {

  private final Map<String, WebFileSystemHandler> fileSystems;
  private final Map<Long, String> openedResources;
  private final LoadingCache<String, WebResource> resources;

  private final Logger logger;

  private long nextResourceHandle;

  @Inject
  private UltralightFileSystemBridge(
      WebFileSystemService fileSystemService, @InjectLogger Logger logger) {
    this.fileSystems =
        fileSystemService.getFileSystems().stream()
            .collect(Collectors.toMap(Pair::second, Pair::first));
    this.openedResources = new HashMap<>();

    this.resources =
        CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(
                new CacheLoader<String, WebResource>() {
                  @SuppressWarnings("NullableProblems")
                  @Override
                  public WebResource load(String path) throws Exception {
                    Pair<String, String> info = splitPath(path);
                    if (info == null || !fileSystems.containsKey(info.first())) {
                      throw new IOException("Invalid path: " + path);
                    }
                    return fileSystems.get(info.first()).getFile(info.second());
                  }
                });

    this.nextResourceHandle = 0;
    this.logger = logger;
  }

  @Override
  public boolean fileExists(String path) {
    Pair<String, String> info = splitPath(path);
    if (info == null || !fileSystems.containsKey(info.first())) {
      return false;
    }

    return fileSystems.get(info.first()).existsFile(info.second());
  }

  @Override
  public long getFileSize(long handle) {
    if (!openedResources.containsKey(handle)) {
      return -1;
    }

    try {
      return resources.get(openedResources.get(handle)).getSize();
    } catch (IOException | ExecutionException e) {
      logger.error("Exception in file system:", e);
      return -1;
    }
  }

  @Override
  public String getFileMimeType(String path) {
    try {
      return resources.get(path).getMimeType();
    } catch (ExecutionException e) {
      logger.error("Exception in file system:", e);
      return "unknown";
    }
  }

  @Override
  public long openFile(String path, boolean openForWriting) {
    long handle = nextResourceHandle++;
    if (nextResourceHandle < 0) {
      nextResourceHandle = 0;
    }

    try {
      resources.get(path).open();
      openedResources.put(handle, path);
      return handle;
    } catch (IOException | ExecutionException e) {
      logger.error("Exception in file system:", e);
      return INVALID_FILE_HANDLE;
    }
  }

  @Override
  public void closeFile(long handle) {
    if (!openedResources.containsKey(handle)) {
      return;
    }

    try {
      resources.get(openedResources.get(handle)).close();
    } catch (IOException | ExecutionException e) {
      logger.error("Exception in file system:", e);
    }
  }

  @Override
  public long readFromFile(long handle, ByteBuffer data, long length) {
    if (!openedResources.containsKey(handle)) {
      return -1;
    }

    try {
      return resources.get(openedResources.get(handle)).readFromFile(data, length);
    } catch (IOException | ExecutionException e) {
      logger.error("Exception in file system:", e);
      return -1;
    }
  }

  private Pair<String, String> splitPath(String fullPath) {
    if (!fullPath.startsWith("$")) {
      return new Pair<>(
          "class-resource", fullPath.startsWith("/") ? fullPath.replaceFirst("/", "") : fullPath);
    }

    int indexOfSlash = fullPath.indexOf('/');
    if (indexOfSlash < 2) {
      return null;
    }

    String protocol = fullPath.substring(1, indexOfSlash);
    String path = fullPath.substring(indexOfSlash + 1);

    return new Pair<>(protocol, path);
  }
}
