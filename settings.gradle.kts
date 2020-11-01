rootProject.name = "flint"

fun defineModule(path: String) {
    include(path)
    findProject(":$path")?.name = path.replace(":", "-")
}

pluginManagement {
    plugins {
        id("net.flintmc.flint-gradle-plugin") version "2.3.3"
    }

    buildscript {
        dependencies {
            classpath("net.flintmc", "flint-gradle-plugin", "2.3.3")
        }
        repositories {
            mavenLocal()
            val labymediaMavenAuthToken: String? by settings
/*
            maven {
                setUrl("https://git.laby.tech/api/v4/groups/2/-/packages/maven")
                name = "Gitlab"
                credentials(HttpHeaderCredentials::class) {
                    if (System.getenv().containsKey("CI_JOB_TOKEN")) {
                        name = "Job-Token"
                        value = System.getenv("CI_JOB_TOKEN")
                    } else {
                        name = "Private-Token"
                        value = labymediaMavenAuthToken
                    }
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }*/
            mavenCentral()
        }
    }

}


defineModule("annotation-processing:autoload")
defineModule("annotation-processing:build.gradle.bak")

defineModule("framework:eventbus")
defineModule("framework:inject")
defineModule("framework:inject-primitive")
defineModule("framework:packages")
defineModule("framework:service")
defineModule("framework:stereotype")
defineModule("framework:tasks")

defineModule(":launcher")

defineModule(":mcapi")

defineModule("render:gui")
defineModule("render:shader")
defineModule("render:vbo-rendering")
defineModule("render:webgui")

defineModule("transform:asm")
defineModule("transform:exceptions")
defineModule("transform:hook")
defineModule("transform:javassist")
defineModule("transform:launcher-plugin")
defineModule("transform:minecraft")
defineModule("transform:minecraft-obfuscator")
defineModule("transform:shadow")

defineModule("util:commons")
defineModule("util:csv")
defineModule("util:i18n")
defineModule("util:mapping")
defineModule("util:session-service")
