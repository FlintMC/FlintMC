package net.labyfy.gradle.manifest;

public class ManifestDownload implements InstallInstruction {

  private final String type = "DOWNLOAD_FILE";
  private Data data;

  public String getType() {
    return type;
  }

  public Data getData() {
    return data;
  }

  public ManifestDownload setData(Data data) {
    this.data = data;
    return this;
  }

  public static class Data {
    private String url;
    private String path;
    private String md5;

    public String getUrl() {
      return url;
    }

    public String getPath() {
      return path;
    }

    public String getMd5() {
      return md5;
    }

    public Data setUrl(String url) {
      this.url = url;
      return this;
    }

    public Data setPath(String path) {
      this.path = path;
      return this;
    }

    public Data setMd5(String md5) {
      this.md5 = md5;
      return this;
    }
  }

}
