object Version {
	const val kotlin = "1.3.72"
	const val coroutines = "1.3.3"
	const val kodein = "6.5.5"

	object Plugin {
		const val android = "4.0.1"
		const val fabric = "1.31.2"
		const val crashlytics = "2.2.0"
	}

	object Network {
		const val retrofit = "2.9.0"
		const val gander = "3.1.0"
		const val loggingInterceptor = "4.5.0-RC1"
	}

	object Google {
		const val gson = "2.8.6"
		const val material = "1.1.0"
		const val autoService = "1.0-rc6"

		object PlayServices {
			const val base = "17.1.0"
			const val authApiPhone = "17.4.0"
		}
	}

	object AndroidX {
		const val core = "1.3.0"
		const val appCompat = "1.1.0"
		const val annotation = "1.1.0"
		const val fragment = "1.3.0-alpha06"
		const val recyclerView = "1.1.0"
		const val room = "2.2.5"
		const val coordinatorLayout = "1.1.0"
		const val workManager = "2.3.2"
		const val constraintlayout = "1.1.3"
		const val swipeRefreshLayout = "1.0.0"
		const val lifecycle = "2.2.0"
		const val preference = "1.1.0"
	}

	object Firebase {
		const val fcm = "20.2.4"
	}

	object Facebook {
		const val stetho = "1.5.0"
		const val shimmer = "0.5.0"
	}

	object Hyperion {
		const val hyperion = "0.9.27"
		const val hyperionAppInfo = "1.0.0"
		const val lynx = "1.6"
	}
}

object Lib {

	object Plugin {
		const val kotlin = "com.android.tools.build:gradle:${Version.Plugin.android}"
		const val android = "org.jetbrains.kotlin:kotlin-gradle-plugin${Version.kotlin}"
	}

	object Kotlin {
		const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$${Version.kotlin}"
		const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.kotlin}"
		const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
	}

	object AndroidX {
		const val coreKtx = "androidx.core:core-ktx:${Version.AndroidX.core}"
		const val appCompat = "androidx.appcompat:appcompat:${Version.AndroidX.appCompat}"
		const val annotation = "androidx.annotation:annotation:${Version.AndroidX.annotation}"
		const val fragment = "androidx.fragment:fragment:${Version.AndroidX.fragment}"
		const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Version.AndroidX.constraintlayout}"
		const val workManager = "androidx.work:work-runtime-ktx:${Version.AndroidX.workManager}"
		const val preferenceKtx = "androidx.preference:preference-ktx:${Version.AndroidX.preference}"
		const val swiperefreshlayout =
			"androidx.swiperefreshlayout:swiperefreshlayout:${Version.AndroidX.swipeRefreshLayout}"

		object Lifecycle {
			const val extensions = "androidx.lifecycle:lifecycle-extensions:${Version.AndroidX.lifecycle}"
			const val liveData = "androidx.lifecycle:lifecycle-livedata:${Version.AndroidX.lifecycle}"
			const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.AndroidX.lifecycle}"
			const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.AndroidX.lifecycle}"
		}

		object Room {
			const val common = "androidx.room:room-common:${Version.AndroidX.room}"
			const val ktx = "androidx.room:room-ktx:${Version.AndroidX.room}"
			const val runtime = "androidx.room:room-runtime:${Version.AndroidX.room}"
			const val compiler = "androidx.room:room-compiler:${Version.AndroidX.room}"
		}
	}

	object Google {
		const val gson = "com.google.code.gson:gson:${Version.Google.gson}"
		const val material = "com.google.android.material:material:${Version.Google.material}"
		const val autoService = "com.google.auto.service:auto-service:${Version.Google.autoService}"

		object PlayServices {
			const val base = "com.google.android.gms:play-services-base:${Version.Google.PlayServices.base}"
			const val authApiPhone =
				"com.google.android.gms:play-services-auth-api-phone:${Version.Google.PlayServices.authApiPhone}"
		}
	}

	object Network {
		object Gander {
			const val gander = "com.ashokvarma.android:gander:${Version.Network.gander}"
			const val persintance = "com.ashokvarma.android:gander-persistence:${Version.Network.gander}"
		}

		object Retrofit {
			const val retrofit = "com.squareup.retrofit2:retrofit:${Version.Network.retrofit}"
			const val converterGson = "com.squareup.retrofit2:converter-gson:${Version.Network.retrofit}"
		}

		object Interceptor {
			const val loggin = "com.squareup.okhttp3:logging-interceptor:${Version.Network.loggingInterceptor}"
		}
	}

	object Kodein {
		const val androidX = "org.kodein.di:kodein-di-framework-android-x:${Version.kodein}"
		const val androidCore = "org.kodein.di:kodein-di-framework-android-core:${Version.kodein}"
		const val generic = "org.kodein.di:kodein-di-generic-jvm:${Version.kodein}"
	}

	object Firebase {
		const val fcm = "com.google.firebase:firebase-messaging:${Version.Firebase.fcm}"
	}

	object Facebook {
		const val stetho = "com.facebook.stetho:stetho:${Version.Facebook.stetho}"
		const val shimmer = "com.facebook.shimmer:shimmer:${Version.Facebook.shimmer}"
	}

	object Hyperion {
		const val core = "com.willowtreeapps.hyperion:hyperion-core:${Version.Hyperion.hyperion}"
		const val attr = "com.willowtreeapps.hyperion:hyperion-attr:${Version.Hyperion.hyperion}"
		const val buildConfig = "com.willowtreeapps.hyperion:hyperion-build-config:${Version.Hyperion.hyperion}"
		const val disk = "com.willowtreeapps.hyperion:hyperion-disk:${Version.Hyperion.hyperion}"
		const val geigerCounter = "com.willowtreeapps.hyperion:hyperion-geiger-counter:${Version.Hyperion.hyperion}"
		const val measurement = "com.willowtreeapps.hyperion:hyperion-measurement:${Version.Hyperion.hyperion}"
		const val phoenix = "com.willowtreeapps.hyperion:hyperion-phoenix:${Version.Hyperion.hyperion}"
		const val preferences = "com.willowtreeapps.hyperion:hyperion-shared-preferences:${Version.Hyperion.hyperion}"
		const val plugin = "com.willowtreeapps.hyperion:hyperion-plugin:${Version.Hyperion.hyperion}"
		const val appinfo = "com.star_zero:hyperion-appinfo:${Version.Hyperion.hyperionAppInfo}"
		const val lynx = "com.github.pedrovgs:lynx:${Version.Hyperion.lynx}"
	}
}
