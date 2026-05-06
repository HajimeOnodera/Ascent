plugins {
    java
    id("io.github.goooler.shadow") version "8.1.8" apply false
}

allprojects {
    group = "fun.ascent"
    version = "1.0.0"

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

subprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<AbstractCopyTask> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    dependencies {
        // Shared dependencies across all subprojects (from root pom)
        implementation("org.postgresql:postgresql:42.7.11")
        
        compileOnly("org.projectlombok:lombok:1.18.44")
        annotationProcessor("org.projectlombok:lombok:1.18.44")
    }
}
