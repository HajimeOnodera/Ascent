plugins {
    id("java-library")
}

dependencies {
    api(project(":database"))
    api("net.kyori:adventure-api:4.26.1")
    api("net.kyori:adventure-text-minimessage:4.26.1")
    api("net.minestom:minestom:2026.04.13-1.21.11")
    api("dev.hollowcube:polar:1.15.1")
    api("it.unimi.dsi:fastutil:8.5.12")
    api("redis.clients:jedis:5.1.5")
    api("com.google.code.gson:gson:2.11.0")
    api("org.reflections:reflections:0.10.2")
    api("org.json:json:20231013")
    
    api("org.slf4j:slf4j-api:2.0.17")
    api("ch.qos.logback:logback-classic:1.5.16")
}


