![Flint](.artwork/Flint_whitebg.png)

###### A modern Minecraft modding framework helping You to create maintainable Mods easily

---

![License](https://img.shields.io/badge/license-LGPL--3.0-blue)
![MC Version](https://img.shields.io/badge/Minecraft-1.15.2-9cf)
![MC Version](https://img.shields.io/badge/Minecraft-1.16.4-9cf)
[![Discord](https://img.shields.io/discord/784821338199556096.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/tPb9j3ZBXu)

Flint is a Minecraft modding framework created by LabyMedia GmbH. It is
developed with a focus on responsibility-driven design, interface segregation
and dependency injection. This allows for easy encapsulation of 
version-specific code. Flint makes heavy use of bytecode manipulation - 
Minecraft isn't patched directly - resulting in better compatibility between
Mods and a simpler update process.
 
**Warning**: Flint is still in development and not stable yet. We are
currently working with Minecraft 1.15.2 and 1.16.4, but will soon start to implement other
versions (including 1.8.9).
 
Contributions are welcome, just make sure to take a look at our contribution
guidelines first. We would also love to chat with you about Flint on our
[Discord Server](https://discord.gg/tPb9j3ZBXu). We always appreciate feedback.


### Table of Contents

1. [Motivation](#motivation)
2. [Examples](#examples) \
    2.1. [Listen on Events](#listen-on-events-provided-by-the-framework) \
    2.2. [Use Javassist to transform a Class](#use-javassist-to-transform-a-minecraft-class) \
    2.3. [Access private fields](#access-private-fields) \
    2.4. [Hook into Methods](#hook-into-arbitrary-methods)
3. [Architecture](#architecture) \
    3.1. [Dependency Injection](#dependency-injection) \
    3.2. [Binding Implementations](#binding-implementations) \
    3.3. [Separating version-specific Code](#separating-version-specific-code)
4. [Building  Flint](#building-flint)
5. [Creating a simple Mod](#creating-a-simple-mod) \
    5.1. [Project Setup](#project-setup) \
    5.2. [Create an Installer](#create-an-installer)
6. [Roadmap](#roadmap)   
7. [Further Resources](#further-resources)

## Motivation

Creating Minecraft modifications is often not as easy as it could be due to
several challenges.

- Version Independence is difficult to achieve.
- Modding APIs are not stable and do not provide enough features to implement
  the mod.
- Patching Minecraft directly leads to incompatibilities with other mods and is
  often not EULA compliant.
- Bytecode manipulation is difficult and results in code strongly coupled with
  the Minecraft version it was developed against, meaning it is tedious to port
  to other Minecraft versions.
- Distributing a mod with all its dependencies quickly results in a non-trivial
  installation process and tends to brick when the wrong version of the used
  modding framework is installed or dependency conflicts appear.

We learned from older projects and tried to approach these issues to make your
life as a mod creator as easy as possible.

- Flint helps you to encapsulate version-specific code properly and is able to
  bundle multiple implementations for different Minecraft versions into a
  single JAR file. Flint will then automatically load the correct
  implementation at runtime.
- Flint does not patch Minecraft directly and doesn't provide a way for you to
  do that. Instead, it is completely build using byte code manipulation at
  runtime.
- Bytecode manipulation with flint is easy. The annotation based class 
  transformers help you to use your favourite manipulation library while
  eradicating the need to worry about obfuscation. At the same time, Flint's
  architecture encourages you to properly encapsulate those class 
  transformations and reimplement them for each supported version.
- Flint's installer helps you to automatically resolve and install
  dependencies. Flint also makes sure your mod and all its dependencies are
  actually compatible with the given environment. As third-party maven
  dependencies aren't shaded into your JAR file, issues with libraries loaded
  more than once won't appear.


## Examples

Many things can already be accomplished using Flint's version independent
Minecraft API. There are however many tools provided to modify Minecraft even
further. A few examples are listed below.

#### Listen on Events provided by the Framework

For event listener, Flint uses the `@Subscribe` annotation. The event type will
be inferred from the method's parameter type.

```java
@Subscribe
public void onTick(TickEvent tickEvent) {
  if (tickEvent.getType() == TickEvent.Type.GENERAL)
    System.out.println("Tick!");
}
```

#### Use Javassist to transform a Minecraft class

Flint provides several ways for bytecode manipulation, including the popular
[Javassist](https://www.javassist.org/) library as well as
[objectweb ASM](https://asm.ow2.io/). Class  transformations can easily be
performed by annotating an appropriate Method with `@ClassTransform`.

```java
@ClassTransform("net.minecraft.client.Minecraft")
public void transformMinecraft(ClassTransformContext ctx) throws CannotCompileException {
  CtClass mcClass = ctx.getCtClass();
  mcClass.addMethod(CtMethod.make(
      "public void helloWorld() { System.out.println(\"Hello World!\"); }", mcClass));
}
```

#### Access private Fields

With the help of the `@Shadow` annotation, interfaces can be used to easily
access private fields or methods in Minecraft's classes.

```java
@Shadow("net.minecraft.client.Minecraft")
public interface MinecraftAccessor {
  
  @FieldGetter("gameDir")
  File getGameDir();
}
```

Instances of that class can then be casted to the interface type to access
fields and methods:

```java
File gameDir = ((MinecraftAccessor) Minecraft.getInstance()).getGameDir();
```

#### Hook into arbitrary Methods

Should the included events not suffice, a Mod can hook into arbitrary Minecraft
methods using the `@Hook` annotation.

```java
@Hook(
    className = "net.minecraft.client.Minecraft",
    methodName = "displayInGameMenu",
    parameters = {@Type(reference = boolean.class)},
    executionTime = Hook.ExecutionTime.BEFORE)
public void beforeInGameMenu(@Named("args") Object[] args) {
  System.out.println("Opening the in-game Menu...");
}
```

## Architecture

Flint is build using a responsibility driven approach and dependency injection.
For the latter, we currently use Google's Guice (the assisted factory
extensions is not compatible though, however, a custom alternative is
available). 

#### Dependency Injection

To access the tools of the framework you won't call static getter methods but
instead use constructor injection to retrieve the instance you need.

```java
@Singleton
public class ChatHandler {
  
  private final TaskExecutor executor;  

  @Inject
  private ChatHandler(TaskExecutor executor) {
    this.executor = executor;
  }

  @Subscribe
  public void onChat(ChatSendEvent event) {
    if (event.getMessage().equals("Remind me!")) {
      this.executor.scheduleSync(60 * 20, () -> {
        System.out.println("Reminder!");
      });
    }
  }
}
```

There is no need to manually register the event listener. An instance of the
class will be constructed automatically using Guice, an appropriate instance of
the `TaskExecutor` interface will be injected. For further explanations, see
the very comprehensive
[Guice documentation](https://github.com/google/guice/wiki).

#### Binding Implementations

When creating own interfaces and implementations, there is no need to manually
bind them together using a Guice module. Instead, the implementation should be
marked with the `@Implement` annotation, which also allows for version-specific
implementations.

Assume a simple interface whose implementation performs an action that requires
different implementations for different Minecraft versions.

```java
public interface StuffDoer {
  void doStuff();
}
```

Flint will then automatically bind the correct implementation for the running
version.

```java
@Implement(value = StuffDoer.class, version = "1.15.2")
public class VersionedStuffDoer implements StuffDoer {

  @Override
  public void doStuff() {
    // Implementation for Minecraft 1.15.2
  }
}
```

Abstracting your version-specific code like this results in a strong
encapsulation and helps you to write big parts of your Mod version
independently.

#### Separating version specific code

Each Flint module (as well as Mods build using our Gradle plugin) is split up
into multiple source sets.

The `main` source set should contain interfaces and classes that make up the
public API of the module. This API must always be completely version
independent. Minecraft classes can't be accessed directly.

The `internal` source set should contain version-independent implementations of
interfaces located in the `main` source set.

you can add a source set for every supported Minecraft version. These source
sets should contain version-specific implementations. Minecraft classes can be
accessed directly. The resulting symbolic references will be remapped
automatically at runtime to assure compatibility with both obfuscated and
de-obfuscated Minecraft installations. Class transformations and alike should
also go into these source sets.

## Building Flint

Building Flint is fairly easy. The project is set up using Gradle and a custom
Gradle plugin that manages source sets and dependencies. The Gradle plugin also
downloads, decompiles and de-obfuscates Minecraft when building for the first
time.

After cloning the repository, just run the `build` task:
```bash
$ ./gradlew build
```

There is also a task to start a de-obfuscated Minecraft directly out of your
development environment.
```bash
$ ./gradlew runClient1.16.4
```

If you want to login into your Minecraft account, just set the following
property in your global Gradle property file (`~/.gradle/gradle.properties`):
```properties
net.flintmc.gradle.login=true
```

When running the client, a login prompt will appear.

## Creating a simple Mod

Getting started with Flint is not difficult. The project setup doesn't require
deep Gradle knowledge and IDE support should be available without any extra
steps.

#### Project Setup

To create a simple Mod, the first step is to set up a new Gradle project. We
will use the following `build.gradle.kts`:

```kotlin
plugins {
    id("java-library")
    id("net.flintmc.flint-gradle")
}

repositories {
    mavenCentral()
}

group = "your.group"
version = "1.0.0"

flint {
    // Enter the newest Flint version here
    flintVersion = "2.0.12"
    minecraftVersions("1.15.2", "1.16.4")
    authors = arrayOf("Your Name")
    runs {
        overrideMainClass("net.flintmc.launcher.FlintLauncher")    
    }
}

dependencies {
    annotationProcessor(flintApi("annotation-processing-autoload"))
    internalAnnotationProcessor(flintApi("annotation-processing-autoload"))

    api(flintApi("framework-eventbus"))
    api(flintApi("framework-inject"))
    api(flintApi("mcapi"))
    api(flintApi("util-task-executor"))

    minecraft("1.15.2", "1.16.4") {
        annotationProcessor(flintApi("annotation-processing-autoload"))
    }
}
```

You will also need the following in your `settings.gradle.kts`, otherwise
Gradle won't find our custom Gradle plugin.

```kotlin
pluginManagement {
    plugins {
        // make sure to use the newest version
        id("net.flintmc.flint-gradle") version "2.7.2"
    }
    buildscript {
        dependencies {
            classpath("net.flintmc", "flint-gradle", "2.7.2")         
        }
        repositories {
            maven {
                setUrl("https://dist.labymod.net/api/v1/maven/release")
                name = "Flint"   
            }
            mavenCentral()
        } 
    }
}
```

You can then set up the source sets. The Gradle plugin expects the following
structure:

```
.
└── src
    ├── internal/java/your/group/internal
    ├── main/java/your/group
    └── v1_16_4/java/your/group/v1_16_4
```

If you now add the `ChatHandler` class from the 
[Dependency Injection](#dependency-injection) section, you will see that
`Reminder!` is printed to the log output one minute after you wrote
`Remind me!` into the chat.

#### Create an Installer

The Flint gradle plugin can automatically generate a simple JAR installer. It
will contain all the files needed by your mod and will also download and
install dependencies (including Flint itself) if not already installed.

To generate the installer, just run following task:

```bash
$ ./gradlew bundledInstallerJar
```

For more comprehensive examples and tutorials, go to our
[developer documentation](https://flintmc.net/docs). There you will also find
tutorials on how to publish your Mod to our distribution service.

## Roadmap

This project is not yet finished, there are many things we still want to do.

- [ ] Improve dependency resolution in package loading.
- [ ] Implement Minecraft 1.8.9 and other versions.
- [ ] Make it possible to create server mods.
- [ ] Write more documentation and create further resources on getting started.
- [ ] Improve Minecraft API.
- [ ] Move to yarn mappings.
- [ ] Make the gradle plugin not depend on MCP.
- [ ] Remove Guice and create an own injection framework for better performance
     and more control over class loading.
- [ ] Improve startup performance.

## Further Resources

- If you would like to contribute to Flint (or any of the related repositories)
  make sure to take a look at our contribution guidelines. There you will find
  information on coding and formatting standards we require as well as the
  process of code review.
- On [flintmc.net](https://flintmc.net), you will find a comprehensive
  documentation as well as many examples and tutorials.
- JavaDocs should automatically be available in your IDE of choice if you're
  including Flint as a Gradle/maven dependency. However, a hosted version can
  be found [here](https://javadocs.flintmc.net/).
- For more explanations regarding dependency injection, also refer to the
  [Guice documentation](https://github.com/google/guice/wiki).
- If you have any questions or feedback, feel free to join our
  [Discord Server](https://discord.gg/tPb9j3ZBXu) and talk to us directly.
- Should you notice any bugs or missing features, you can create an issue right
  here on GitHub.
  
---

NOT OFFICIAL MINECRAFT PRODUCT. NOT APPROVED BY OR ASSOCIATED WITH MOJANG.
