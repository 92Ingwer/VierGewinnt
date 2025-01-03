package org.kim.vierGewinnt.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.vierGewinnt.objects.Game;
import org.kim.vierGewinnt.services.GameService;

public class VierGewinntCommand implements CommandExecutor {
    static final Component USAGE = Component.text("§cUsage: Verwende /viergewinnt <Spieler/list/spectate>");
    static final Component PLAYERNOTFOUND = Component.text("§cSpieler nicht gefunden");
    public static final Component PREFIX = MiniMessage.miniMessage().deserialize("<b><gradient:#B7A9F8:#F6A4A4>Vier-Gewinnt</b> <color:gray>|");

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p)) {
            return false;
        }
        if (strings.length >2 || strings.length == 0) {
            p.sendMessage(USAGE);
            return false;
        }
        if(strings[0].equalsIgnoreCase("spectate")) {
            Player spectated = Bukkit.getPlayer(strings[1]);
            if (spectated == null) {
                p.sendMessage(PLAYERNOTFOUND);
                return false;
            }
            Game game = Game.gameHashMap.get(spectated.getUniqueId());
            if (game == null) {
                p.sendMessage(PREFIX.append(MiniMessage.miniMessage().deserialize("<color:#99ffcf> " + spectated.getName() + " spielt kein VierGewinnt! </color>")));
                return false;
            }
            game.getList().add(p);
            p.openInventory(game.getGameInventory());
            GameService.spectateVierGewinntHashMap.put(p.getUniqueId(), spectated.getUniqueId());
            return false;
        }
        if(strings[0].equalsIgnoreCase("list")) {
            if(Game.gameHashMap.isEmpty()) {
                p.sendMessage(PREFIX.append(MiniMessage.miniMessage().deserialize("<color:#99ffcf> Es gibt keine offenen Spiele! </color>")));
                return false;
            }
            for(Game game : Game.uniqueGameHashMap.values()) {
                p.sendMessage(PREFIX.append(MiniMessage.miniMessage().deserialize("<color:#99ffcf> " + game.getStarter().getName() + " vs " + game.getOther().getName() + " </color> <color:#9accf5>[Spectate] </color>").clickEvent(ClickEvent.runCommand("/viergewinnt spectate " + game.getStarter().getName()))));
            }
            return false;
        }
        Player t = Bukkit.getPlayer(strings[0]);
        if (t == null) {
            p.sendMessage(PLAYERNOTFOUND);
            return false;
        }
        if(t == p) {
            p.sendMessage(PREFIX.append(MiniMessage.miniMessage().deserialize("<color:#99ffcf> Du kannst nicht gegen dich selbst spielen! </color>")));
            return false;
        }
        p.sendMessage(PREFIX.append(MiniMessage.miniMessage().deserialize("<color:#99ffcf> Du hast eine Anfrage an " + t.getName() + " geschickt! </color>")) );
        t.sendMessage(PREFIX.append(MiniMessage.miniMessage().deserialize("<color:#99ffcf> " + p.getName() + " hat dir eine Anfrage für VierGewinnt geschickt! </color>" + "<color:#9accf5>[Accept]</color>").clickEvent(ClickEvent.runCommand("/accept " + p.getName()))));
        GameService.requestHashMap.put(p.getUniqueId(),t.getUniqueId());
        return false;
    }
}
