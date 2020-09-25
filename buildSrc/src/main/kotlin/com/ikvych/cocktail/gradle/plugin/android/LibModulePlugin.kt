package com.ikvych.cocktail.gradle.plugin.android

import com.android.build.gradle.LibraryExtension
import org.gradle.api.plugins.PluginContainer

@Suppress("unused") // user in build.gradle.kts
open class LibModulePlugin : ResModulePlugin() {

	override fun apply(plugins: PluginContainer): Unit = with(plugins) {
		super.apply(plugins)
		apply("kotlin-android")
		apply("kotlin-android-extensions")
	}
}