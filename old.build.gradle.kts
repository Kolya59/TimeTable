import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = "1.0-SNAPSHOT"

buildscript{
    repositories {
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.21"))
        classpath(kotlin("serialization", version = "1.3.20"))
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

plugins {
    val kotlinVersion = "1.3.21"
    java
    application
    kotlin("jvm") version kotlinVersion
    kotlin("gradle-plugin") version kotlinVersion
    //kotlin("serialization") version "1.3.20"
    id("com.jfrog.bintray") version "0.4.1"
}

/*compileKotlin {
    kotlinOptions.jvmTarget= 1.8
}*/

dependencies {
    compile("no.tornado:tornadofx:1.3.21")
    implementation(kotlin("stdlib-jdk8"))
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}