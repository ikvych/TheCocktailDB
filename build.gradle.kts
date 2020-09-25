import Lib.Plugin.android
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-android-extensions:${Version.kotlin}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {
    project.plugins.withType(Kapt3GradleSubplugin::class.java).whenPluginAdded {
        extensions.getByName<KaptExtension>("kapt").apply {
            mapDiagnosticLocations = true
            useBuildCache = true
        }
    }
}

tasks.register("clean", Delete::class) {
    project.allprojects.forEach {
        delete(it.buildDir)
    }
}