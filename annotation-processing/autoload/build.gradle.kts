plugins {
    id("java-library")
}

group = "net.flintmc"

flint {
    type = net.flintmc.gradle.extension.FlintGradleExtension.Type.LIBRARY
}

dependencies {
    annotationProcessor("com.google.auto.service", "auto-service", "1.0-rc6")

    api(project(":annotation-processing"))
    api(project(":util:util-commons"))

    api("com.google.auto.service", "auto-service", "1.0-rc6")
    api("org.javassist", "javassist", "3.25.0-GA")
    api("org.apache.commons", "commons-lang3", "3.10")
    api("com.squareup", "javapoet", "1.13.0")
    api("commons-io", "commons-io", "2.6")
}