/*
 * Copyright 2020 Mohammed Khalid Hamid.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dependencies

import com.android.build.gradle.api.BaseVariant
import com.android.builder.model.LintOptions
import org.gradle.api.Action
import org.gradle.api.DomainObjectSet
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.accessors.runtime.addDependencyTo
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import java.io.File

/**
 * @author Mohammed Khalid Hamid
*/

open class Utility {
    val implementation            = "implementation"
    val kapt                      = "kapt"
    val api                       = "api"
    val compileOnly               = "compileOnly"
    val testImplementation        = "testImplementation"
    val androidTestImplementation = "androidTestImplementation"


    lateinit var ext: KPluginExtensions

    open fun Project.applyPlugins(isApp : Boolean){

        if(isApp){
            apply(plugin = "com.android.application")
        } else {
            apply(plugin = "com.android.library")
            apply(plugin = "org.gradle.maven-publish")
        }
        apply(plugin = "kotlin-android")
        apply(plugin = "kotlin-android-extensions")
        apply(plugin = "kotlin-kapt")

        apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
        apply(plugin = "androidx.navigation.safeargs")
        apply(plugin = "com.diffplug.gradle.spotless")
    }

    private var lintExclusion = mutableListOf("ObsoleteLintCustomCheck", // ButterKnife will fix this in v9.0
        "IconExpectedSize",
        "InvalidPackage", // Firestore uses GRPC which makes lint mad
        "NewerVersionAvailable", "GradleDependency", // For reproducible builds
        "SelectableText", "SyntheticAccessor")

    fun Project.getLintBaseline() : File{
        val lintBaseLineFilePath = ext.lintBaseLineFilePath
        if(lintBaseLineFilePath.isEmpty()) return file("$rootDir/quality/lint-baseline.xml")
        return file(lintBaseLineFilePath)
    }

