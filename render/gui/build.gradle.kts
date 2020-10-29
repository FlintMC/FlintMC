plugins {
    id("java-library")
}

group = "net.flintmc"

flint {
    minecraftVersions("1.15.2")
}

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    v1_15_2Implementation(project(":annotation-processing:annotation-processing-autoload"))
    api(project(":framework:framework-eventbus"))

}
