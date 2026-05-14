plugins {
    id("io.github.goooler.shadow")
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":database"))
    implementation(project(":core-skyblock"))
    implementation("net.kyori:adventure-text-minimessage:4.26.1")
    implementation("net.minestom:minestom:2026.04.13-1.21.11")
    implementation("org.reflections:reflections:0.10.2")
}

tasks.shadowJar {
    archiveFileName.set("SkyblockHub.jar")
    manifest {
        attributes["Main-Class"] = "fun.ascent.skyblock.hub.HubServer"
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