    fun LintOptions.disableLint(){
        System.out.println(" size is "+ext.lintExclusionRules.size)
        if(ext.lintExclusionRules.isEmpty()){
            lintExclusion.addAll(ext.lintExclusionRules)
        }
        System.out.println(" size is "+lintExclusion.size)
        try{
            lintExclusion.forEach {
                disable.add(it)
                severityOverrides.put(it, 5)
            }

        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    fun LintOptions.disableThis(rule : String){
        disable.add("")
        severityOverrides
    }
    fun DependencyHandler.unitTest() {
        testImplementation(Dependencies.JUNIT)
        testImplementation(Dependencies.JUNIT_EXT)
        testImplementation(Dependencies.MOCKWEBSERVER)
        testImplementation(Dependencies.ARCH_CORE_TESTING)
        testImplementation(Dependencies.ESPRESSO_CORE)
        testImplementation(Dependencies.ESP_CONTRIBUTE)
        testImplementation(Dependencies.ESP_INTENTS)
        testImplementation(Dependencies.TEST_KTX_CORE)
        testImplementation(Dependencies.TEST_KTX_JUNIT)
        testImplementation(Dependencies.TEST_RULES)
        testImplementation(Dependencies.MOKITO_CORE)
        testImplementation(Dependencies.MOKITO_ALL)
        testImplementation(Dependencies.MOKITO_INLINE)
        testImplementation(Dependencies.CR_TEST)
        testImplementation(Dependencies.MULTIDEXTEST)
        testImplementation(Dependencies.KLUENT)
        testImplementation(Dependencies.MOKITO_ANDROID)
        testImplementation(Dependencies.MOKITO_KOTLIN)
        testImplementation(Dependencies.CR_TEST_DEBUG)
        testImplementation(Dependencies.CR_TEST)

        //
        compileOnly(Dependencies.jdk9Deps)
        kapt(Dependencies.jaxbApi)
        kapt(Dependencies.jaxbCore)
        kapt(Dependencies.jaxbImpl)

    }

    /**
     * 1. KOTLIN
     * 2. V7
     * 3. CONSTRAINT LAYOUT
     * 4. TIMBER
     * 5. RETROFIT ADAPTER
     * 6. RETROFIT GSON CONVERTER
     * 7. RETROFIT RUNTIME
     * 8. OKHTTP
     * 9. OKHTTP INTERCEPTOR
     * 10. CARD VIEW
     * 11. GOOGLE MATERIAL
     * 12. ANNOTATIONS
     *
    */
    fun DependencyHandler.commonAndroidLibrariers(){
        implementation(Dependencies.KOTLIN)
        implementation(Dependencies.V7)
        implementation(Dependencies.CONSTRAINT_LAYOUT)
        implementation(Dependencies.TIMBER)
        implementation(Dependencies.SWIPEX)
        implementation(Dependencies.MULTIDEX)

        implementation(Dependencies.RETROFIT_ADAPTER)
        implementation(Dependencies.RETROFIT_GSON_CONVERTER)
        implementation(Dependencies.RETROFIT_RUNTIME)

        implementation(Dependencies.OKHTTP)
        implementation(Dependencies.OKHTTP_INTERCEPTOR)

        implementation(Dependencies.CARD_VIEW)
        implementation(Dependencies.GOOGLE_MATERIAL)
        implementation(Dependencies.ANNOTATIONS)
    }

    fun KaptExtension.configureKapt(){
        correctErrorTypes = true
        javacOptions {
            // Increase the max count of errors from annotation processors.
            // Default is 100.
            option("-Xmaxerrs", 500)
        }
    }

    fun Project.configureJacoco(
        project: Project,
        variants: DomainObjectSet<out BaseVariant>,
        options: JacocoOptions
    ) {
        System.out.println("configureJacoco 1")
        variants.all {
            val variantName = name
            System.out.println("configureJacoco 1$variantName")
            val isDebuggable = this.buildType.isDebuggable
            if (!isDebuggable) {
                project.logger.info("Skipping Jacoco for $name because it is not debuggable.")
                System.out.println("configureJacoco 2$isDebuggable")
                return@all
            }
            System.out.println("configureJacoco 33")
            project.tasks.register<JacocoReport>("jacoco${variantName.capitalize()}Report") {
                dependsOn(project.tasks["test${variantName.capitalize()}UnitTest"])
                val coverageSourceDirs = "src/main/java"
                System.out.println("configureJacoco 3")

                val javaClasses = project
                    .fileTree("${project.buildDir}/intermediates/javac/$variantName") {
                        setExcludes(options.excludes)
                    }

                val kotlinClasses = project
                    .fileTree("${project.buildDir}/tmp/kotlin-classes/$variantName") {
                        setExcludes(options.excludes)
                    }

                // Using the default Jacoco exec file output path.
                val execFile = "jacoco/test${variantName.capitalize()}UnitTest.exec"

                executionData.setFrom(
                    project.fileTree("${project.buildDir}") {
                        setIncludes(listOf(execFile))
                    }
                )

                // Do not run task if there's no execution data.
                setOnlyIf { executionData.files.any { it.exists() } }

                classDirectories.setFrom(javaClasses, kotlinClasses)
                sourceDirectories.setFrom(coverageSourceDirs)
                additionalSourceDirs.setFrom(coverageSourceDirs)

                reports.xml.isEnabled = true
                reports.html.isEnabled = true
                System.out.println("configureJacoco 4")
            }
        }
    }

    fun DependencyHandler.UITest(){
        androidTestImplementation(Dependencies.RECYCLER_VIEW)
        androidTestImplementation(Dependencies.CARD_VIEW)
        androidTestImplementation(Dependencies.GOOGLE_MATERIAL)
        androidTestImplementation(Dependencies.RECYCLER_VIEW)
        androidTestImplementation(Dependencies.GOOGLE_MATERIAL)

        androidTestImplementation(Dependencies.TEST_RULES)
        androidTestImplementation(Dependencies.JUNIT_EXT)
        androidTestImplementation(Dependencies.ARCH_CORE_TESTING)
        androidTestImplementation(Dependencies.MOKITO_CORE)
        androidTestImplementation(Dependencies.SWIPEX)
        androidTestImplementation(Dependencies.MULTIDEXTEST)



        addConfigurationWithExclusion("androidTestImplementation",Dependencies.ESPRESSO_CORE, {
            exclude(group = "com.android.support", module = "support-annotations")
            exclude(group = "com.google.code.findbugs", module = "jsr305")
        })
        addConfigurationWithExclusion("androidTestImplementation",Dependencies.MOKITO_CORE,
            { exclude(group = "net.bytebuddy") })
    }

    @Suppress("UNUSED_PARAMETER")
    fun DependencyHandler.dagger(){
        //implementation(Dependencies.DAGGER_RUNTIME)
        api(Dependencies.DAGGER_ANDROID)
        //implementation(Dependencies.DAGGER_ANDROID_SUPPORT)
        kapt(Dependencies.DAGGER_ANDROID_PROCESSOR)
    }

    @Suppress("UNUSED_PARAMETER")
    fun DependencyHandler.room(){
        implementation(Dependencies.ROOM_RUNTIME)
        implementation(Dependencies.ROOM_TESTING)
        kapt(Dependencies.ROOM_COMPILER)
        implementation(Dependencies.ROOM_KTX)
    }

    fun DependencyHandler.lifeCycle(){
        // Lifecycle component
        implementation(Dependencies.LC_EXTENSION)
        implementation(Dependencies.LC_JAVA8)
        implementation(Dependencies.LC_RUNTIME)
        kapt(Dependencies.LC_COMPILER)

    }

    fun DependencyHandler.fragment(){
        implementation(Dependencies.FRAGMENT_TESTING)
    }

    private fun DependencyHandler.implementation(dependencyName: String){
        addConfiguration(implementation,dependencyName)
    }

    private fun DependencyHandler.api(dependencyName: String){
        addConfiguration(api,dependencyName)
    }

    private fun DependencyHandler.compileOnly(dependencyName: String){
        addConfiguration(compileOnly,dependencyName)
    }

    private fun DependencyHandler.kapt(dependencyName: String){
        addConfiguration(kapt,dependencyName)
    }

    private fun DependencyHandler.testImplementation(dependencyName: String){
        addConfiguration(testImplementation,dependencyName)
    }

    private fun DependencyHandler.androidTestImplementation(dependencyName: String){
        addConfiguration(androidTestImplementation,dependencyName)
    }

    /**
     * Adds a dependency
     *
     * @param configurationName configuration to be set for this dependency
     * @param dependencyNotation notation for the dependency to be added.
     * @return The dependency.
     *
     * @see [DependencyHandler.add]
     */
    private fun DependencyHandler.addConfiguration(configurationName: String, dependencyNotation: Any): Dependency? =
        add(configurationName, dependencyNotation)


    /**
     * Adds a dependency
     *
     * @param configurationName configuration to be set for this dependency
     * @param dependencyNotation notation for the dependency to be added.
     * @param dependencyConfiguration expression to use to configure the dependency.
     * @return The dependency.
     *
     * @see [DependencyHandler.add]
     */
    private fun DependencyHandler.addConfigurationWithExclusion(
        configurationName: String,
        dependencyNotation: String,
        dependencyConfiguration: Action<ExternalModuleDependency>
    ): ExternalModuleDependency = addDependencyTo(
        this, configurationName, dependencyNotation, dependencyConfiguration
    ) as ExternalModuleDependency
}