/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        val kotlin_repo_url: String? by settings
        logger.info("[COMMUNITY_PROJECT_BUILD] kotlin repository url: $kotlin_repo_url")
        kotlin_repo_url?.also { maven(it) }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        val kotlin_repo_url: String? by settings
        kotlin_repo_url?.also { maven(it) }
    }
    val kotlin_version: String? by settings
    val agp: String? by settings
    val ksp: String? by settings
    logger.info("[COMMUNITY_PROJECT_BUILD] kotlin version: $kotlin_version")
    versionCatalogs {
        create("libs") {
            kotlin_version?.also {
                version("kotlin", it)
            }
            agp?.also { version("androidGradlePlugin", it) }
            ksp?.also { version("ksp", it) }
        }
    }
}
rootProject.name = "nowinandroid"
include(":app")
include(":app-nia-catalog")
//include(":benchmarks")
include(":core:common")
include(":core:data")
include(":core:data-test")
include(":core:database")
include(":core:datastore")
include(":core:datastore-test")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:ui")
include(":core:testing")
include(":core:analytics")
include(":core:notifications")

include(":feature:foryou")
include(":feature:interests")
include(":feature:bookmarks")
include(":feature:topic")
include(":feature:search")
include(":feature:settings")
include(":lint")
include(":sync:work")
include(":sync:sync-test")
include(":ui-test-hilt-manifest")
