/*
 * Copyright 2014-2025 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package ktorbuild

import ktorbuild.internal.gradle.finalizedOnRead
import ktorbuild.targets.KtorTargets
import org.gradle.api.JavaVersion
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.internal.os.OperatingSystem
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import javax.inject.Inject

abstract class KtorBuildExtension(
    objects: ObjectFactory,
    providers: ProviderFactory,
    val targets: KtorTargets,
) {

    @Inject
    constructor(
        objects: ObjectFactory,
        providers: ProviderFactory,
    ) : this(objects, providers, targets = objects.newInstance())

    private val buildingOnTeamCity: Provider<Boolean> =
        providers.environmentVariable("TEAMCITY_VERSION").map(String::isNotBlank)

    val isCI: Provider<Boolean> =
        providers.environmentVariable("CI")
            .map(String::isNotBlank)
            .orElse(buildingOnTeamCity)
            .orElse(false)

    /**
     * The JDK version to be used to build the project.
     * By default, the minimal supported JDK version is used.
     */
    val jvmToolchain: Property<JavaLanguageVersion> =
        objects.property<JavaLanguageVersion>()
            .convention(DEFAULT_JDK)
            .finalizedOnRead()

    fun jvmToolchain(version: Int) {
        jvmToolchain.set(JavaLanguageVersion.of(version))
    }

    /**
     * The JDK version to be used for testing.
     *
     * The value is determined from the Gradle property "test.jdk".
     * If the property is not specified, it defaults to the current JDK used by Gradle.
     *
     * For example, to run tests against JDK 8, run a test task with flag "-Ptest.jdk=8"
     * or put this property to `gradle.properties`.
     */
    val jvmTestToolchain: Provider<JavaLanguageVersion> =
        providers.gradleProperty("test.jdk")
            .orElse(providers.provider { JavaVersion.current().majorVersion })
            .map(JavaLanguageVersion::of)

    /**
     * The Kotlin API version Ktor should be compatible with.
     *
     * DON'T change the property name as it is used in Kotlin Libraries train.
     */
    val kotlinApiVersion: Provider<KotlinVersion> =
        providers.gradleProperty("kotlin_api_version")
            .map(KotlinVersion::fromVersion)
            .orElse(DEFAULT_KOTLIN_API_VERSION)

    /**
     * The Kotlin Language version Ktor should be compatible with.
     *
     * DON'T change the property name as it is used in Kotlin Libraries train.
     */
    val kotlinLanguageVersion: Provider<KotlinVersion> =
        providers.gradleProperty("kotlin_language_version")
            .map(KotlinVersion::fromVersion)
            .orElse(DEFAULT_KOTLIN_LANGUAGE_VERSION)

    /** Host operating system. */
    val os: Provider<OperatingSystem> = providers.provider { OperatingSystem.current() }

    companion object {
        const val NAME = "ktorBuild"

        /** The default (minimal) JDK version used for building the project. */
        private val DEFAULT_JDK = JavaLanguageVersion.of(8)

        /** The default (minimal) Kotlin version used as API version. */
        private val DEFAULT_KOTLIN_API_VERSION = KotlinVersion.KOTLIN_2_0

        /** The default Kotlin version used as Language version. */
        private val DEFAULT_KOTLIN_LANGUAGE_VERSION = KotlinVersion.KOTLIN_2_1
    }
}
