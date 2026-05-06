plugins {
    id("java-library")
}

dependencies {
    api(project(":commons"))
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.json:json:20231013")
}
