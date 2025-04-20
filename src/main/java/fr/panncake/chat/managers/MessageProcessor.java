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
