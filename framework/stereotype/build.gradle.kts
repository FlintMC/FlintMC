plugins {
    id("java-library")
}

group = "net.flintmc"

dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":annotation-processing:annotation-processing-autoload"))
    api(project(":framework:framework-inject-primitive"))
    api(project(":launcher"))
    api(project(":util:util-commons"))

    api("com.google.guava", "guava", "21.0")
    api("com.google.inject", "guice", "4.2.0")
    api("org.apache.commons", "commons-lang3", "3.9")
    api("org.codehaus.groovy", "groovy-all", "3.0.2")
    api("org.javassist", "javassist", "3.25.0-GA")

}
