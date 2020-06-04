package net.labyfy.gradle.manifest;

public class Manifest {

  private String name;
  private String description;
  private String version;
  private String[] authors;
  private InstallInstruction[] installInstructions;

  public String getName() {
    return name;
  }

  public Manifest setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Manifest setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getVersion() {
    return version;
  }

  public Manifest setVersion(String version) {
    this.version = version;
    return this;
  }

  public String[] getAuthors() {
    return authors;
  }

  public Manifest setAuthors(String... authors) {
    this.authors = authors;
    return this;
  }

  public InstallInstruction[] getInstallInstructions() {
    return installInstructions;
  }

  public Manifest setInstallInstructions(InstallInstruction... installInstructions) {
    this.installInstructions = installInstructions;
    return this;
  }
}
