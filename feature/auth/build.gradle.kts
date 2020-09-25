plugins {
    `feature-module`
}

dependencies {
    implementation (featureAuthApi)
    implementation (corePresentation)
    implementation (featureMainApi)
    implementation (dataRepository)

    implementation (Lib.AndroidX.coreKtx)
}