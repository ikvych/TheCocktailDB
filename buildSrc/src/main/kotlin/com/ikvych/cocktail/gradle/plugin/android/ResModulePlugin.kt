package com.ikvych.cocktail.gradle.plugin.android

import com.android.build.gradle.LibraryExtension
import com.ikvych.cocktail.gradle.plugin.android.base.BaseAndroidModulePlugin
import org.gradle.api.plugins.PluginContainer

@Suppress("unused") // user in build.gradle.kts
open class ResModulePlugin : BaseAndroidModulePlugin<LibraryExtension>() {

	override fun apply(plugins: PluginContainer): Unit = with(plugins) {
		apply("com.android.library")
	}
}