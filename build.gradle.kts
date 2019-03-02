import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin ("jvm") version "1.3.21"
    //id ("kotlin-multiplatform") version "1.3.20"
    id ("kotlinx-serialization") version "1.3.20" apply (true)
    id ("org.openjfx.javafxplugin") version "0.0.7"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile("org.jetbrains.kotlin:kotlin-stdlib")
    compile("org.jetbrains.kotlin:kotlin-serialization:1.3.20")
    compile("org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin:1.3.20")
    compile("org.openjfx.javafxplugin:0.0.7")

}

repositories {
    mavenCentral()
}

javafx {
    modules ( "javafx.controls", "javafx.fxml" )
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}