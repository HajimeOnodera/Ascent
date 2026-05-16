plugins {
    id("io.github.goooler.shadow")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")

    implementation(project(":commons"))
    implementation(project(":database"))

    implementation("org.mongodb:mongodb-driver-sync:5.1.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    archiveFileName.set("DiscordSync.jar")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
