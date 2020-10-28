buildscript {

    dependencies {
        classpath("net.flintmc", "flint-gradle-plugin", "2.1.3")
    }


    repositories {
        maven {
            setUrl("https://git.laby.tech/api/v4/groups/2/-/packages/maven")
            name = "Gitlab"
            credentials(HttpHeaderCredentials::class) {
                name = "Private-Token"
                value = System.getenv().getOrDefault("CI_JOB_TOKEN", properties["labymedia_maven_auth_token"]) as String?
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
        mavenCentral()
    }
}

apply(plugin = "java-library")
apply(plugin = "net.flintmc.flint-gradle")

dependencies {
    "api"("net.flintmc", "flint-gradle-plugin", "2.1.3")
}

flint{
}

repositories {
    maven {
        setUrl("https://git.laby.tech/api/v4/groups/2/-/packages/maven")
        name = "Gitlab"
        credentials(HttpHeaderCredentials::class) {
            name = "Private-Token"
            value = System.getenv().getOrDefault("CI_JOB_TOKEN", properties["labymedia_maven_auth_token"]) as String?
        }
        authentication {
            create<HttpHeaderAuthentication>("header")
        }
    }
    mavenCentral()
}
