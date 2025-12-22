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
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://storage.googleapis.com/download.flutter.io")

        //Below for with AAR file
        maven {
            url = uri("app/libs/repo")
        }

        //Below with remote hosted flutter module
        //maven("https://raw.githubusercontent.com/dhaiyurpatel/flutter_module/main/docs")
    }

}

rootProject.name = "POCFlutterToAndroid"
include(":app")
