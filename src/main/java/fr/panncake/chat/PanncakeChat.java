package fr.panncake.chat;

import org.bukkit.plugin.java.JavaPlugin;


public class PanncakeChat extends JavaPlugin {

    private static PanncakeChat instance;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("PanncakeChat has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PanncakeChat has been disabled!");
    }

    public static PanncakeChat getInstance() {
        return instance;
    }
}
