plugins {
    id("io.github.goooler.shadow")
}

dependencies {
    implementation(project(":commons"))
    implementation("net.kyori:adventure-text-minimessage:4.26.1")
    implementation("net.minestom:minestom:2026.04.13-1.21.11")
}

tasks.shadowJar {
    archiveFileName.set("CoreLobby.jar")
    manifest {
        attributes("Main-Class" to "fun.ascent.lobby.Main")
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
