package com.ikvych.cocktail.gradle.extension

import Config
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.*

fun Project.addKotlinStdLibDependency(configurationName: String = "implementation") {
	dependencies {
		add(configurationName, kotlin("stdlib", Version.kotlin))
	}
}

internal fun Project.setKotlinCompileOption(jvmTargetVersion: String = Config.jvmTarget) {
	project.tasks.withType(KotlinCompile::class.java).configureEach {
		kotlinOptions {
			jvmTarget = jvmTargetVersion
		}
	}
}

internal fun Project.getProperties(filePath: String) = Properties().apply {
	FileInputStream(rootProject.file(filePath)).use(::load)
}


