package net.labyfy.downloader

import com.google.common.collect.*
import com.google.common.io.Files
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.name.Names
import org.apache.commons.io.IOUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.happy.collections.lists.decorators.SortedList_1x0

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
                                if (downloads.classifiers.nativesLinux != null) {
                                    path = downloads.classifiers.nativesLinux.path.split('/')
                                    version = path[path.length - 2]
                                    this.downloadArtifact(artifact + "-natives-linux", version, libraries, downloads.classifiers.nativesLinux.url)
                                }
                                if (downloads.classifiers.nativesMacOS != null) {
                                    path = downloads.classifiers.nativesMacOS.path.split('/')
                                    version = path[path.length - 2]
                                    this.downloadArtifact(artifact + "-natives-macos", version, libraries, downloads.classifiers.nativesMacOS.url)
                                }
                                if (downloads.classifiers.nativesWindows != null) {
                                    path = downloads.classifiers.nativesWindows.path.split('/')
                                    version = path[path.length - 2]
                                    this.downloadArtifact(artifact + "-natives-windows", version, libraries, downloads.classifiers.nativesWindows.url)
                                }
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

        project.defaultTasks("download-libraries")

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
        FileOutputStream fileOutputStream = new FileOutputStream();
        IOUtils.write(IOUtils.toByteArray(new URL(url)), fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    }

}
