package fr.panncake.chat.managers;

import fr.panncake.chat.PanncakeChat;
import fr.panncake.chat.models.Channel;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;

public class MessageProcessor {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private static BukkitAudiences audiences;

    public static void init(PanncakeChat plugin) {
        audiences = BukkitAudiences.create(plugin);
    }

    public static String process(Player sender, String rawMessage, Channel channel) {
        Component messageComponent = MINI_MESSAGE.deserialize(rawMessage);

        String plainTextMessage = PlainTextComponentSerializer.plainText().serialize(messageComponent);

        return String.join("|||",
                PanncakeChat.getInstance().getConfig().getString("server-name", "server"),
                channel.getName(),
                sender.getName(),
                plainTextMessage);
    }

    public static void sendFormattedMessage(Player player, String formattedMessage) {
        Audience audience = audiences.player(player);
        audience.sendMessage(MINI_MESSAGE.deserialize(formattedMessage));
    }
}
