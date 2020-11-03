plugins {
    id("java-library")
}

group = "net.flintmc"



dependencies {
    annotationProcessor("com.google.auto.service", "auto-service", "1.0-rc6")

    api(project(":util:util-commons"))

    api("org.apache.commons", "commons-lang3", "3.10")
    api("com.squareup", "javapoet", "1.13.0")
    api("commons-io", "commons-io", "2.6")

    implementation("com.google.auto.service:auto-service:1.0-rc6")
}