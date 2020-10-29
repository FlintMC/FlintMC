plugins {
    id("java-library")
}

group = "net.flintmc"

flint.configureNow()

dependencies {
    api("org.joml", "joml", "1.9.25")
    api("com.google.inject", "guice", "4.2.0")

    implementation("com.google.auto.service", "auto-service", "1.0-rc6")
}