import net.flintmc.gradle.extension.FlintGradleExtension.Type
import net.flintmc.minecraft.version.MinecraftVersionGenerator
import java.nio.file.Paths

plugins {
    id("java-library")
}

afterEvaluate {
    val minecraftVersion = MinecraftVersionGenerator.generateWithProjectDependencies(
        versionFile = file("version.json"),
        project = project
    )

    minecraftVersion.mainClass = "net.flintmc.launcher.FlintLauncher"
    minecraftVersion.id = "flint-1.15.2"

    val version = file("build/generated/flint/version.json")
    version.parentFile.mkdirs()
    version.writeBytes(
        MinecraftVersionGenerator.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(minecraftVersion)
    )
}

flint {
    type = Type.LIBRARY
    staticFileEntry(
        Paths.get("build/generated/flint/version.json"),
        Paths.get("versions/flint-1.15.2/flint-1.15.2.json"),
        "version.json"
    )
}

dependencies{
    implementation(project(":launcher"))
    implementation(project(":framework:framework-packages"))
}
