package com.ikvych.cocktail.gradle.plugin.kotlin

import com.ikvych.cocktail.gradle.extension.addKotlinStdLibDependency
import com.ikvych.cocktail.gradle.extension.setKotlinCompileOption
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class KotlinModulePlugin : Plugin<Project> {

	override fun apply(project: Project) = with(project) {
		plugins.apply("kotlin")

		setKotlinCompileOption()
		addKotlinStdLibDependency("api")
	}
}
