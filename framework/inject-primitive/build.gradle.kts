plugins {
    id("java-library")
    id("net.flintmc.flint-gradle-plugin")
}

group = "net.flintmc"

flint.configureNow()

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    "internalAnnotationProcessor"(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-inject-primitive"))
    api(project(":framework:framework-stereotype"))

    "flint"(project(":framework:framework-inject-primitive"))
    "flint"(project(":framework:framework-stereotype"))

    "flint"("com.google.inject.extensions", "guice-assistedinject", "4.2.0")
    "flint"("org.apache.logging.log4j", "log4j-labyfy", "2.8.1")
}
