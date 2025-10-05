import java.util.Properties

val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(localFile.inputStream())
    }
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.acteam.vocago"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.acteam.vocago"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"${localProperties["BASE_URL"]}\"")
        buildConfigField("String", "GEMINI_API_KEY", "\"${localProperties["GEMINI_API_KEY"]}\"")
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${localProperties["GOOGLE_CLIENT_ID"]}\"")
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

}

dependencies {
    implementation(libs.stream.webrtc.android)
    //
    implementation(libs.androidx.browser)
    // Socket
    implementation(libs.socket.io.client)
    // Media
    implementation(libs.androidx.media)
    // TriggerX
    implementation(libs.triggerx)
    //audio
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    //camera
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.mlkit.vision)
    implementation(libs.text.recognition)
    //google
    implementation(libs.googleid)
    implementation(libs.androidx.credentials.play.services.auth)
    // paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    // ROOM
    implementation(libs.gson)
    implementation(libs.androidx.room.runtime)
    implementation(libs.generativeai.v060)
    implementation(libs.foundation)
    ksp(libs.androidx.room.compiler)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.guava)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.paging)

    implementation(libs.androidx.foundation)
    // DataStore
    implementation(libs.androidx.datastore.preferences)
    // Coin
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    // Credentials
    implementation(libs.androidx.credentials)
    // Ktor client
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    // Koin
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)
    //Lottie
    implementation(libs.android.lottie.compose)
    implementation(libs.introshowcaseview)
    implementation(libs.accompanist.flowlayout.v0360)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}