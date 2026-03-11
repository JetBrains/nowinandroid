import com.google.devtools.ksp.gradle.KspAATask
import com.google.devtools.ksp.gradle.model.Ksp
import org.gradle.kotlin.dsl.support.kotlinCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

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
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()

        // This is used only for internal Google builds.
        maven { url = uri("../nowinandroid-prebuilts/m2repository") }
        maven("https://androidx.dev/storage/compose-compiler/repository")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        val kotlin_repo_url: String? by project
        logger.info("Custom kotlin repository url: $kotlin_repo_url")
        kotlin_repo_url?.also { maven(it) }
    }
}
allprojects {

    val kotlin_language_version: String? by project
    val kotlin_api_version: String? by project
    val kotlin_additional_cli_options: String? by project

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.add("-Xannotation-default-target=param-property")
            freeCompilerArgs.add("-Xmetadata-version=2.3.0")
        }
        kotlin_additional_cli_options?.let {
            compilerOptions {
                logger.info("Additional kotlin compiler options: \'$it\'")
                freeCompilerArgs.addAll(
                    it.trim().split(" "),
                )
            }
        }

        kotlin_language_version?.let {
            logger.info("Custom kotlin language version: $it")
            compilerOptions {
                languageVersion.set(KotlinVersion.fromVersion(it))
            }
        }
        kotlin_api_version?.let {
            logger.info("Custom kotlin api version: $it")
            compilerOptions {
                apiVersion.set(KotlinVersion.fromVersion(it))
            }
        }
    }
}

/*
 * By listing all the plugins used throughout all subprojects in the root project build script, it
 * ensures that the build script classpath remains the same for all projects. This avoids potential
 * problems with mismatching versions of transitive plugin dependencies. A subproject that applies
 * an unlisted plugin will have that plugin and its dependencies _appended_ to the classpath, not
 * replacing pre-existing dependencies.
 */
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.dependencyGuard) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.google.osslicenses) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.nowinandroid.root)
}
