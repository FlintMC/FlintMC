package net.labyfy.component.packages;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.name.Names;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.subproperty.TaskBody;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBodyPriority;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;

/**
 * This class is used to instantiate the package loader
 * which is used to resolve and load the packages and their dependencies
 */
@Singleton
@Task(Tasks.PRE_MINECRAFT_INITIALIZE)
@TaskBodyPriority()
public class PackageLoader {

    private final File packageFolder;

    @Inject
    private PackageLoader(
            @Named("packageFolder") File pakcageFolder) {
        this.packageFolder = pakcageFolder;

        Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                this.bind(Key.get(String.class, Names.named("blub"))).toInstance("Test123");
            }
        }).getInstance(Key.get(String.class, Names.named("blub")));
    }

    @TaskBody
    private void load() {
        System.out.println("Load packages...");
    }
}
