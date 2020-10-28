rootProject.name = "flint"

//defineModule("annotation-processing:autoload")
//defineModule("annotation-processing:build.gradle.bak")
//
//defineModule("framework:eventbus")
//defineModule("framework:inject")
//defineModule("framework:inject-primitive")
//defineModule("framework:packages")
//defineModule("framework:service")
//defineModule("framework:stereotype")
//defineModule("framework:tasks")
//
//defineModule("render:gui")
//defineModule("render:shader")
//defineModule("render:vbo-rendering")
//defineModule("render:webgui")
//
//defineModule("transform:asm")
//defineModule("transform:exceptions")
//defineModule("transform:hook")
//defineModule("transform:javassist")
//defineModule("transform:launcher-plugin")
//defineModule("transform:minecraft")
//defineModule("transform:minecraft-obfuscator")
//defineModule("transform:shadow")
//
//defineModule("util:commons")
//defineModule("util:csv")
//defineModule("util:i18n")
//defineModule("util:mapping")
//defineModule("util:session-service")

fun defineModule(path: String) {
    include(path)
    findProject(":$path")?.name = path.replace(":", "-")
}
