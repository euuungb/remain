plugins {
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.easternkite.remain"
version = "0.0.1"

application {
    mainClass.set("com.easternkite.remain.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

// JVM 타겟 버전 설정
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.contentNegotiation)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.contentNegotiation)

    implementation(libs.ktor.network)
    implementation(libs.ktor.serialization.json)
    implementation(libs.generativeAi)

    testImplementation(libs.ktor.server.test.jvm)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlin.datetime)
}
