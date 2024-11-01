// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.compose) apply false
}
buildscript {

  dependencies {
    classpath ("com.android.tools.build:gradle:8.7.1")
    classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.3")
    classpath ("com.google.gms:google-services:4.4.2")
    classpath ("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.20-1.0.14")


    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
  }
}
