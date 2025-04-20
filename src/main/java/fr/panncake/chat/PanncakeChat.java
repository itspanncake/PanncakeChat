package fr.panncake.chat;

import fr.panncake.chat.managers.RedisManager;
import org.bukkit.plugin.java.JavaPlugin;


public class PanncakeChat extends JavaPlugin {

    private static PanncakeChat instance;

    private RedisManager redisManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.redisManager = new RedisManager(
                getConfig().getString("redis.host"),
                getConfig().getInt("redis.port"),
                getConfig().getString("redis.password"),
                getConfig().getInt("redis.timeout")
        );

        getLogger().info("PanncakeChat has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PanncakeChat has been disabled!");
    }

    public static PanncakeChat getInstance() {
        return instance;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }
}
