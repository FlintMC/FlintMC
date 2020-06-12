package net.labyfy.gradle.library;

import java.util.Map;

public class GradleModuleMetaData {

  private final String formatVersion = "1.0";
  private Component component;
  private Variant[] variants;

  public String getFormatVersion() {
    return formatVersion;
  }

  public Component getComponent() {
    return component;
  }

  public GradleModuleMetaData setComponent(Component component) {
    this.component = component;
    return this;
  }

  public Variant[] getVariants() {
    return variants;
  }

  public GradleModuleMetaData setVariants(Variant[] variants) {
    this.variants = variants;
    return this;
  }

  public static class Component{
    private String group;
    private String module;
    private String version;

    public String getGroup() {
      return group;
    }

    public Component setGroup(String group) {
      this.group = group;
      return this;
    }

    public String getModule() {
      return module;
    }

    public Component setModule(String module) {
      this.module = module;
      return this;
    }

    public String getVersion() {
      return version;
    }

    public Component setVersion(String version) {
      this.version = version;
      return this;
    }
  }

  public static class Variant{
    private String name;
    private Map<String, Object> attributes;
    private File[] files;
    private Dependency[] dependencies;

    public String getName() {
      return name;
    }

    public Variant setName(String name) {
      this.name = name;
      return this;
    }

    public Map<String, Object> getAttributes() {
      return attributes;
    }

    public Variant setAttributes(Map<String, Object> attributes) {
      this.attributes = attributes;
      return this;
    }

    public File[] getFiles() {
      return files;
    }

    public Variant setFiles(File[] files) {
      this.files = files;
      return this;
    }

    public Dependency[] getDependencies() {
      return dependencies;
    }

    public Variant setDependencies(Dependency[] dependencies) {
      this.dependencies = dependencies;
      return this;
    }

    public static class File{
      private String name;
      private String url;

      public String getName() {
        return name;
      }

      public File setName(String name) {
        this.name = name;
        return this;
      }

      public String getUrl() {
        return url;
      }

      public File setUrl(String url) {
        this.url = url;
        return this;
      }
    }

    public static class Dependency{
      private String group;
      private String module;
      private Map<String, String> version;

      public String getGroup() {
        return group;
      }

      public Dependency setGroup(String group) {
        this.group = group;
        return this;
      }

      public String getModule() {
        return module;
      }

      public Dependency setModule(String module) {
        this.module = module;
        return this;
      }

      public Map<String, String> getVersion() {
        return version;
      }

      public Dependency setVersion(Map<String, String> version) {
        this.version = version;
        return this;
      }
    }
  }

}
