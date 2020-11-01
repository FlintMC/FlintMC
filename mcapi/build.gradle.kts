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

    api(project(":framework:framework-eventbus"))
    api(project(":framework:framework-inject"))
    api(project(":framework:framework-stereotype"))
    api(project(":framework:framework-tasks"))
    api(project(":transform:transform-hook"))
    api(project(":transform:transform-shadow"))

    api("com.google.code.gson", "gson", "2.8.6")
}
