pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Slots"
include(":app")
include(":feature:dashboard")
include(":feature:login")
include(":feature:registration")
include(":feature:booking")
include(":core:model")
include(":core:data")
include(":core:datastore")
include(":core:database")
include(":core:designsystem")
include(":core:network")
include(":core:ui")
include(":feature:home")
