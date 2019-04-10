pluginManagement {
    repositories {
        maven(url = "http://dl.bintray.com/kotlin/kotlin-eap")
        maven ("https://plugins.gradle.org/m2/")
        // mavenLocal()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.jetbrains.kotlin.jvm") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
            if (requested.id.id == "org.openjfx.javafx-plugin") {
                useModule("org.openjfx:javafx-plugin:${requested.version}")
            }
        }
    }
}

rootProject.name = "timetable"
