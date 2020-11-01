plugins {
    id("java-library")
}

group = "net.flintmc"

flint {
    type = net.flintmc.gradle.extension.FlintGradleExtension.Type.LIBRARY
}

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    v1_15_2AnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-inject"))
    api(project(":framework:framework-stereotype"))

    api("org.eclipse.collections", "eclipse-collections", "8.2.1")

    v1_15_2Implementation(project(":transform:transform-javassist"))
}
