plugins {
    id("java-library")
}

group = "net.flintmc"

flint.configureNow()

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    v1_15_2AnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-inject"))
    api(project(":framework:framework-stereotype"))
    
    api("org.eclipse.collections", "eclipse-collections", "8.2.1")
    
    "v1_15_2Implementation"(project(":transform:transform-javassist"))

    "flint"(project(":framework:framework-stereotype"))
    "flint"(project(":framework:framework-inject"))
    "flint"(project(":transform:transform-javassist"))

    "flint"("org.eclipse.collections", "eclipse-collections", "8.2.1")
}
