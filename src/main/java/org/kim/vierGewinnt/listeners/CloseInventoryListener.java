package org.kim.vierGewinnt.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.kim.vierGewinnt.objects.Game;
import org.kim.vierGewinnt.services.GameService;

/**
 * Listener class for handling inventory close events in the VierGewinnt game.
 */
public class CloseInventoryListener implements Listener {

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        // If the inventory close event is due to a plugin, return
        if(e.getReason() == InventoryCloseEvent.Reason.PLUGIN) {
            return;
        }
        // If the player is in a game and closes the inventory, close the inventory for all players in the game (and spectators)
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
        //If a spectator closes the inventory, remove them from the list
        GameService.spectateVierGewinntHashMap.remove(p.getUniqueId());
    }
}