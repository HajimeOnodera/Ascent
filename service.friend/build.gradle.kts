plugins {
    id("java")
    id("application")
}

group = "fun.ascent"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":service.generic"))
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.mongodb:bson:4.11.2")
    implementation("org.mongodb:mongodb-driver-sync:4.11.2")
}

application {
    mainClass.set("fun.ascent.service.friend.FriendService")
}