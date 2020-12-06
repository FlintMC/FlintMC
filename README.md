![Flint](.artwork/Flint_whitebg.png)

###### A modern Minecraft modding framework helping You to create maintainable Mods easily

---

![License](https://img.shields.io/badge/license-LGPL--3.0-blue)
![MC Version](https://img.shields.io/badge/Minecraft-1.15.2-9cf)
[![Discord](https://img.shields.io/discord/784821338199556096.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/tPb9j3ZBXu)

Flint is a Minecraft modding framework created by LabyMedia GmbH. It is
developed with a focus on responsibility-driven design, interface segregation
and dependency injection. This allows for easy encapsulation of 
version-specific code. Flint makes heavy use of bytecode manipulation - 
Minecraft isn't patched directly - resulting in better compatibility between
Mods and a simpler update process.
 
**Warning**: Flint is still in development and not stable yet. We are
currently working with Minecraft 1.15.2, but will soon start to implement other
versions (including 1.16 and 1.8).
 
Contributions are welcome, just make sure to take a look at our contribution
guidelines first. We would also love to chat with You about Flint on our
[Discord Server](https://discord.gg/tPb9j3ZBXu). We always appreciate feedback.


### Table of Contents

1. [Examples](#examples) \
    1.1. [Listen on Events](#listen-on-events-provided-by-the-framework) \
    1.2. [Use Javassist to transform a Class](#use-javassist-to-transform-a-minecraft-class) \
    1.3. [Access private fields](#access-private-fields) \
    1.4. [Hook into Methods](#hook-into-arbitrary-methods)
2. [Architecture](#architecture) \
    2.1. [Dependency Injection](#dependency-injection) \
    2.2. [Binding Implementations](#binding-implementations) \
    2.3. [Separating version-specific Code](#separating-version-specific-code)
3. [Building  Flint](#building-flint)
4. [Creating a simple Mod](#creating-a-simple-mod)
5. [Further Resources](#further-resources)

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
      "public void helloWorld() { System.out.println(\\\"Hello World!\\\"); }", mcClass));
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

To access the tools of the framework You won't call static getter methods but
instead use constructor injection to retrieve the instance You need.

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
public class VersionedStuffDoer {

  @Override
  public void doStuff() {
    // Implementation for Minecraft 1.15.2
  }
}
```

Abstracting Your version-specific code like this results in a strong
encapsulation and helps You to write big parts of Your Mod version
independently.

#### Separating version specific code

Each Flint module (as well as Mods build using our Gradle plugin) is split up
into multiple source sets.

The `main` source set should contain interfaces and classes that make up the
public API of the module. This API must always be completely version
independent. Minecraft classes can't be accessed directly.

The `internal` source set should contain version-independent implementations of
interfaces located in the `main` source set.

You can add a source set for every supported Minecraft version. These source
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
```
$ ./gradlew build
```

There is also a task to start a de-obfuscated Minecraft directly out of Your
development environment.
```
$ ./gradlew runClient1.15.2
```

If You want to login into Your Minecraft account, just set the following
property in Your global Gradle property file (`~/.gradle/gradle.properties`):
```properties
net.flint.gradle.login=true
```

When running the client, a login prompt will appear.

## Creating a simple Mod

To create a simple Mod, the first step is to set up a new Gradle project. We
will use the following `build.gradle.kts`:

```kotlin
plugins {
    id("java-library")
    id("net.flintmc.flint-gradle-plugin")
}

repositories {
    maven {
        setUrl("https://dist.labymod.net/api/v1/maven/release")
        name = "Flint"   
    }
    mavenCentral()
}

group = "you.group"
version = "1.0.0"

var depFlintVersion = "1.1.0" // enter the newest Flint version here

flint {
    flintVersion = depFlintVersion
    minecraftVersions("1.15.2")
    authors = arrayOf("Your Name")
    runs {
        overrideMainClass("net.flintmc.launcher.FlintLauncher")    
    }
}

dependencies {
    annotationProcessor("net.flintmc", "annotation-processing-autoload", depFlintVersion)
    internalAnnotationProcessor("net.flintmc", "annotation-processing-autoload", depFlintVersion)
    v1_15_2AnnotationProcessor("net.flintmc", "annotation-processing-autoload", depFlintVersion)

    api("net.flintmc", "framework-eventbus", depFlintVersion)
    api("net.flintmc", "framework-inject", depFlintVersion)
    api("net.flintmc", "mcapi", depFlintVersion)
    api("net.flintmc", "util-task-executor", depFlintVersion)
}
```

You will also need the following in Your `settings.gradle.kts`, otherwise
Gradle won't find out custom Gradle plugin.

```kotlin
pluginManagement {
    plugins {
        id("net.flintmc.flint-gradle-plugin") version "2.5.8" // make sure to use the newest version
    }
    buildscript {
        dependencies {
            classpath("net.flintmc", "flint-gradle-plugin", "2.5.8")         
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
    └── v1_15_2/java/your/group/v1_15_2
```

If You now add the `ChatHandler` class from the 
[Dependency Injection](#dependency-injection) section, You will see that
`Reminder!` is printed to the log output one minute after You wrote
`Remind me!` into the chat.

For more comprehensive examples and tutorials, go to our
[developer documentation](https://flintmc.net/docs). There You will also find
tutorials on how to publish and install Your Mod.

## Further Resources

- If You would like to contribute to Flint (or any of the related repositories)
  make sure to take a look at our contribution guidelines. There You will find
  information on coding and formatting standards we require as well as the
  process of code review.
- On [flintmc.net](https://flintmc.net), You will find a comprehensive
  documentation as well as many examples and tutorials.
- JavaDocs should automatically be available in Your IDE of choice if You're
  including Flint as a Gradle/maven dependency. However, a hosted version can
  be found [here](https://flintmc.net/javadocs).
- For more explanations regarding dependency injection, also refer to the
  [Guice documentation](https://github.com/google/guice/wiki).
- If You have any questions or feedback, feel free to join our
  [Discord Server](https://discord.gg/tPb9j3ZBXu) and ask us directly.
- Should You notice any bugs or missing features, You can create an issue right
  here on GitHub.

