/*
 * This is a general purpose Gradle build.
 * Learn more about Gradle by exploring our Samples at https://docs.gradle.org/8.14.2/samples
 */

plugins {
    java
    id("maven-publish") // needed for publishing to local maven repo
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

group   = "com.ronreynolds"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation          (libs.slf4j.api)
    runtimeOnly             (libs.logback)

    // test dependencies
    testImplementation      (libs.assertj)
    testImplementation      (libs.junit.jupiter)
    testRuntimeOnly         (libs.junit.launcher)

    // Lombok dependencies
    compileOnly             (libs.lombok)
    annotationProcessor     (libs.lombok)
    testCompileOnly         (libs.lombok)
    testAnnotationProcessor (libs.lombok)
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("uberjar", Jar::class) {
    group = "build"
    description = "Creates a jar containing classes and all runtime dependencies"
    // task configuration here
    manifest.attributes["Main-Class"] = "com.ronreynolds.tools.Main"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    with(tasks.jar.get())
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}