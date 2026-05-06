plugins {
    id("io.github.goooler.shadow")
}

dependencies {
    implementation(project(":core-lobby"))
    implementation(project(":core-skyblock"))
}

tasks.shadowJar {
    archiveFileName.set("AscentLoader.jar")
    manifest {
        attributes("Main-Class" to "fun.ascent.loader.Loader")
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
