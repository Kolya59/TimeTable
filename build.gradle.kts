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
    compile ("org.jetbrains.kotlin:kotlin-stdlib")
    compile ("org.jetbrains.kotlin:kotlin-serialization:1.3.20")
    //compile ("org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin")
    compile ("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.10.0")
    compile ("org.openjfx.javafx-plugin:0.0.7")
    compile ("no.tornado:tornadofx:1.7.18")
    compile ("lib:kotlin-serialization-1.3.20")
//    compile ("lib:javafx.base")
//    compile ("lib:javafx.controls")
//    compile ("lib:javafx.fxml")
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