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
include(":common:error-handler:logic")

// common
include(":data:common:token:logic")
include(":domain:common:token:logic")
include(":domain:common:token:model")

include(":domain:common:user:logic")
include(":data:common:user:logic")

include(":domain:common:auth:logic")

include(":data:common:chat:logic")
include(":data:common:chat:model")
include(":domain:common:chat:logic")
include(":domain:common:chat:model")
include(":features:common:chat:logic")
include(":features:common:chat:model")

// client
include(":data:client:auth:logic")
include(":data:client:auth:model")
include(":domain:client:auth:logic")
include(":domain:client:auth:model")
include(":features:client:auth")

include(":data:client:chat:logic")
include(":data:client:chat:model")
include(":domain:client:chat:logic")
include(":domain:client:chat:model")
include(":features:client:chat")

// pro
include(":data:pro:auth:logic")
include(":data:pro:auth:model")
include(":domain:pro:auth:logic")
include(":domain:pro:auth:model")
include(":features:pro:auth")

include(":data:pro:chat:logic")
include(":data:pro:chat:model")
include(":domain:pro:chat:logic")
include(":domain:pro:chat:model")
include(":features:pro:chat")