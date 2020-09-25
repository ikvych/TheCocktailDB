plugins {
    `feature-module`
}

dependencies {
    implementation (corePresentation)
    implementation (dataRepository)
    implementation (featureMainCocktail)
    implementation (featureMainSetting)
    implementation (featureMainApi)
    implementation (featureDetailApi)
/*    implementation (platformFirebase)*/

    implementation (Lib.AndroidX.coreKtx)
}