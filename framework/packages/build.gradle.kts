plugins {
    id("java-library")
}

group = "net.flintmc"

flint.configureNow()

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    "internalAnnotationProcessor"(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-inject"))
    api(project(":framework:framework-stereotype"))
    api(project(":util:util-commons"))

    "internalImplementation"(project(":framework:framework-inject", "internal"))
    "internalImplementation"(project(":framework:framework-service"))
    "internalImplementation"(project(":framework:framework-stereotype", "internal"))
    "internalImplementation"(project(":framework:framework-tasks"))

    "internalImplementation"("com.google.code.gson", "gson", "2.8.6")

    "flint"(project(":framework:framework-inject"))
    "flint"(project(":framework:framework-inject", "internal"))
    "flint"(project(":framework:framework-service"))
    "flint"(project(":framework:framework-stereotype"))
    "flint"(project(":framework:framework-stereotype", "internal"))
    "flint"(project(":util:util-commons"))

    "flint"("com.google.code.gson", "gson", "2.8.6")
}
