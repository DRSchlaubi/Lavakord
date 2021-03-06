import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.jfrog.bintray.gradle.BintrayExtension

plugins {
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
    id("com.jfrog.bintray") version "1.8.5"
    id("org.jetbrains.dokka") version "1.4.10"
    `maven-publish`
}

group = "me.schlaubi"
version = "0.3"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://dl.bintray.com/kordlib/Kord")
    maven("https://kotlin.bintray.com/kotlinx/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    jcenter()
}

dependencies {
    implementation(kotlin("reflect"))

    api("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.0.0")
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.4.1")

    api("io.ktor", "ktor-io", "1.4.1")
    api("io.ktor", "ktor-utils", "1.4.1")
    api("io.ktor", "ktor-client-websockets", "1.4.1")
    api("io.ktor", "ktor-client", "1.4.1")
    api("io.ktor", "ktor-client-logging", "1.4.1")

    compileOnly("dev.kord", "kord-core", "0.7.0-SNAPSHOT")
    compileOnly("com.gitlab.kordlib.kord:kord-core:0.6.10") // legacy
    api("com.github.FredBoat", "Lavalink-Client", "4.0") // legacy

    api("io.github.microutils", "kotlin-logging", "2.0.4")

    testImplementation(kotlin("test-junit"))
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    val sourcesJar = task<Jar>("sourcesJar") {
        dependsOn(classes)
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }


    val javadocJar = task<Jar>("javadocJar") {
        dependsOn(dokkaHtml)
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        archiveClassifier.set("javadoc")
        from(dokkaHtml)
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(javaComponent)
                artifact(sourcesJar)
                artifact(javadocJar)

                pom {
                    name.set(project.name)
                    description.set("Extension of the official LavaLink-Client to work with Kord")
                    url.set("https://github.com/DRSchlaubi/lavakord")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://github.com/DRSchlaubi/Lavakord/blob/master/LICENSE")
                        }
                    }

                    developers {
                        developer {
                            name.set("Michael Rittmeister")
                            email.set("mail@schlaubi.me")
                            organizationUrl.set("https://michael.rittmeister.in")
                        }
                    }

                    scm {
                        connection.set("scm:git:https://github.com/DRSchlaubi/lavakord.git")
                        developerConnection.set("scm:git:https://github.com/DRSchlaubi/lavakord.git")
                        url.set("https://github.com/DRSchlaubi/lavakord")
                    }
                }
            }
        }
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    dokkaHtml {
        outputDirectory.set(file("docs/"))

        dokkaSourceSets {
            configureEach {
                includeNonPublic.set(false)

                jdkVersion.set(8)
            }
        }
    }
}

kotlin {
    explicitApi()
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("mavenJava")
    pkg {
        repo = "maven"
        name = "lavakord"
        setLicenses("MIT")
        vcsUrl = "https://github.com/DRSchlaubi/lavakord.git"
        version {
            name = project.version as String
            gpg {
                sign = true
                passphrase = System.getenv("GPG_PASS")
            }
        }
    }
}


fun BintrayExtension.pkg(block: BintrayExtension.PackageConfig.() -> Unit) = pkg(delegateClosureOf(block))
fun BintrayExtension.PackageConfig.version(block: BintrayExtension.VersionConfig.() -> Unit) = version(delegateClosureOf(block))
fun BintrayExtension.VersionConfig.gpg(block: BintrayExtension.GpgConfig.() -> Unit) = gpg(delegateClosureOf(block))
