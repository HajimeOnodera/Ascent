plugins {
    id("io.github.goooler.shadow")
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":skyblock.island"))
    implementation("net.kyori:adventure-text-minimessage:4.26.1")
    implementation("dev.hollowcube:polar:1.15.1")
    implementation("net.minestom:minestom:2026.04.13-1.21.11")
    implementation("org.jctools:jctools-core:4.0.5")
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.yaml:snakeyaml:2.2")
}

tasks.shadowJar {
    archiveFileName.set("CoreSkyblock.jar")
    manifest {
        attributes("Main-Class" to "fun.ascent.skyblock.Main")
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
