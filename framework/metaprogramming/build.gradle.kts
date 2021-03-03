plugins {
    id("java-library")
}

group = "net.flintmc"

repositories {
    mavenCentral()
}

dependencies {
    api("org.javassist", "javassist", "3.27.0-GA")
}
