plugins {
    id("java-library")
    id("net.flintmc.flint-gradle-plugin")
}

group = "net.flintmc"

flint.configureNow()

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    "internalAnnotationProcessor"(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-inject"))
    api(project(":transform:transform-hook"))
}
