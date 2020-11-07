plugins {
    id("java-library")
}

group = "net.flintmc"



dependencies {
    annotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    internalAnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))
    v1_15_2AnnotationProcessor(project(":annotation-processing:annotation-processing-autoload"))

    api(project(":launcher"))
    api(project(":framework:framework-inject"))
    api(project(":util:util-csv"))

    api("org.javassist", "javassist", "3.27.0-GA")
    api("com.google.code.gson", "gson", "2.8.6")

}
