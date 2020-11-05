buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:${KotlinVersion.CURRENT}")
    }
}

plugins {
    `kotlin-dsl`
}
apply(plugin = "kotlinx-serialization")

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.3")
}

repositories {
    mavenCentral()
}