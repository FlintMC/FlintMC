plugins {
    id("java-library")
}

group = "net.flintmc"

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    "internalAnnotationProcessor"(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":annotation-processing:annotation-processing-autoload"))
    api(project(":util:util-mapping"))

    internalImplementation(project(":transform:transform-launcher-plugin"))
    internalImplementation(project(":transform:transform-minecraft"))
}
