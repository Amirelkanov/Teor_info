plugins {
    kotlin("jvm") version "1.6.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.jetbrains.kotlin:kotlin-test:$embeddedKotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$embeddedKotlinVersion")
    testImplementation("junit:junit:4.13.2")
}