package fr.panncake.chat.commands.tabcompleters;

import fr.panncake.chat.PanncakeChat;
import fr.panncake.chat.models.Channel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChannelCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command cmd,
            @NotNull String msg,
            @NotNull String[] args
    ) {
        if (!(sender instanceof Player player)) return null;

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("join");
            completions.add("list");
        }

        if (args.length == 2 && args[0].equals("join")) {
            List<Channel> channels = PanncakeChat.getInstance().getChannelManager().getPlayerChannels(player);
            for (Channel channel : channels) {
                completions.add(channel.getName());
            }
        }

        return completions;
    }
}
