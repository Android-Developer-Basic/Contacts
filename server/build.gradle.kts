plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}

group = "ru.otus.contacts"
version = "1.0.0"
application {
    mainClass.set("ru.otus.contacts.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status)
    implementation(libs.ktor.server.contentType)
    implementation(libs.ktor.serialization.contentJson)
    implementation(libs.ktor.server.auth)
    implementation(libs.faker)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
}