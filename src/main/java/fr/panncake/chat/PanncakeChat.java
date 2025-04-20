package fr.panncake.chat;

import fr.panncake.chat.commands.ChannelCommand;
import fr.panncake.chat.commands.ChatCommand;
import fr.panncake.chat.commands.tabcompleters.ChannelCompleter;
import fr.panncake.chat.commands.tabcompleters.ChatCompleter;
import fr.panncake.chat.managers.ChannelManager;
import fr.panncake.chat.managers.MessageProcessor;
import fr.panncake.chat.managers.RedisManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public class PanncakeChat extends JavaPlugin {

    private static PanncakeChat instance;

    private BukkitAudiences audiences;

    private ChannelManager channelManager;
    private RedisManager redisManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.audiences = BukkitAudiences.create(this);
        MessageProcessor.init(this);

        this.channelManager = new ChannelManager();
        this.redisManager = new RedisManager(
                getConfig().getString("redis.host"),
                getConfig().getInt("redis.port"),
                getConfig().getString("redis.password"),
                getConfig().getInt("redis.timeout")
        );

        Objects.requireNonNull(getCommand("chat")).setExecutor(new ChatCommand());
        Objects.requireNonNull(getCommand("chat")).setTabCompleter(new ChatCompleter());

        Objects.requireNonNull(getCommand("channel")).setExecutor(new ChannelCommand());
        Objects.requireNonNull(getCommand("channel")).setTabCompleter(new ChannelCompleter());

        getLogger().info("PanncakeChat has been enabled!");
    }

    @Override
    public void onDisable() {
        if (audiences != null) {
            audiences.close();
        }

        if (redisManager != null) {
            redisManager.disconnect();
        }

        getLogger().info("PanncakeChat has been disabled!");
    }

    public static PanncakeChat getInstance() {
        return instance;
    }

    public BukkitAudiences getAudiences() {
        return audiences;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }
}
