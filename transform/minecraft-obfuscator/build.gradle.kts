plugins {
    id("java-library")
}

group = "net.flintmc"



dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-inject"))
    api(project(":transform:transform-asm"))
    api(project(":transform:transform-launcher-plugin"))
    api(project(":transform:transform-minecraft"))
    api(project(":util:util-mapping"))
}
