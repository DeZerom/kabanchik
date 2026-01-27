rootProject.name = "kabanchik"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            @Suppress("UnstableApiUsage")
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

    includeBuild("build-logic")
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")

include(":common:ui-kit")
include(":common:network")
include(":common:store")
include(":common:tools")
include(":common:data-store")

include(":data:client:auth:logic")
include(":data:client:auth:model")
include(":domain:client:auth:logic")
include(":domain:client:auth:model")

include(":data:client:token:logic")
include(":domain:client:token:logic")
include(":domain:client:token:model")

include(":data:client:chat-details:logic")
include(":data:client:chat-details:model")
include(":domain:client:chat-details:logic")
include(":domain:client:chat-details:model")
include(":features:client:chat-details")