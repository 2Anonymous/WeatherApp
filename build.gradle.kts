// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(BuildPlugins.plugInApplication) version Versions.gradle apply false
    id(BuildPlugins.plugInJetbrains) version Versions.kotlin apply false
    id (BuildPlugins.plugInKotlinkapt) version Versions.kapt apply false
    id (BuildPlugins.plugInHilt) version Versions.hiltDI apply false
    id (BuildPlugins.plugInNavigation) version Versions.navigationPlugin apply false
}