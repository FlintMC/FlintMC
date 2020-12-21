plugins {
    id("java-library")
}

group = "net.flintmc"

flint{
    minecraftVersions("1.15.2")
}

dependencies {
    minecraft("1.15.2"){
        annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    }

    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-stereotype"))
    api(project(":framework:framework-inject"))
}
