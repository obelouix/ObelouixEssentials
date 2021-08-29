// Configure Auto Relocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.NOT_OP
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.OP
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.STARTUP

plugins {
    java
    id("xyz.jpenilla.run-paper") version "1.0.4"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0"
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
        setUrl("https://repo.codemc.io/repository/nms/")
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
    maven {
        setUrl("https://repo.incendo.org/content/repositories/snapshots")
    }
    maven {
        setUrl("https://mvn.intellectualsites.com/content/groups/public/")
    }
}

dependencies {

    // Paper
    compileOnly("io.papermc.paper:paper:1.17.1-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-mojangapi:1.17.1-R0.1-SNAPSHOT")
    implementation("io.papermc:paperlib:1.0.6")

    // Luckperms
    compileOnly("net.luckperms:api:5.3")

    // ProtocolLib
    compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0")

    // WorldEdit
    compileOnly("com.fastasyncworldedit:FAWE-Bukkit:1.17-162") { isTransitive = false }
    compileOnly("com.fastasyncworldedit:FAWE-Core:1.17-162")

    // WorldGuard
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.6-SNAPSHOT")

    // Commodore (Minecraft Brigadier)
    implementation("me.lucko:commodore:1.10")

    // Aikar's Timing
    implementation("co.aikar:minecraft-timings:1.0.4")

    // Dynmap
    compileOnly("us.dynmap:dynmap-api:3.2-beta-1")

    //Sponge Configurate
    implementation("org.spongepowered:configurate-core:4.1.2")
    implementation("org.spongepowered:configurate-yaml:4.1.2")

    //NBT API
    implementation("de.tr7zw:item-nbt-api-plugin:2.8.0")

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
    //dependsOn(tasks.processResources)
    dependsOn(tasks["relocateShadowJar"])
    //exclude dependencies as they are included in the server jar
    dependencies {
        exclude(dependency("com.mojang:brigadier"))
//        exclude(dependency("org.yaml:snakeyaml"))
    }
}

tasks.jar {
    enabled = false
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

bukkit {
    main = "fr.obelouix.essentials.Essentials"
    apiVersion = "1.17"
    softDepend = listOf("ProtocolLib", "LuckPerms", "FastAsyncWorldEdit", "WorldGuard")
    load = STARTUP
    prefix = "Obelouix"
    defaultPermission = OP

    permissions {
        register("bukkit.command.difficulty") {
            description = "Allows you to run the difficulty command"
            default = OP
        }
        register("bukkit.command.xp") {
            description = "Allows you to run the xp command"
            default = OP
        }

        register("obelouix.break.block.*") {
            description = "Allows you to control which block players are allowed to break"
            default = NOT_OP
            childrenMap = mapOf(
                "obelouix.break.block.minecraft.tnt" to false,
                "obelouix.break.block.minecraft.lava" to false,
                "obelouix.break.block.minecraft.bedrock" to false
            )
        }

        register("obelouix.place.block.*") {
            description = "Allows you to control which block players are allowed to place"
            default = NOT_OP
            childrenMap = mapOf(
                "obelouix.place.block.minecraft.tnt" to false,
                "obelouix.place.block.minecraft.bedrock" to false
            )
        }

        register("obelouix.commands.day") {
            description = "Allows you to run the day command"
            default = OP
        }

        register("obelouix.commands.settings") {
            description = "Allows you to run the settings command"
            default = OP
        }

        register("obelouix.commands.admin") {
            description = "Allows you to run the admin command"
            default = OP
        }

        register("obelouix.commands.openinv") {
            description = "Allows you to run the openinv command"
            default = OP
        }

        register("obelouix.commands.enderchest") {
            description = "Allows you to run the enderchest command"
            default = OP
            children = listOf("obelouix.commands.enderchest.others")
        }

        register("obelouix.commands.tppos") {
            description = "Allows you to run the tppos command"
            default = OP
        }

        register("obelouix.commands.fly") {
            description = "Allows you to run the fly command"
            default = OP
            children = listOf("obelouix.commands.fly.others")
        }

        register("obelouix.commands.gamemode") {
            description = "Allows you to run the gamemode command"
            default = OP
            children = listOf(
                "obelouix.commands.gamemode.adventure",
                "obelouix.commands.gamemode.creative",
                "obelouix.commands.gamemode.spectator",
                "obelouix.commands.gamemode.survival",
                "obelouix.commands.gamemode.others.adventure",
                "obelouix.commands.gamemode.others.creative",
                "obelouix.commands.gamemode.others.spectator",
                "obelouix.commands.gamemode.others.survival"
            )
        }

        register("obelouix.admin.playerdetails") {
            description = "Allows you to see critical information about a player when hovering his name in the chat"
            default = OP
        }

    }

}
