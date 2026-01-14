plugins {
    java
    id("com.gradleup.shadow") version "9.3.1"
}

group = "me.lukiiy.hytaleMP"
version = "1.0-SNAPSHOT"
description = "A small Hytale plugin."

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(files("lib/HytaleServer.jar"))
}

tasks {
    processResources {
        val props = mapOf(
            "group" to rootProject.group,
            "version" to rootProject.version,
            "description" to rootProject.description
        )

        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("manifest.json") {
            expand(props)
        }
    }

    shadowJar {
        dependsOn(processResources)
        archiveClassifier.set("")
        mergeServiceFiles()
        minimize()
    }

    jar { enabled = false }

    build { dependsOn("shadowJar") }
}


val targetJava = 25

java {
    val javaVersion = JavaVersion.toVersion(targetJava)

    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

    if (JavaVersion.current() < javaVersion) toolchain.languageVersion.set(JavaLanguageVersion.of(targetJava))
}