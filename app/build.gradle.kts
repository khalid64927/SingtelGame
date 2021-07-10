import com.dependencies.Dependencies
plugins {
    id("com.khalid.hamid.KhalidAndroidPlugin")
}
System.out.println("after plugin")
kapt {
    correctErrorTypes = true
    javacOptions {
        // Increase the max count of errors from annotation processors.
        // Default is 100.
        option("-Xmaxerrs", 500)
    }
}


KPlugin {
    System.out.println("KPlugin Ext app...")
    isLibraryModule = false
    minSDK = 19
    compileSDK = "30"
    targetSDK = "30"
    versionCode = 10
    versionName = "1.1"
    testRunner = "com.khalid.hamid.githubrepos.utilities.AppTestRunner"
    lintBaseLineFilePath = "com.khalid.hamid.githubrepos.utilities.AppTestRunner"
    checkstylePath = "$rootDir/quality/checkstyle.xml"
    jacoco{
        excludes("app:testProdDebugUnitDebug","app:connectedProdDebugAndroidTest")
    }
}

spotless {
    kotlin {
        target ("**/*.kt")
        ktlint("0.35.0").userData(mapOf("disabled_rules" to "no-wildcard-imports"))
            licenseHeaderFile(project.rootProject.file("scripts/copyright.kt"))
    }
}

allOpen.annotation("com.khalid.hamid.githubrepos.testing.OpenClass")
android {
    System.out.println("android block...")
    compileSdkVersion(29)

    dataBinding {
        isEnabledForTests = true
        isEnabled = true
    }
    defaultConfig {
        applicationId = "com.khalid.hamid.singtelgame"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    flavorDimensions("default")
    productFlavors {
        create("mock") {
            applicationId = "com.khalid.hamid.singtelgame"
        }
        create("prod") {
            applicationId = "com.khalid.hamid.githubrepos"
        }
    }
}

dependencies {
    implementation(Dependencies.KOTLIN)
    implementation(Dependencies.V7)
    implementation(Dependencies.CONSTRAINT_LAYOUT)
    implementation(Dependencies.TIMBER)
    implementation(Dependencies.MULTIDEX)
    implementation(Dependencies.CRASH)
    implementation(Dependencies.CR_CORE)
    implementation(Dependencies.CR_ANDROID)


    implementation(Dependencies.DAGGER_RUNTIME)
    api(Dependencies.DAGGER_ANDROID)
    implementation(Dependencies.DAGGER_ANDROID_SUPPORT)
    kapt(Dependencies.DAGGER_ANDROID_PROCESSOR)
    kapt(Dependencies.DAGGER_COMPILER)
    // Lifecycle component
    implementation(Dependencies.LC_EXTENSION)
    implementation(Dependencies.LC_JAVA8)
    implementation("android.arch.lifecycle:common-java8:1.1.1")
    implementation(Dependencies.LC_RUNTIME)
    implementation(Dependencies.LD_KTX)
    kapt(Dependencies.LC_COMPILER)
    kapt(Dependencies.LC_VM_KTX)
    // Rx Java
    implementation(Dependencies.RX_ANDROID)
    // Room component
    implementation(Dependencies.ROOM_RUNTIME)
    implementation(Dependencies.ROOM_TESTING)
    kapt(Dependencies.ROOM_COMPILER)
    implementation(Dependencies.ROOM_KTX)

    implementation(Dependencies.RETROFIT_ADAPTER)
    implementation(Dependencies.RETROFIT_GSON_CONVERTER)
    implementation(Dependencies.RETROFIT_RUNTIME)

    implementation(Dependencies.OKHTTP)
    implementation(Dependencies.OKHTTP_INTERCEPTOR)

    implementation(Dependencies.ESP_IDL)

    implementation(Dependencies.FRAGMENTKTX)
    implementation(Dependencies.ACTIVITY_KTX)
    implementation(Dependencies.FRAGMENT_TESTING)
    implementation(Dependencies.TEST_CORE)
    // UI
    implementation(Dependencies.SHIMMER)
    implementation(Dependencies.SWIPEX)
    implementation(Dependencies.RECYCLER_VIEW)

    implementation(Dependencies.GOOGLE_MATERIAL){
        exclude(group = "androidx.recyclerview")
    }
    implementation(Dependencies.ANNOTATIONS)
    implementation(Dependencies.CARD_VIEW)
    // Image library
    implementation(Dependencies.GLIDE_RUNTIME)
    kapt(Dependencies.GLIDE_COMPILER)
    // Nav
    implementation(Dependencies.NAV_RUNTIME_FRAGMENT_KTX)
}

System.out.println("script end")

