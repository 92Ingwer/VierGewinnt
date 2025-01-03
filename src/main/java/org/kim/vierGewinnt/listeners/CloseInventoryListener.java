package org.kim.vierGewinnt.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.kim.vierGewinnt.objects.Game;

/**
 * Listener class for handling inventory close events in the VierGewinnt game.
 */
public class CloseInventoryListener implements Listener {

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Game game = Game.gameHashMap.get(p.getUniqueId());
        if (game != null && e.getInventory().equals(game.getGameInventory())) {
            try {
                game.getStarter().closeInventory();
                game.getOther().closeInventory();
                for (Player specatedPlayer : game.getList()) {
                    specatedPlayer.closeInventory();
                }
            } catch (Exception ignored) {
            }
            game.winGame(game.getStarter().equals(p) ? game.getOther() : game.getStarter());
        }
    }
}