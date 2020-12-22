plugins {
    id("java-library")
}

group = "net.flintmc"

dependencies {
    minecraft("1.15.2"){
        implementation(project(":transform:transform-hook"))
        implementation(project(":transform:transform-javassist"))
        implementation(project(":transform:transform-shadow"))
    }
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-stereotype"))
    api(project(":transform:transform-minecraft"))



}
