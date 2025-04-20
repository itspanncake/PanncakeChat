package fr.panncake.chat.commands;

import fr.panncake.chat.PanncakeChat;
import fr.panncake.chat.managers.MessageProcessor;
import fr.panncake.chat.models.Channel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command cmd,
            @NotNull String msg,
            @NotNull String[] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command!")
                    .color(TextColor.color(NamedTextColor.RED)));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Component.text("Usage: /chat <message>")
                    .color(TextColor.color(NamedTextColor.RED)));
            return true;
        }

        Channel channel = PanncakeChat.getInstance().getChannelManager().getPlayerChannel(player);
        String message = String.join(" ", args);

        String processMessage = MessageProcessor.process(player, message, channel);
        PanncakeChat.getInstance().getRedisManager().publish(channel.getName(), processMessage);
        return true;
    }
}
