package net.labyfy.component.packages.impl;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.packages.Package;
import net.labyfy.component.packages.PackageLoader;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Singleton
@Task(Tasks.PRE_MINECRAFT_INITIALIZE)
@Implement(PackageLoader.class)
public class LabyPackageLoader implements PackageLoader {

    private final File packageFolder;
    private final Set<File> jars = new HashSet<>();

    @Inject
    private LabyPackageLoader( @Named("labyfyPackageFolder") File packageFolder ) {
        this.packageFolder = packageFolder;

        System.out.println("Reading packages in " + packageFolder.getAbsolutePath());
        if(packageFolder.listFiles() != null) {
            for (File labypackage : Objects.requireNonNull(packageFolder.listFiles())) {
                if(labypackage.getName().toLowerCase().endsWith(".jar"))
                    jars.add(labypackage);
            }
        }
    }

    @TaskBody
    private void load() {
        System.out.println("Loading packages from " + packageFolder.getAbsolutePath() + "...");
        for(File file: jars){
            System.out.println("-- now loading " + file.getName());
        }
    }

    @Override
    public List<Package> getAllPackages() {
        return null;
    }

    @Override
    public List<Package> getLoadedPackages() {
        return null;
    }
}
