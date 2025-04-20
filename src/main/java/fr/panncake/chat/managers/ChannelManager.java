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
