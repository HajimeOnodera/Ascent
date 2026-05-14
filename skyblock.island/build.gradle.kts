plugins {
    id("io.github.goooler.shadow")
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":database"))
    implementation(project(":core-skyblock"))
    implementation("net.kyori:adventure-text-minimessage:4.26.1")
    implementation("dev.hollowcube:polar:1.15.1")
    implementation("net.minestom:minestom:2026.04.13-1.21.11")
}

tasks.shadowJar {
    archiveFileName.set("SkyblockIsland.jar")
    manifest {
        attributes["Main-Class"] = "fun.ascent.skyblock.island.IslandServer"
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
