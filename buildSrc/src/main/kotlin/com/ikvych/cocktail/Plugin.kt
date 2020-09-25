@file:Suppress("ObjectPropertyName")

import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

inline val PluginDependenciesSpec.`android-app-module`: PluginDependencySpec
	get() = id("android-app-module")

inline val PluginDependenciesSpec.`android-res-module`: PluginDependencySpec
	get() = id("android-res-module")

inline val PluginDependenciesSpec.`android-lib-module`: PluginDependencySpec
	get() = id("android-lib-module")

inline val PluginDependenciesSpec.`feature-module`: PluginDependencySpec
	get() = id("feature-module")

inline val PluginDependenciesSpec.`kotlin-module`: PluginDependencySpec
	get() = id("kotlin-module")