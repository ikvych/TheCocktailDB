import Lib.AndroidX.coreKtx

plugins {
    `android-lib-module`
}

dependencies {
    implementation(coreCommon)
    implementation(coreKodein)
    implementation(dataNetwork)

    implementation(Lib.Kotlin.stdlib)
    implementation(coreKtx)
    implementation(Lib.Kotlin.coroutines)

    implementation(Lib.Google.gson)
    implementation(Lib.Network.Retrofit.retrofit)
    implementation(Lib.Network.Retrofit.converterGson)

    debugImplementation(Lib.Network.Gander.gander) //Should be changed to debugApi
    debugImplementation(Lib.Network.Interceptor.loggin) //Should be changed to debugApi
}