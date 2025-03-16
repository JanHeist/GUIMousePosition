plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "dev_janheist_saveguimouseposition"
version = providers.environmentVariable("VERSION").getOrElse("1.0.0")

labyMod {
    defaultPackageName = "dev_janheist_saveguimouseposition" //change this to your main package name (used by all modules)

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // When the property is set to true, you can log in with a Minecraft account
                    // devLogin = true
                }
            }
        }
    }

    addonInfo {
        namespace = "dev_janheist_saveguimouseposition"
        displayName = "Save GUI Mouse Position"
        author = "Jan Heist"
        description = "Save Mouse Position in Inventory GUIs globally or based on the Inventory's title."
        minecraftVersion = "1.8.9;1.12.2;1.16.5;1.17.1;1.18.2;1.19.2;1.19.3;1.19.4;1.20.1;1.20.2;1.20.4;1.20.5;1.20.6;1.21;1.21.1;1.21.3;1.21.4"
        version = rootProject.version.toString()
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}