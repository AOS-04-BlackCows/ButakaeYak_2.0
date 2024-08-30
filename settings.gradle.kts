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
        // Kakao Map repository
        maven("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
        // Kakao SDK repository (if needed)
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}

rootProject.name = "YacTong"
include(":app")
 