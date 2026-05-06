plugins {
    id("io.github.goooler.shadow")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")
    
    implementation("redis.clients:jedis:5.1.5")
    implementation("com.google.code.gson:gson:2.11.0")
}

tasks.shadowJar {
    archiveFileName.set("CoreProxy.jar")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
