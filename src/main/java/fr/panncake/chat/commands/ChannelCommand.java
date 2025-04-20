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
package fr.panncake.chat.commands;

import fr.panncake.chat.PanncakeChat;
import fr.panncake.chat.managers.ChannelManager;
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
