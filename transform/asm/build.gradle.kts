plugins {
    id("java-library")
}

group = "net.flintmc"

flint.configureNow()

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":framework:framework-stereotype"))

    api("org.ow2.asm", "asm", "7.2-beta")
    api("org.ow2.asm", "asm-tree", "7.2-beta")
    api("org.ow2.asm", "asm-commons", "7.2-beta")
    api("com.google.guava", "guava", "21.0")

    internalImplementation(project(":transform:transform-launcher-plugin"))
    internalImplementation(project(":transform:transform-minecraft"))
    internalImplementation(project(":util:util-mapping"))
}
