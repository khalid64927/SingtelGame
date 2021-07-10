package com.dependencies

import org.gradle.api.Action

open class KPluginExtensions {

    var modules = listOf<String>()
    var isLibraryModule = false
    var exclusions = ""
    var compileSDK = "30"
    var buildTools = "30.0.3"
    var targetSDK = "30"
    var minSDK : Int = 21
    var versionCode : Int = 10
    var versionName = "1.1"
    var testRunner = "com.khalid.hamid.githubrepos.utilities.AppTestRunner"
    var lintBaseLineFilePath = ""
    var lintExclusionRules : List<String> = emptyList()
    var checkstylePath = ""


    open val jacoco: JacocoOptions = JacocoOptions()
    open fun jacoco(action: Action<JacocoOptions>) {
        action.execute(jacoco)
    }
}

open class JacocoOptions {
    open var isEnabled: Boolean = true

    open var excludes: ArrayList<String> = arrayListOf()
    open var dependentTasklist: ArrayList<String> = arrayListOf()
    open fun excludes(vararg excludes: String) {
        this.excludes.addAll(excludes)
    }
    open fun dependsOnTasks(vararg dependentTasks : String){
        this.dependentTasklist.addAll(dependentTasks)
    }
}