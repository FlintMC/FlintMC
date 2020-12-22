plugins {
    id("java-library")
}

group = "net.flintmc"

dependencies {
    minecraft("1.15.2") {
        annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    }
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-inject"))
    api(project(":mcapi"))

    internalImplementation("com.mojang", "authlib", "1.6.25")
}
