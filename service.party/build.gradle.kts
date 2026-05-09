plugins {
    id("java")
    id("io.github.goooler.shadow")
}

tasks.shadowJar {
    archiveFileName.set("PartyService.jar")
    manifest {
        attributes("Main-Class" to "fun.ascent.service.party.PartyService")
    }
}

group = "fun.ascent"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":commons"))
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation("com.google.code.gson:gson:2.11.0")

}

tasks.build {
    dependsOn(tasks.shadowJar)
}
