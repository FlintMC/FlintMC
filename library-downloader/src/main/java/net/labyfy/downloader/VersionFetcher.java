package net.labyfy.downloader;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class VersionFetcher {

  private static final String VERSION_MANIFEST =
      "https://launchermeta.mojang.com/mc/game/version_manifest.json";

  public static VersionManifest.Entry fetch(String version) {
    return fetchAll().stream()
        .filter(entry -> entry.getId().equals(version))
        .findAny()
        .orElse(null);
  }

  public static Collection<VersionManifest.Entry> fetchAll() {
    try {
      VersionManifest versionManifest =
          new Gson()
              .fromJson(
                  new JsonReader(
                      new InputStreamReader(
                          new URL(VERSION_MANIFEST).openStream(), Charset.defaultCharset())),
                  VersionManifest.class);
      return Arrays.asList(versionManifest.getVersions());

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Version fetchDetails(VersionManifest.Entry entry) {

    try {
      return new Gson()
          .fromJson(
              new JsonReader(
                  new InputStreamReader(
                      new URL(entry.getUrl()).openStream(), Charset.defaultCharset())),
              Version.class);

    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static class Version {

    private Object arguments;
    private Object assetIndex;
    private String assets;
    private Version.Downloads downloads;
    private String id;
    private Library[] libraries;
    private Object logging;
    private String mainClass;
    private int minimumLauncherVersion;
    private String releaseTime;
    private String time;
    private String type;

    public Downloads getDownloads() {
      return this.downloads;
    }

    public Object getLogging() {
      return logging;
    }

    public String getMainClass() {
      return mainClass;
    }

    public int getMinimumLauncherVersion() {
      return minimumLauncherVersion;
    }

    public String getReleaseTime() {
      return releaseTime;
    }

    public String getTime() {
      return time;
    }

    public String getType() {
      return type;
    }

    public Object getAssetIndex() {
      return assetIndex;
    }

    public String getAssets() {
      return assets;
    }

    public String getId() {
      return id;
    }

    public Object getArguments() {
      return arguments;
    }

    public Library[] getLibraries() {
      return this.libraries;
    }

    public Version setArguments(Object arguments) {
      this.arguments = arguments;
      return this;
    }

    public Version setAssetIndex(Object assetIndex) {
      this.assetIndex = assetIndex;
      return this;
    }

    public Version setAssets(String assets) {
      this.assets = assets;
      return this;
    }

    public Version setDownloads(Downloads downloads) {
      this.downloads = downloads;
      return this;
    }

    public Version setId(String id) {
      this.id = id;
      return this;
    }

    public Version setLibraries(Library[] libraries) {
      this.libraries = libraries;
      return this;
    }

    public Version setLogging(Object logging) {
      this.logging = logging;
      return this;
    }

    public Version setMainClass(String mainClass) {
      this.mainClass = mainClass;
      return this;
    }

    public Version setMinimumLauncherVersion(int minimumLauncherVersion) {
      this.minimumLauncherVersion = minimumLauncherVersion;
      return this;
    }

    public Version setReleaseTime(String releaseTime) {
      this.releaseTime = releaseTime;
      return this;
    }

    public Version setTime(String time) {
      this.time = time;
      return this;
    }

    public Version setType(String type) {
      this.type = type;
      return this;
    }

    public String toString() {
      return "Version{"
          + "arguments="
          + arguments
          + ", assetIndex="
          + assetIndex
          + ", assets='"
          + assets
          + '\''
          + ", downloads="
          + downloads
          + ", id='"
          + id
          + '\''
          + ", libraries="
          + Arrays.toString(libraries)
          + ", logging="
          + logging
          + ", mainClass='"
          + mainClass
          + '\''
          + ", minimumLauncherVersion="
          + minimumLauncherVersion
          + ", releaseTime='"
          + releaseTime
          + '\''
          + ", time='"
          + time
          + '\''
          + ", type='"
          + type
          + '\''
          + '}';
    }

    public static class Downloads {

      private Download client;

      @SerializedName("client_mappings")
      private Download clientMappings;

      private Download server;

      @SerializedName("server_mappings")
      private Download serverMappings;

      public Download getClient() {
        return this.client;
      }

      public Download getClientMappings() {
        return clientMappings;
      }

      public Download getServer() {
        return server;
      }

      public Download getServerMappings() {
        return serverMappings;
      }

      public String toString() {
        return "Downloads{"
            + "client="
            + client
            + ", clientMappings="
            + clientMappings
            + ", server="
            + server
            + ", serverMappings="
            + serverMappings
            + '}';
      }

      public static class Download {
        private String sha1;
        private int size;
        private String url;

        public String getSha1() {
          return this.sha1;
        }

        public int getSize() {
          return this.size;
        }

        public String getUrl() {
          return this.url;
        }

        public String toString() {
          return "Client{" + "sha1='" + sha1 + '\'' + ", size=" + size + ", url='" + url + '\''
              + '}';
        }
      }
    }

    public static class Library {

      private Library.Downloads downloads;
      private Object extract;
      private String name;
      private Map<String, String> natives;
      private Object rules;

      public Library(
          Downloads downloads, Object extract, Object rules, String name, Map<String, String> natives) {
        this.downloads = downloads;
        this.extract = extract;
        this.rules = rules;
        this.name = name;
        this.natives = natives;
      }

      public Library() {
      }


      public Map<String, String> getNatives() {
        return natives;
      }

      public Object getRules() {
        return rules;
      }

      public Object getExtract() {
        return extract;
      }

      public String getName() {
        return name;
      }

      public String toString() {
        return "Library{" +
            "downloads=" + downloads +
            ", extract=" + extract +
            ", rules=" + rules +
            ", name='" + name + '\'' +
            ", natives=" + natives +
            '}';
      }

      public Library.Downloads getDownloads() {
        return this.downloads;
      }

      public static class Downloads {

        private Artifact artifact;
        private Map<String, Artifact> classifiers;

        public Downloads() {
        }

        public Downloads(Artifact artifact) {
          this.artifact = artifact;
        }

        public Downloads(Artifact artifact, Map<String, Artifact> classifiers) {
          this.artifact = artifact;
          this.classifiers = classifiers;
        }

        public Artifact getArtifact() {
          return this.artifact;
        }

        public Map<String, Artifact> getClassifiers() {
          return classifiers;
        }

        public String toString() {
          return "Downloads{" + "artifact=" + artifact + ", classifiers=" + classifiers + '}';
        }

        public static class Artifact {
          private String path;
          private String sha1;
          private int size;
          private String url;

          public Artifact() {
          }

          public Artifact(String path, String sha1, int size, String url) {
            this.path = path;
            this.sha1 = sha1;
            this.size = size;
            this.url = url;
          }

          public String getPath() {
            return this.path;
          }

          public String getSha1() {
            return this.sha1;
          }

          public int getSize() {
            return this.size;
          }

          public String getUrl() {
            return this.url;
          }

          public String toString() {
            return "Artifact{"
                + "path='"
                + path
                + '\''
                + ", sha1='"
                + sha1
                + '\''
                + ", size="
                + size
                + ", url='"
                + url
                + '\''
                + '}';
          }

          public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Artifact artifact = (Artifact) o;
            return size == artifact.size
                && Objects.equals(path, artifact.path)
                && Objects.equals(sha1, artifact.sha1)
                && Objects.equals(url, artifact.url);
          }

          public int hashCode() {
            return Objects.hash(path, sha1, size, url);
          }
        }
      }
    }
  }

  public static class VersionManifest {

    private Entry[] versions;

    public Entry[] getVersions() {
      return this.versions;
    }

    public static class Entry {
      private String id;
      private String type;
      private String url;
      private String time;
      private String releaseTime;

      public String getId() {
        return this.id;
      }

      public String getType() {
        return this.type;
      }

      public String getUrl() {
        return this.url;
      }

      public String getTime() {
        return this.time;
      }

      public String getReleaseTime() {
        return this.releaseTime;
      }

      public String toString() {
        return "Version{"
            + "id='"
            + id
            + '\''
            + ", type='"
            + type
            + '\''
            + ", url='"
            + url
            + '\''
            + ", time='"
            + time
            + '\''
            + ", releaseTime='"
            + releaseTime
            + '\''
            + '}';
      }

      public Version getDetails() {
        return fetchDetails(this);
      }
    }

    public String toString() {
      return "VersionManifest{" + "versions=" + Arrays.toString(versions) + '}';
    }
  }
}
