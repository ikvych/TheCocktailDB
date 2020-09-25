import Lib.AndroidX.coreKtx

plugins {
    `feature-module`
}

dependencies {
    implementation (corePresentation)
    implementation (dataRepository)
    implementation (featureDetailApi)
    implementation (coreKtx)
}