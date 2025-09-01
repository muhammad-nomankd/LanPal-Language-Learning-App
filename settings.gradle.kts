pluginManagement {
    repositories {
        maven {
            url = uri("https://androidx.dev/snapshots/builds/14016416/artifacts/repository")
        }
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
        maven { url = uri("https://androidx.dev/snapshots/builds/14016416/artifacts/repository/") }

    }
}

rootProject.name = "LangPal"
include(":app")
 