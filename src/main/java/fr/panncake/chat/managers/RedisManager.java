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
package fr.panncake.chat.managers;

import fr.panncake.chat.PanncakeChat;
import fr.panncake.chat.models.Channel;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {

    private final String host;
    private final int port;
    private final String password;
    private final int timeout;

    private JedisPool pool;
    private JedisPubSub pubSub;

    public RedisManager(String host, int port, String password, int timeout) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.timeout = timeout;
    }

    public void connect() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);

        if (password.isEmpty()) {
            pool = new JedisPool(poolConfig, host, port, timeout);
        } else {
            pool = new JedisPool(poolConfig, host, port, timeout, password);
        }

        new Thread(() -> {
            try (Jedis jedis = pool.getResource()) {
                pubSub = new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        handleIncomingMessage(channel, message);
                    }
                };
                jedis.subscribe(pubSub, "panncakechat:global", "panncakechat:staff");
            }
        }, "Redis-Subscriber").start();
    }

    public void disconnect() {
        if (pubSub != null) {
            pubSub.unsubscribe();
        }

        if (pool != null) {
            pool.close();
        }
    }

    public void publish(String channel, String message) {
        try (Jedis jedis = pool.getResource()) {
            jedis.publish("panncakechat:" + channel, message);
        }
    }

    private void handleIncomingMessage(String redisChannel, String message) {
        String[] split = message.split("\\|\\|\\|", 4);
        if (split.length != 4) return;

        String server = split[0];
        String channelName = split[1];
        String player = split[2];
        String content = split[3];

        Bukkit.getScheduler().runTask(PanncakeChat.getInstance(), () -> {
            Channel channel = PanncakeChat.getInstance().getChannelManager().getChannel(channelName);

            if (channel == null) return;

            String formattedMessage = String.format(
                    "<%s> [%s] <white>%s</white>: %s",
                    channel.getDisplayName(),
                    server,
                    player,
                    content
            );

            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.hasPermission("panncakechat." + channelName))
                    .forEach(p -> MessageProcessor.sendFormattedMessage(p, formattedMessage));
        });
    }
}
