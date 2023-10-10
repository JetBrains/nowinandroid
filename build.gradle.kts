import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://androidx.dev/storage/compose-compiler/repository")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        val kotlin_repo_url: String? by project
        logger.info("[COMMUNITY_PROJECT_BUILD] kotlin repository url: $kotlin_repo_url")
        kotlin_repo_url?.also { maven(it) }

        // Android Build Server
        maven { url = uri("../nowinandroid-prebuilts/m2repository") }
    }
    dependencies {
        classpath(libs.google.oss.licenses.plugin) {
            exclude(group = "com.google.protobuf")
        }
    }
}

allprojects {
    val kotlin_version: String? by project
    kotlin_version?.also {
        configurations.all {
            resolutionStrategy.eachDependency {
                if (requested.group == "androidx.compose.compiler" && requested.name == "compiler") {
                    useTarget("org.jetbrains.kotlin.experimental.compose:compiler:$it")
                    because("Experimental developer version of compose")
                }
            }
        }
        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:" +
                        "suppressKotlinVersionCompatibilityCheck=$it",
                    "-Xmetadata-version=1.8" //WA for compatibility with Hilt see
                    // https://jetbrains.slack.com/archives/C04857B0BNE/p1688641699713959?thread_ts=1688639092.588919&cid=C04857B0BNE
                )
            }
        }
    }
}

// Lists all plugins used throughout the project without applying them.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.secrets) apply false
}