package fr.panncake.chat;

import org.bukkit.plugin.java.JavaPlugin;

public class PanncakeChat extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("PanncakeChat has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PanncakeChat has been disabled!");
    }
}
