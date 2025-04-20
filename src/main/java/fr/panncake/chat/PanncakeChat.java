/**
 * Copyright (c) 2025 Panncake
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fr.panncake.chat;

import fr.panncake.chat.commands.ChannelCommand;
import fr.panncake.chat.commands.ChatCommand;
import fr.panncake.chat.commands.tabcompleters.ChannelCompleter;
import fr.panncake.chat.commands.tabcompleters.ChatCompleter;
import fr.panncake.chat.listeners.ChatListener;
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

        this.audiences = BukkitAudiences.create(this);

        saveDefaultConfig();

        MessageProcessor.init(this);

        this.channelManager = new ChannelManager();
        this.redisManager = new RedisManager(
                getConfig().getString("redis.host"),
                getConfig().getInt("redis.port"),
                getConfig().getString("redis.password"),
                getConfig().getInt("redis.timeout")
        );
        this.redisManager.connect();

        Objects.requireNonNull(getCommand("chat")).setExecutor(new ChatCommand());
        Objects.requireNonNull(getCommand("chat")).setTabCompleter(new ChatCompleter());

        Objects.requireNonNull(getCommand("channel")).setExecutor(new ChannelCommand());
        Objects.requireNonNull(getCommand("channel")).setTabCompleter(new ChannelCompleter());

        getServer().getPluginManager().registerEvents(new ChatListener(), this);

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
