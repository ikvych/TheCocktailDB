import Lib.AndroidX.Lifecycle.liveDataKtx

plugins {
    `android-lib-module`
}

dependencies {
    implementation(liveDataKtx)
    api(Lib.Kotlin.coroutines)
}