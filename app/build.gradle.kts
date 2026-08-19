import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

val properties =
    Properties().apply {
        load(project.rootProject.file("local.properties").inputStream())
    }

// 릴리즈 서명 정보는 local.properties 에서 읽는다.
// 키스토어(*.jks)와 local.properties 는 .gitignore 로 막혀 있어 저장소에 올라가지 않는다.
// CI 나 갓 클론한 환경처럼 키스토어가 없는 곳에서도 debug 빌드는 그대로 되어야 하므로,
// 경로가 지정된 경우에만 서명 설정을 만든다.
val releaseKeystorePath: String? = properties.getProperty("release.keystore.path")
val releaseKeystore = releaseKeystorePath?.let { rootProject.file(it) }

// 경로를 적어 두고 파일이 없는 건 오타일 가능성이 높으므로 조용히 넘기지 않는다.
if (releaseKeystorePath != null && releaseKeystore?.exists() != true) {
    error("release.keystore.path 가 가리키는 키스토어를 찾을 수 없습니다: $releaseKeystorePath")
}

val hasReleaseSigning = releaseKeystore != null

android {
    namespace = "com.flint.android"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.flint.android"
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.targetSdk
                .get()
                .toInt()
        versionCode =
            libs.versions.versionCode
                .get()
                .toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", properties.getProperty("base.url"))

        val kakaoNativeAppKey = properties["kakao.native.app.key"].toString()
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeAppKey\"")
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = kakaoNativeAppKey
    }

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = releaseKeystore
                storePassword = properties.getProperty("release.keystore.password")
                keyAlias = properties.getProperty("release.key.alias")
                keyPassword = properties.getProperty("release.key.password")
            }
        }
    }

    buildTypes {
        release {
            if (hasReleaseSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
    // Androidx
    implementation(libs.bundles.androidx.core)
    implementation(libs.androidx.datastore.preferences)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    // Kotlinx
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.immutable)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    // Network
    implementation(libs.bundles.network)

    // DI
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    // Debug
    debugImplementation(libs.bundles.debug)

    // Kakao
    implementation(libs.kakao.user)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.timber)
    implementation(libs.lottie.compose)
    implementation(libs.pebble)
}
