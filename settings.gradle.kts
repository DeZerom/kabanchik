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
include(":proApp")

include(":common:ui-kit")
include(":common:network")
include(":common:store")
include(":common:tools")
include(":common:data-store")

// client
include(":data:client:auth:logic")
include(":data:client:auth:model")
include(":domain:client:auth:logic")
include(":domain:client:auth:model")
include(":features:client:auth")

include(":data:client:token:logic")
include(":domain:client:token:logic")
include(":domain:client:token:model")

include(":data:client:chat-details:logic")
include(":data:client:chat-details:model")
include(":domain:client:chat-details:logic")
include(":domain:client:chat-details:model")
include(":features:client:chat-details")

// pro
include(":data:pro:auth:logic")
include(":data:pro:auth:model")
include(":domain:pro:auth:logic")
include(":domain:pro:auth:model")
include(":features:pro:auth")

include(":data:pro:chat-details:logic")
include(":data:pro:chat-details:model")
include(":domain:pro:chat-details:logic")
include(":domain:pro:chat-details:model")
include(":features:pro:chat-details")