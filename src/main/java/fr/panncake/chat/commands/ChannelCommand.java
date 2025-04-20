package fr.panncake.chat.commands;

import fr.panncake.chat.PanncakeChat;
import fr.panncake.chat.managers.ChannelManager;
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

import java.util.List;

public class ChannelCommand implements CommandExecutor {

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

        ChannelManager channelManager = PanncakeChat.getInstance().getChannelManager();

        if (args.length == 0) {
            List<Channel> channels = channelManager.getPlayerChannels(player);
            player.sendMessage(Component.text("Available channels:")
                    .color(TextColor.color(NamedTextColor.GREEN)));
            for (Channel channel : channels) {
                player.sendMessage(Component.text("-").color(NamedTextColor.WHITE).appendSpace()
                        .append(Component.text(channel.getName(), NamedTextColor.WHITE)));
            }
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "join":
                if (args.length < 2) {
                    player.sendMessage(Component.text("Usage: /channel join <channel>")
                            .color(TextColor.color(NamedTextColor.RED)));
                    return true;
                }

                Channel channel = channelManager.getChannel(args[1]);
                if (channel == null) {
                    player.sendMessage(Component.text("Channel not found!")
                            .color(TextColor.color(NamedTextColor.RED)));
                    return true;
                }

                if (!player.hasPermission(channel.getPermission())) {
                    player.sendMessage(Component.text("You do not have permission to join this channel!")
                            .color(TextColor.color(NamedTextColor.RED)));
                    return true;
                }

                channelManager.setPlayerChannel(player, channel.getName());
                player.sendMessage(Component.text("You have joined the channel: " + channel.getName())
                        .color(TextColor.color(NamedTextColor.GREEN)));
                break;

            case "list":
                List<Channel> availableChannels = channelManager.getPlayerChannels(player);
                player.sendMessage(Component.text("Available channels:")
                        .color(TextColor.color(NamedTextColor.GREEN)));
                for (Channel availableChannel : availableChannels) {
                    player.sendMessage(Component.text("- " + availableChannel.getName())
                            .color(TextColor.color(NamedTextColor.WHITE)));
                }
                break;

            default:
                player.sendMessage(Component.text("Unknown sub-command: " + subCommand)
                        .color(TextColor.color(NamedTextColor.RED)));
                player.sendMessage(Component.text("Usage: /channel <join|list>")
                        .color(TextColor.color(NamedTextColor.RED)));
                break;
        }
        return true;
    }
}
