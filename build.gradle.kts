buildscript {

    dependencies {
        classpath("net.flintmc", "flint-gradle-javadocs-plugin", "1.0.0")
        classpath("net.flintmc", "flint-gradle-plugin", "2.0.2")
    }


    repositories {
        maven {
            setUrl("https://git.laby.tech/v4/groups/client/-/packages/maven")
            name = "Gitlab"
            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
        mavenCentral()
    }
}

plugins {
    `kotlin-dsl`
}

