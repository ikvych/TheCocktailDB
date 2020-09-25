import com.ikvych.cocktail.gradle.extension.debug
import com.ikvych.cocktail.gradle.extension.release
import com.ikvych.cocktail.gradle.extension.buildConfigField

plugins {
    `android-app-module`
}

android {
    buildTypes {
        debug {
            // api
/*            buildConfigField("API_URL_POSTMAN", "https://4a706c72-875e-420e-af29-65a2205bf421.mock.pstmn.io/")
            buildConfigField("API_URL_PROD", "https://4a706c72-875e-420e-af29-65a2205bf421.mock.pstmn.io/")*/
        }

        release {
            // api
/*            buildConfigField("API_URL_PROD", "https://4a706c72-875e-420e-af29-65a2205bf421.mock.pstmn.io/")*/
        }
    }
}

dependencies {
/*    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")*/

    implementation(coreKodein)
    implementation(corePresentation)
    implementation(coreStyling)
    implementation(coreCommon)

    implementation(dataDatabaseImpl)
    implementation(dataLocalPreference)
    implementation(dataNetworkImpl)
    implementation(dataRepositoryImpl)

    implementation(featureAuth)
    implementation(featureDetail)
    implementation(featureMain)
    implementation(featureMap)
    implementation(featureProfile)
    implementation(featureSearch)
    implementation(featureSplash)

    implementation(localization)

    implementation(platformFirebase)

    debugImplementation(Lib.Facebook.stetho)
}
