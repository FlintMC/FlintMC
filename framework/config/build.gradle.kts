plugins {
    id("java-library")
}

group = "net.flintmc"

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-inject"))
    api(project(":framework:framework-eventbus"))
    api(project(":framework:framework-stereotype"))

    api(project(":transform:transform-hook"))

    api("com.google.code.gson", "gson", "2.8.6")
}
