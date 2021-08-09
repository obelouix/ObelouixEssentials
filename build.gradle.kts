// Configure Auto Relocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    id("xyz.jpenilla.run-paper") version "1.0.3"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "fr.obelouix"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}


repositories {
    mavenCentral()
    maven {
        setUrl("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        setUrl("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        setUrl("https://jitpack.io")
    }
    maven {
        setUrl("https://repo.dmulloy2.net/repository/public/")
    }

    maven {
        setUrl("https://repo.spongepowered.org/maven/")
    }
    maven {
        setUrl("https://libraries.minecraft.net/")
    }
    maven {
        setUrl("https://repo.codemc.org/repository/maven-public/")
    }
    maven {
        setUrl("https://repo.aikar.co/content/groups/aikar/")
    }
    maven {
        setUrl("https://maven.enginehub.org/repo/")
    }
    maven {
        setUrl("https://hub.spigotmc.org/nexus/content/groups/public/")
    }
    maven {
        setUrl("https://repo.mikeprimm.com")
    }

}

dependencies {

    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    implementation("io.papermc:paperlib:1.0.6")
    compileOnly("net.luckperms:api:5.3")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.6-SNAPSHOT")
    implementation("org.spongepowered:configurate-core:4.1.1")
    implementation("org.spongepowered:configurate-yaml:4.1.1")
    implementation("me.lucko:commodore:1.10")
    implementation("co.aikar:minecraft-timings:1.0.4")
    compileOnly("us.dynmap:dynmap-api:3.2-beta-1")

    //test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.17.1")
    }

}
//automatic relocation of dependencies
tasks.create<ConfigureShadowRelocation>("relocateShadowJar") {
    target = tasks["shadowJar"] as com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
    prefix = "fr.obelouix.libs"
}

tasks.shadowJar {
    dependsOn(tasks.processResources)
    dependsOn(tasks["relocateShadowJar"])
    //exclude dependencies as they are included in the server jar
    dependencies {
        exclude(dependency("com.mojang:brigadier"))
        exclude(dependency("org.yaml:snakeyaml"))
    }
}

tasks.jar {
    enabled = false
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    from("src/main/resources") {
        include("**/plugin.yml")
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}
