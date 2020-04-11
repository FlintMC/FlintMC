package net.labyfy.component.packages;

import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class is used to instantiate the package loader
 * which is used to resolve and load the packages and their dependencies
 */
@Singleton
@Task(Tasks.PRE_MINECRAFT_INITIALIZE)
public class PackageLoader {

    private final File packageFolder;
    private final Set<File> jars = new HashSet<>();

    @Inject
    private PackageLoader( @Named("labyfyPackageFolder") File packageFolder ) {
        this.packageFolder = packageFolder;

        System.out.println("Reading packages in " + packageFolder.getAbsolutePath());
        if(packageFolder.listFiles() != null) {
            for (File labypackage : Objects.requireNonNull(packageFolder.listFiles())) {
                if(labypackage.getName().endsWith(".jar"))
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
}
