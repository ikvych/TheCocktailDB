import Lib.AndroidX.Lifecycle.liveData
import Lib.AndroidX.Lifecycle.liveDataKtx

plugins {
    `android-lib-module`
}

dependencies {
    api(Lib.Kotlin.coroutines)
    implementation(liveData)
    implementation(liveDataKtx)
    implementation(Lib.AndroidX.Room.common)
}