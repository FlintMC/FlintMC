plugins {
    id("java-library")
}

group = "net.flintmc"

dependencies {
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-inject"))
}