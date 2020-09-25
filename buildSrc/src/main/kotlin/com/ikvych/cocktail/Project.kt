import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

inline val DependencyHandler.app: ProjectDependency
    get() = project(":app")

inline val DependencyHandler.hyperion: ProjectDependency
    get() = project(":hyperion")

inline val DependencyHandler.localization: ProjectDependency
    get() = project(":localization")

// core
inline val DependencyHandler.coreCommon: ProjectDependency
    get() = project(":core:common")

inline val DependencyHandler.coreKodein: ProjectDependency
    get() = project(":core:kodein")

inline val DependencyHandler.corePresentation: ProjectDependency
    get() = project(":core:presentation")

inline val DependencyHandler.coreStyling: ProjectDependency
    get() = project(":core:styling")

// data
inline val DependencyHandler.dataNetwork: ProjectDependency
    get() = project(":data:network")

inline val DependencyHandler.dataNetworkImpl: ProjectDependency
    get() = project(":data:network:impl")

inline val DependencyHandler.dataDatabase: ProjectDependency
    get() = project(":data:database")

inline val DependencyHandler.dataDatabaseImpl: ProjectDependency
    get() = project(":data:database:impl")

inline val DependencyHandler.dataLocal: ProjectDependency
    get() = project(":data:locale")

inline val DependencyHandler.dataLocalPreference: ProjectDependency
    get() = project(":data:locale:preference")

inline val DependencyHandler.dataRepository: ProjectDependency
    get() = project(":data:repository")

inline val DependencyHandler.dataRepositoryImpl: ProjectDependency
    get() = project(":data:repository:impl")

// platform
inline val DependencyHandler.platformFirebase: ProjectDependency
    get() = project(":platform:firebase")

// feature
inline val DependencyHandler.featureSplash: ProjectDependency
    get() = project(":feature:splash")

inline val DependencyHandler.featureSplashApi: ProjectDependency
    get() = project(":feature:splash:api")

inline val DependencyHandler.featureAuth: ProjectDependency
    get() = project(":feature:auth")

inline val DependencyHandler.featureAuthApi: ProjectDependency
    get() = project(":feature:auth:api")

inline val DependencyHandler.featureDetail: ProjectDependency
    get() = project(":feature:detail")

inline val DependencyHandler.featureDetailApi: ProjectDependency
    get() = project(":feature:detail:api")

inline val DependencyHandler.featureMain: ProjectDependency
    get() = project(":feature:main")

inline val DependencyHandler.featureMainApi: ProjectDependency
    get() = project(":feature:main:api")

inline val DependencyHandler.featureMainSetting: ProjectDependency
    get() = project(":feature:main:setting")

inline val DependencyHandler.featureMainCocktail: ProjectDependency
    get() = project(":feature:main:cocktail")

inline val DependencyHandler.featureMainCocktailFilter: ProjectDependency
    get() = project(":feature:main:cocktail:filter")

inline val DependencyHandler.featureMap: ProjectDependency
    get() = project(":feature:map")

inline val DependencyHandler.featureProfile: ProjectDependency
	get() = project(":feature:profile")

inline val DependencyHandler.featureProfileApi: ProjectDependency
	get() = project(":feature:profile:api")

inline val DependencyHandler.featureSearch: ProjectDependency
	get() = project(":feature:search")

inline val DependencyHandler.featureSearchApi: ProjectDependency
    get() = project(":feature:search:api")


