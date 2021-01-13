import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

object Versions {
    const val kotlin = "1.4.21"
    const val ktor = "1.4.1"
    const val coroutines = "1.4.2"
    const val kotlinxSerialization = "1.0.1"
    const val kotlinLogging = "2.0.4"
    const val kord = "0.7.0-SNAPSHOT"
}

object Dependencies {
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val kotlinxSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}"

    const val `ktor-io` = "io.ktor:ktor-io:${Versions.ktor}"
    const val `ktor-utils` = "io.ktor:ktor-utils:${Versions.ktor}"
    const val `ktor-client-websockets` = "io.ktor:ktor-client-websockets:${Versions.ktor}"
    const val `ktor-client-core` = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val `ktor-client-serialization` = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val `ktor-client-logging` = "io.ktor:ktor-client-logging:${Versions.ktor}"
    const val `ktor-client-cio` = "io.ktor:ktor-client-cio:${Versions.ktor}"
    const val `ktor-client-js` = "io.ktor:ktor-client-js:${Versions.ktor}"

    const val kotlinLogging = "io.github.microutils:kotlin-logging:${Versions.kotlinLogging}"

    const val kord = "dev.kord:kord-core:${Versions.kord}"
}

fun RepositoryHandler.jitpack() = maven("https://jitpack.io")
fun RepositoryHandler.kord() = maven("https://dl.bintray.com/kordlib/Kord")
fun RepositoryHandler.sonatype() = maven("https://oss.sonatype.org/content/repositories/snapshots/")