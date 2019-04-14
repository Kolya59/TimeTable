import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.openjfx:javafx-plugin:0.0.7")
    }
}
apply(plugin = "org.openjfx.javafxplugin")

plugins {
    kotlin ("jvm") version "1.3.20"
    id ("kotlinx-serialization") version "1.3.20" apply true
    id ("org.openjfx.javafxplugin") version "0.0.7"
}

dependencies {
    implementation( kotlin("stdlib-jdk8"))
    // Kotlin
    compile ("org.jetbrains.kotlin:kotlin-stdlib")
    compile ("org.jetbrains.kotlin:kotlin-serialization:1.3.20")
    //compile ("org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin")
    compile ("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.10.0")
    compile("lib:kotlin-serialization-1.3.20")
    // Java FX
    compile ("org.openjfx.javafx-plugin:0.0.7")
    // Tornado FX
    compile ("no.tornado:tornadofx:1.7.18")
    // Controls FX
//    compile ("org.controlsfx:controlsfx:8.0.5")
//    compile ("no.tornado:tornadofx-controlsfx:0.1")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven ("https://plugins.gradle.org/m2/" )
    flatDir{
        dirs ("lib")
    }
    flatDir{
        dirs ("/Users/kolya59/Yandex.Disk.localized/Универ/Курсовая/Timetable/lib/javafx-sdk-11.0.2/lib")
    }
    maven ("https://kotlin.bintray.com/kotlinx")
}

javafx {
    modules ( "javafx.controls", "javafx.fxml")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}