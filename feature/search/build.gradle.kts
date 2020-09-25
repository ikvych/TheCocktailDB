plugins {
    `feature-module`
}

dependencies {
    implementation (corePresentation)
    implementation (dataRepository)
    implementation (featureDetailApi)
    implementation (featureSearchApi)

    implementation (Lib.AndroidX.coreKtx)
}