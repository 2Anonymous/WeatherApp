plugins {
    id(BuildPlugins.plugInApplication)
    id(BuildPlugins.plugInJetbrains)
    id (BuildPlugins.plugInKotlinKapt)
    id (BuildPlugins.plugInNavigationSafeargs)
    id (BuildPlugins.plugInHilt)
}

android {
    namespace = AppConfig.packageName
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.packageName
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile(AppConfig.proguardOptimize), AppConfig.proguardConsumerRules)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }

    buildFeatures {
        dataBinding = true
    }

    buildFeatures {
        buildConfig = true
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(Library.libCoreKtx)
    implementation(Library.libAppcompat)
    implementation(Library.libMaterial)
    implementation(Library.libConstraintlayout)
    testImplementation(Library.libJunit)
    androidTestImplementation(Library.libExtJunit)
    androidTestImplementation(Library.libEspresso)
    implementation(Library.libActivityKtx)
    implementation(Library.libFragmentKtx)
    /*Kotlin Coroutines*/
    implementation(Library.libCoroutinesCore)
    implementation(Library.libCoroutinesAndroid)
    /*ViewModel and LiveData*/
    implementation(Library.libConcurrentFutures)
    implementation(Library.libLifecycleExtensions)
    implementation(Library.libLifecycleRuntimeKtx)
    implementation(Library.libLifecycleLivedataKtx)
    implementation(Library.libLifecycleViewModelKtx)
    /*Networking*/
    implementation(Library.libOkhttp3)
    implementation(Library.libRetrofit)
    implementation(Library.libGsonConverter)
    implementation(Library.libInterceptor)
    /*Navigation*/
    implementation(Library.libNavigationUi)
    implementation(Library.libNavigationFragmentKtx)
    /*Dagger-hilt*/
    implementation(Library.libDaggerHilt)
    kapt(Library.libHiltCompiler)

    implementation(Library.libHiltWork)
    implementation(Library.libStartupRuntime)
    /*Preference Manager*/
    implementation(Library.libPreference)
    /**/
    implementation(Library.libSdp)
    implementation(Library.libSsp)
    /*Glide*/
    implementation(Library.libGlide)
    annotationProcessor(Library.libGlideCompiler)
    /*WorkManager*/
    implementation(Library.libWorkManager)

    implementation(Library.libLocation)
}