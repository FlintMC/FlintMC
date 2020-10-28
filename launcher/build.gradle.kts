plugins {
    id("java-library")
    id("net.flintmc.flint-gradle-plugin")
}

group = "net.flintmc"

flint.configureNow()

dependencies {
    api(project(":transform:transform-exceptions"))

    api("org.apache.logging.log4j", "log4j-api", "2.8.1")
    api("com.beust", "jcommander", "1.78")
    api("io.sentry", "sentry-log4j2", "1.7.30")

    runtimeOnly("org.apache.logging.log4j", "log4j-core", "2.8.1")

    "flint"(project(":transform:transform-exceptions"))

    "flint"("org.apache.logging.log4j", "log4j-api", "2.8.1")
    "flint"("com.beust", "jcommander", "1.78")
    "flint"("io.sentry", "sentry-log4j2", "1.7.30")
    "flint"("org.apache.logging.log4j", "log4j-core", "2.8.1")
}
