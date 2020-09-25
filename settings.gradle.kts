rootProject.name = "TheCocktailD"
include(":app")

include(":platform")
include(":platform:firebase")

include(":feature")
include(":feature:map")
include(":feature:profile")
include(":feature:main:cocktail:filter")
include(":feature:search")
include(":feature:detail")
include(":feature:main:setting")
include(":feature:main:cocktail")
include(":feature:main")
include(":feature:splash")
include(":feature:auth")
include(":feature:search:api")
include(":feature:profile:api")
include(":feature:detail:api")
include(":feature:auth:api")
include(":feature:main:api")

include(":core")
include(":core:kodein")
include(":core:presentation")
include(":core:styling")
include(":core:common")

include(":data")
include(":data:database:impl")
include(":data:database")
include(":data:locale:preference")
include(":data:locale")
include(":data:network:impl")
include(":data:network")
include(":data:repository:impl")
include(":data:repository")

include(":localization")
