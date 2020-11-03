plugins {
    id("java-library")
}

group = "net.flintmc"



dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    v1_15_2AnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-stereotype"))
    api(project(":framework:framework-inject"))
    api(project(":framework:framework-packages"))
    api(project(":framework:framework-service"))
}
