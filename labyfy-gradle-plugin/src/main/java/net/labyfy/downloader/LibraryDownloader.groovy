package net.labyfy.downloader


import com.google.common.collect.*
import com.google.common.io.Files
import com.google.gson.GsonBuilder
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.name.Names
import org.apache.commons.io.IOUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.happy.collections.lists.decorators.SortedList_1x0

import java.security.MessageDigest
import java.util.function.BiConsumer

class LibraryDownloadExtension {
    String version;
}

class LibraryDownloader implements Plugin<Project> {

    void apply(Project project) {
        def extension = project.extensions.create('minecraft', LibraryDownloadExtension)
        // Add a task that uses configuration from the extension object
        project.task('download-libraries') {
            doLast {
                if (extension.version == null) throw new IllegalArgumentException("minecraft.version must be set!")
                VersionFetcher.VersionManifest.Entry entry = VersionFetcher.fetch(extension.version)
                File libraries = new File(project.projectDir, "libraries");
                if (!libraries.exists()) libraries.mkdirs()


                Multimap<String, VersionFetcher.Version.Library.Downloads> versions = Multimaps.newListMultimap(
                        new TreeMap<>(new Comparator<String>() {
                            int compare(String o1, String o2) {
                                return o1 <=> o2
                            }
                        }),
                        { ->
                            SortedList_1x0.of(Lists.newArrayList(), new Comparator<VersionFetcher.Version.Library.Downloads>() {
                                int compare(VersionFetcher.Version.Library.Downloads o1, VersionFetcher.Version.Library.Downloads o2) {
                                    return o1.artifact.path <=> o2.artifact.path
                                }
                            })
                        });


                VersionFetcher.Version details = entry.details;

                println "downloading client libraries:"

                if (new File("libraries", "client.jar").exists()) {
                    println " -> skip client.jar"
                } else {
                    this.downloadArtifact("client", extension.version, libraries, details.downloads.client.url)
                }

                details.libraries.each { VersionFetcher.Version.Library library ->
                    String[] path = library.downloads.artifact.path.split('/');
                    String artifact = path[path.length - 3]
                    versions.put(artifact, library.downloads)
                }


                ImmutableSortedSet.copyOfSorted(versions.keySet()).each { String artifact ->
                    String[] path = versions.get(artifact).last().artifact.path.split('/');
                    String targetVersion = path[path.length - 2]
                    for (VersionFetcher.Version.Library.Downloads downloads : versions.get(artifact)) {

                        path = downloads.artifact.path.split('/');
                        String version = path[path.length - 2]
                        if (targetVersion == version) {
                            downloadArtifact(artifact, version, libraries, downloads.artifact.url)
                            if (downloads.classifiers != null) {
                                downloads.classifiers.forEach(new BiConsumer<String, VersionFetcher.Version.Library.Downloads.Artifact>() {
                                    void accept(String name, VersionFetcher.Version.Library.Downloads.Artifact classifier) {
                                        path = classifier.path.split('/')
                                        version = path[path.length - 2]
                                        downloadArtifact(artifact + "-" + name, version, libraries, classifier.url)
                                    }
                                })
                            }
                        }
                    }
                }

                String version = extension.version

                Injector injector = Guice.createInjector(new AbstractModule() {
                    protected void configure() {
                        this.bind(Key.get(File.class, Names.named("libraries"))).toInstance(libraries)
                        this.bind(Key.get(Map.class, Names.named("launchArguments"))).toInstance(ImmutableMap.<String, String> of("--version", version))
                        this.bind(Key.get(File.class, Names.named("input"))).toInstance(new File(libraries, "client-" + extension.version + ".jar"))
                        this.bind(Key.get(File.class, Names.named("output"))).toInstance(new File(libraries, "client-" + extension.version + ".jar"))
                        this.bind(Key.get(File.class, Names.named("labyfyRoot"))).toInstance(new File(project.projectDir, "Labyfy"))
                    }
                });

                println('downloading mappings:')
                download("https://dl.labymod.net/mappings/" + extension.version + "/methods.csv", new File(project.projectDir, "Labyfy/assets/" + extension.version + "/methods.csv"))
                download("https://dl.labymod.net/mappings/" + extension.version + "/fields.csv", new File(project.projectDir, "Labyfy/assets/" + extension.version + "/fields.csv"))
                download("https://dl.labymod.net/mappings/" + extension.version + "/joined.tsrg", new File(project.projectDir, "Labyfy/assets/" + extension.version + "/joined.tsrg"))

                println "deobfuscating client.jar:"
                injector.getInstance(LabyDeobfuscator.class);
                println "finished"

            }

        }

        project.task('generate-json') {
            doLast {
                VersionFetcher.Version version = VersionFetcher.fetch(extension.version).getDetails();


                Collection<VersionFetcher.Version.Library> libraries = new LinkedHashSet<>()

                getChildren(getHighestProject(project)).each { childProject ->
                    childProject.configurations.getByName("minecraft").dependencies.each { dependency ->

                        if (dependency.version == null || dependency.group == null || dependency.version == null) return
                        for (ArtifactRepository repository : childProject.repositories.asList()) {


                            def url = repository.properties.get('url')
                            def jarUrl = String.format("%s%s/%s/%s/%s-%s.jar", url.toString(),
                                    dependency.group.replace('.', '/'), dependency.name, dependency.version,
                                    dependency.name, dependency.version)
                            try {
                                HttpURLConnection huc = (HttpURLConnection) new URL(jarUrl).openConnection();
                                huc.setRequestProperty("User-Agent",
                                        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; en-US; rv:1.9.0.13) Gecko/2009073021 Firefox/3.0.13");
                                huc.setRequestMethod("GET");
                                huc.connect();

                                if (huc.getResponseCode() == 200) {

                                    Formatter formatter = new Formatter();
                                    byte[] data = IOUtils.toByteArray(huc);
                                    for (byte b : MessageDigest.getInstance("SHA-1").digest(data)) {
                                        formatter.format("%02x", b);
                                    }
                                    def sha1sum = formatter.toString()

                                    libraries.add(
                                            new VersionFetcher.Version.Library(
                                                    new VersionFetcher.Version.Library.Downloads(
                                                            new VersionFetcher.Version.Library.Downloads.Artifact(
                                                                    (dependency.group + "." + dependency.name + "." + dependency.version).replace('.', '/'),
                                                                    sha1sum,
                                                                    data.length,
                                                                    jarUrl
                                                            )), null, null, dependency.group + ":" + dependency.name + ":" + dependency.version, null))
                                    huc.disconnect()
                                    return
                                }
                                huc.disconnect()
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
                version.id = "Labyfy-" + version.id
                libraries.addAll(Arrays.asList(version.libraries))
                version.mainClass = "net.labyfy.component.launcher.LabyLauncher"

                Object[] gameArguments = new Object[version.getArguments().getGame().length + 2];
                System.arraycopy(version.arguments.game, 0, gameArguments, 0, version.arguments.game.length);
                gameArguments[gameArguments.length - 2] = "--tweakClass"
                gameArguments[gameArguments.length - 1] = "net.labyfy.component.transform.tweaker.LabyDebugTweaker"

                version.arguments.game = gameArguments
                version.libraries = libraries.<VersionFetcher.Version.Library> toArray([] as VersionFetcher.Version.Library)
                println new GsonBuilder().disableHtmlEscaping().create().toJson(version)

            }
        }.mustRunAfter("build")

        project.defaultTasks("download-libraries")

    }

    private Project getHighestProject(Project project) {
        if (project.parent == null) {
            return project;
        }

        if (project.parent.parent == null) {
            return project.parent;
        }

        return getHighestProject(project.parent);
    }

    private Collection<Project> getChildren(Project project) {
        Collection<Project> projects = new HashSet<>();
        projects.add(project)
        project.childProjects.values().each { child ->
            projects.addAll(getChildren(child))
        }
        return projects
    }

    private void download(String url, File file) {
        if (file.exists()) return;
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
        httpURLConnection.setRequestProperty("User-Agent", getUserAgent());
        httpURLConnection.connect();
        file.getParentFile().mkdirs()
        Files.write(IOUtils.toByteArray(httpURLConnection), file)
    }

    private String getUserAgent() {
        return "LabyMod v" + "4" + " on mc" + "1.15.1";
    }


    private void downloadArtifact(String artifact, String version, File libraries, String url) {
        File file = new File(libraries, artifact + "-" + version + ".jar");
        if (file.exists()) {
            println " -> skip " + artifact + "-" + version + ".jar to " + libraries.getAbsolutePath() + " url " + url
            return
        };
        println " -> download " + artifact + "-" + version + ".jar to " + libraries.getAbsolutePath() + " url " + url
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        IOUtils.write(IOUtils.toByteArray(new URL(url)), fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    }

}
