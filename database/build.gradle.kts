plugins {
    id("java-library")
}

dependencies {
    api("org.mongodb:mongodb-driver-sync:5.1.4")
    api("com.google.code.gson:gson:2.11.0")

    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
}
