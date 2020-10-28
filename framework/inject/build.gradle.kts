plugins {
    id("java-library")
    id("net.flintmc.flint-gradle-plugin")
}

group = "net.flintmc"

flint.configureNow()

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    "internalAnnotationProcessor"(project(":annotation-processing:annotation-processing-autoload"))

    api("com.google.inject", "guice", "4.2.0")

    "flint"("com.google.inject", "guice", "4.2.0")
}
