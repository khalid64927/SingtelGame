// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        //google()
        maven {
            url = uri("https://maven.google.com")
        }
        jcenter()
        maven {
            url = uri("https://maven.fabric.io/public")
        }
        maven{
            url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
        // Add the Google Services plugin (check for v3.1.2 or higher).
        classpath ("com.google.gms:google-services:4.3.3")
        // Add the Fabric Crashlytics plugin.
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.0.0-beta03")
    }


}

allprojects {
    repositories {
        google()
        jcenter()
        maven{
            url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
        }
    }

    configurations.all() {
        resolutionStrategy.force("org.antlr:antlr4-runtime:4.7.1")
        resolutionStrategy.force("org.antlr:antlr4-tool:4.7.1")
    }

}
