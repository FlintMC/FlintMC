plugins {
    id("java-library")
}

group = "net.flintmc"


dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    v1_15_2AnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api("org.joml", "joml", "1.9.25")
    api(project(":framework:framework-inject"))
}
