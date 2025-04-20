package fr.panncake.chat.listeners;

import fr.panncake.chat.PanncakeChat;
import fr.panncake.chat.managers.ChannelManager;
import fr.panncake.chat.managers.MessageProcessor;
import fr.panncake.chat.models.Channel;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent e) {
        ChannelManager channelManager = PanncakeChat.getInstance().getChannelManager();
        Channel channel = channelManager.getPlayerChannel(e.getPlayer());

        e.setCancelled(true);

        String message = MessageProcessor.process(e.getPlayer(), PlainTextComponentSerializer.plainText().serialize(e.message()), channel);
        PanncakeChat.getInstance().getRedisManager().publish(channel.getName(), message);
    }
}
