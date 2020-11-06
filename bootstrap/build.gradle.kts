plugins {
    id("java-library")
}

group = "net.flintmc"

configurations {
    create("manifest")
}

dependencies {
    /*project.rootProject.subprojects.forEach { subProject ->
        if (!arrayOf(
                ":",
                ":framework",
                ":render",
                ":transform",
                ":util",
                ":minecraft",
                ":minecraft:minecraft-minecraft-1-15-2",
                ":bootstrap"
            ).contains(subProject.path)
        ) {
            runtimeOnly(subProject)
            subProject.configurations.getByName("runtimeClasspath")
                .allDependencies.forEach {
                    runtimeOnly(it)
                }
        }
    }*/

    runtimeOnly(project(":launcher"))
    runtimeOnly(project(":transform:transform-launcher-plugin"))

    val manifest = configurations.getByName("manifest")
    configurations.getByName("implementation").extendsFrom(manifest)

    manifest("net.flintmc.installer", "logic", "1.1.2")
    manifest("net.flintmc.installer", "logic-implementation", "1.1.2")
    manifest("com.google.code.gson", "gson", "2.8.6")

}