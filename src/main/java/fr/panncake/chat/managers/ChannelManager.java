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

import fr.panncake.chat.models.Channel;
import fr.panncake.chat.models.PlayerData;
import org.bukkit.entity.Player;

import java.util.*;

public class ChannelManager {

    private final Map<String, Channel> channels = new HashMap<>();
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public ChannelManager() {
        registerChannel(new Channel("global", "Global", "panncakechat.global", true));
        registerChannel(new Channel("global", "Staff", "panncakechat.staff", false));
    }

    public void registerChannel(Channel channel) {
        channels.put(channel.getName().toLowerCase(), channel);
    }

    public boolean unRegisterChannel(String name) {
        return channels.remove(name.toLowerCase()) != null;
    }

    public Channel getChannel(String name) {
        return channels.get(name.toLowerCase());
    }

    public Collection<Channel> getChannels() {
        return Collections.unmodifiableCollection(channels.values());
    }

    public void setPlayerChannel(Player player, String name) {
        PlayerData data = playerDataMap.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerData());
        data.setCurrentChannel(name);
    }

    public Channel getPlayerChannel(Player player) {
        PlayerData data = playerDataMap.get(player.getUniqueId());
        if (data == null || data.getCurrentChannel() == null) {
            return channels.get("global");
        }
        return channels.get(data.getCurrentChannel());
    }

    public List<Channel> getPlayerChannels(Player player) {
        List<Channel> availableChannels = new ArrayList<>();
        for (Channel channel : channels.values()) {
            if (channel.isDefaultChannel() || player.hasPermission(channel.getPermission())) {
                availableChannels.add(channel);
            }
        }
        return availableChannels;
    }
}
