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
import org.kim.vierGewinnt.utils.Messages;

public class VierGewinntCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p)) {
            return false;
        }
        if (strings.length >2 || strings.length == 0) {
            p.sendMessage(Messages.MAIN_CMD_USAGE.getMessage());
            return false;
        }
        if(strings[0].equalsIgnoreCase("spectate")) {
            Player spectated = Bukkit.getPlayer(strings[1]);
            if (spectated == null) {
                p.sendMessage(Messages.PLAYER_NOT_FOUND.getMessage());
                return false;
            }
            Game game = Game.gameHashMap.get(spectated.getUniqueId());
            if (game == null) {
                p.sendMessage(Messages.PLAYER_NOT_PLAYING.getMessage());
                return false;
            }
            game.getList().add(p);
            p.openInventory(game.getGameInventory());
            GameService.spectateVierGewinntHashMap.put(p.getUniqueId(), spectated.getUniqueId());
            return false;
        }
        if(strings[0].equalsIgnoreCase("list")) {
            if(Game.gameHashMap.isEmpty()) {
                p.sendMessage(Messages.NO_OPEN_GAMES.getMessage());
                return false;
            }
            for(Game game : Game.uniqueGameHashMap.values()) {
                p.sendMessage(Messages.PREFIX.getMessage().append(Messages.mm("<color:#00C9DB>" + game.getStarter().getName() + " <color:gray>vs <color:#00C9DB>" + game.getOther().getName() + " <color:gray> | <color:#D78C00>[Spectate] </color>").clickEvent(ClickEvent.runCommand("/viergewinnt spectate " + game.getStarter().getName()))));
            }
            return false;
        }
        Player t = Bukkit.getPlayer(strings[0]);
        if (t == null) {
            p.sendMessage(Messages.PLAYER_NOT_FOUND.getMessage());
            return false;
        }
        if(t == p) {
            p.sendMessage(Messages.REQUEST_ERROR.getMessage());
            return false;
        }
        p.sendMessage(Messages.PREFIX.getMessage().append(Messages.mm("<color:#09A9B7> Du hast eine Anfrage an " + t.getName() + " geschickt! </color>")) );
        t.sendMessage(Messages.PREFIX.getMessage().append(Messages.mm("<color:#09A9B7> " + p.getName() + " hat dir eine Anfrage für <gradient:#62D660:#09A9B7>VierGewinnt <color:#09A9B7>geschickt! </color>" + "<color:#9accf5>[Accept]</color>").clickEvent(ClickEvent.runCommand("/accept " + p.getName()))));
        GameService.requestHashMap.put(p.getUniqueId(),t.getUniqueId());
        return false;
    }
}
