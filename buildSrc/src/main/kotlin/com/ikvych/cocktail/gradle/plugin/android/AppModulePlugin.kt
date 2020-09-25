package com.ikvych.cocktail.gradle.plugin.android

import com.android.build.gradle.AppExtension
import com.ikvych.cocktail.gradle.plugin.android.base.BaseAndroidModulePlugin
import com.ikvych.cocktail.gradle.extension.addManifestPlaceholders
import com.ikvych.cocktail.gradle.extension.debug
import com.ikvych.cocktail.gradle.extension.release
import org.gradle.api.plugins.PluginContainer

@Suppress("unused") // user in build.gradle.kts
class AppModulePlugin : BaseAndroidModulePlugin<AppExtension>() {

	override fun apply(plugins: PluginContainer): Unit = with(plugins) {
		apply("com.android.application")
		apply("kotlin-android")
	}

	override fun configure(extension: AppExtension) = with(extension) {
		defaultConfig.applicationId = Config.applicationId
		dataBinding.isEnabled = true

		buildTypes {
			debug {
				applicationIdSuffix = ".${Config.debugSuffix}"
				versionNameSuffix = "-${Config.debugSuffix}"
				isShrinkResources = false

				addManifestPlaceholders("appName" to ".${Config.appName} ${Config.debugSuffix}")
			}
			release {
				isShrinkResources = Config.enableProguard
				isCrunchPngs = true
				isMinifyEnabled = true
				addManifestPlaceholders("appName" to Config.appName)
			}
		}
	}
}