package fr.panncake.chat.commands.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChatCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command cmd,
            @NotNull String msg,
            @NotNull String[] args
    ) {
        if (!(sender instanceof Player)) return null;

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("<message>");
        }

        return completions;
    }
}
