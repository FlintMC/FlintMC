plugins {
    id("java-library")
}

group = "net.flintmc"

dependencies {
    api(project(":transform:transform-exceptions"))

    api("org.apache.logging.log4j", "log4j-api", "2.14.0")
    api("com.beust", "jcommander", "1.78")
    api("io.sentry", "sentry-log4j2", "1.7.30")

    runtimeOnly("org.apache.logging.log4j", "log4j-core", "2.14.0")
}
