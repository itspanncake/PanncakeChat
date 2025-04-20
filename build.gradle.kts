plugins {
    id("java")
    id("io.github.goooler.shadow") version("8.1.8")
}

group = "fr.panncake.chat"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.4")

    implementation("redis.clients:jedis:5.2.0")
    implementation("net.kyori:adventure-api:4.20.0")
    implementation("net.kyori:adventure-text-minimessage:4.20.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.4")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    clean {
        delete("run")
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        minimize()
        doLast {
            copy {
                from(archiveFile)
                into("${rootProject.projectDir}/build")
            }
        }
    }
}
