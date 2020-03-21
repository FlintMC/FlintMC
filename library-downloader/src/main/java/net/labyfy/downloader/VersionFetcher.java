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

    private Version.Downloads downloads;
    private Library[] libraries;

    public Downloads getDownloads() {
      return this.downloads;
    }

    public Library[] getLibraries() {
      return this.libraries;
    }

    public String toString() {
      return "Version{" +
              "downloads=" + downloads +
              ", libraries=" + Arrays.toString(libraries) +
              '}';
    }

    public static class Downloads {

      private Client client;

      public Client getClient() {
        return this.client;
      }

      public String toString() {
        return "Downloads{" + "client=" + client + '}';
      }

      public static class Client {
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

      public String toString() {
        return "Library{" + "downloads=" + downloads + '}';
      }

      public Library.Downloads getDownloads() {
        return this.downloads;
      }

      public static class Downloads {

        private Artifact artifact;
        private Classifiers classifiers;


        public Artifact getArtifact() {
          return this.artifact;
        }

        public Classifiers getClassifiers() {
          return classifiers;
        }

        public String toString() {
          return "Downloads{" +
                  "artifact=" + artifact +
                  ", classifiers=" + classifiers +
                  '}';
        }

        public static class Classifiers {
          private Artifact javadoc;

          @SerializedName("natives-linux")
          private Artifact nativesLinux;

          @SerializedName("natives-windows")
          private Artifact nativesWindows;

          @SerializedName("natives-macos")
          private Artifact nativesMacOS;

          private Artifact sources;

          public Artifact getJavadoc() {
            return javadoc;
          }

          public Artifact getNativesLinux() {
            return nativesLinux;
          }

          public Artifact getNativesWindows() {
            return nativesWindows;
          }

          public Artifact getNativesMacOS() {
            return nativesMacOS;
          }

          public Artifact getSources() {
            return sources;
          }

          public String toString() {
            return "Classifiers{" +
                    "javadoc=" + javadoc +
                    ", nativesLinux=" + nativesLinux +
                    ", nativesWindows=" + nativesWindows +
                    ", nativesMaxOS=" + nativesMacOS +
                    ", sources=" + sources +
                    '}';
          }
        }

        public static class Artifact {
          private String path;
          private String sha1;
          private int size;
          private String url;

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
