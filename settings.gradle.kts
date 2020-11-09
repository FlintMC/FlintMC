rootProject.name = "flint"

fun defineModule(path: String) {
    include(path)
    findProject(":$path")?.name = path.replace(":", "-")
}

pluginManagement {
    plugins {
        id("net.flintmc.flint-gradle-plugin") version "2.5.8"
    }

    buildscript {
        dependencies {
            classpath("net.flintmc", "flint-gradle-plugin", "2.5.8")
        }
        repositories {
            mavenLocal()
            maven {
                setUrl("https://dist.labymod.net/api/v1/maven/release")
                name = "Flint"
                credentials(HttpHeaderCredentials::class) {
                    name = "Authorization"
                    value = "Bearer CbtTjzAOuDBr5QXcGnBc1MB3eIHxcZetnyHtdN76VpTNgbwAf87bzWPCntsXwj52"
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
            mavenCentral()
        }
    }

}


defineModule("annotation-processing:autoload")

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

defineModule("minecraft:minecraft-1-15-2")
include("bootstrap")
