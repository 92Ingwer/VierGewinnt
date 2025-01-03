package org.kim.vierGewinnt.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.vierGewinnt.objects.Game;
import org.kim.vierGewinnt.services.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Command executor for the /accept command.
 * This command allows a player to accept a game request from another player.
 */
public class AcceptCommand implements CommandExecutor {
    static final Component USAGE = Component.text("§cUsage: Verwende /accept <Spieler>");
    static final Component REQUESTNOTFOUND = Component.text("§cAnfrage nicht gefunden");

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p)) {
            return false;
        }

        if (strings.length != 1) {
            p.sendMessage(USAGE);
            return false;
        }
        // Get the target player
        Player t = Bukkit.getPlayer(strings[0]);
        if (t == null) {
            p.sendMessage(REQUESTNOTFOUND);
            return false;
        }
        UUID targetUUID = t.getUniqueId();
        // Check if there is a pending request from the target player
        if (GameService.requestHashMap.get(targetUUID) != p.getUniqueId()) {
            p.sendMessage(REQUESTNOTFOUND);
            return false;
        }
        // Remove the request from the hashmap
        GameService.requestHashMap.remove(targetUUID);
        List<Player> list = new ArrayList<>();
        // Create a new game instance
        Game game = new Game(t, p, targetUUID, list);
        // Add the game to the game hashmaps
        Game.gameHashMap.put(targetUUID, game);
        Game.gameHashMap.put(p.getUniqueId(), game);
        Game.uniqueGameHashMap.put(targetUUID, game);
        // Start the game
        game.start();
        return false;
    }
}