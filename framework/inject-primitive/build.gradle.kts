plugins {
    id("java-library")
}

group = "net.flintmc"

flint.configureNow()

dependencies {

    api("com.google.inject", "guice", "4.2.0")
    api("com.google.inject.extensions", "guice-assistedinject", "4.2.0")

}
