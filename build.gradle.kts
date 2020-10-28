plugins {
    id("net.flintmc.flint-gradle-plugin") version "2.3.2"
}

fun RepositoryHandler.labymedia() {
    val labymediaMavenAuthToken: String? by project

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
    }
}

repositories {
    labymedia()
    mavenCentral()
}

flint {
    projectFilter { !arrayOf("annotation-processing", "autoload").contains(it.name) }
    minecraftVersions("1.15.2", "1.16.3")
}

subprojects {
    plugins.withId("java") {
        repositories {
            labymedia()
            mavenCentral()
        }
    }
}