package org.kim.vierGewinnt.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.kim.vierGewinnt.VierGewinnt;
import org.kim.vierGewinnt.commands.VierGewinntCommand;
import org.kim.vierGewinnt.objects.Game;
import org.kim.vierGewinnt.services.GameService;
import org.kim.vierGewinnt.utils.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Listener class for handling the VierGewinnt game events.
 */
public class VierGewinntGUI implements Listener {
    // Message displayed when a player wins
    // List to store the winning slots
    static final List<Integer> list = new ArrayList<>();

    /**
     * Event handler for inventory click events in the VierGewinnt game.
     *
     * @param e the InventoryClickEvent
     */
    @EventHandler
    public void onVierGewinnt(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        UUID uuid = p.getUniqueId();
        Game game = Game.gameHashMap.get(uuid);
        int slot = e.getSlot();
        Inventory inventory = p.getOpenInventory().getTopInventory();

        // Handle spectator mode
        if (GameService.spectateVierGewinntHashMap.containsKey(p.getUniqueId())) {
            Player spectatedPlayer = Bukkit.getPlayer(GameService.spectateVierGewinntHashMap.get(p.getUniqueId()));
            if (spectatedPlayer != null) {
                inventory = spectatedPlayer.getOpenInventory().getTopInventory();
            }
            game = Game.gameHashMap.get(GameService.spectateVierGewinntHashMap.get(p.getUniqueId()));
        }

        // If the game does not exist, return
        if (game == null) {
            return;
        }

        // If the inventory is the game inventory
        if (inventory.equals(game.getGameInventory())) {
            if (slot < 0 || slot >= e.getInventory().getSize()) {
                return;
            }
            e.setCancelled(true);
            if (GameService.spectateVierGewinntHashMap.containsKey(p.getUniqueId())) {
                return;
            }
            Player t = game.getStarter().equals(p) ? game.getOther() : game.getStarter();
            if (slot == 53) {
                game.winGame(t);
                p.closeInventory();
                t.closeInventory();
                return;
            }
            // Get the materials for the players
            Material starterMaterial = VierGewinnt.getInstance().getMaterialManager().getStarterMaterial();
            Material otherMaterial = VierGewinnt.getInstance().getMaterialManager().getOtherMaterial();
            // Get the material for the current player
            Material material = game.getStarter().equals(p) ? starterMaterial : otherMaterial;
            if (!game.getTurn().equals(uuid)) {
                return;
            }
            if (isFreeSlot(slot, inventory)) {
                inventory.setItem(slot, new ItemStack(material));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
                t.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1,1);
                if (hasPlayerWon(slot, inventory, material)) {
                    for (int i : list) {
                        inventory.setItem(i, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
                    }
                    Game finalGame = game; // For the Runnable
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (Player spectator : finalGame.getList()) {
                                spectator.closeInventory();
                            }
                            finalGame.winGame(p);
                            t.closeInventory();
                            p.closeInventory();
                            this.cancel();
                        }
                    }.runTaskLater(VierGewinnt.getInstance(), 40L);
                    return;
                }
                if (isDrawn(inventory)) {
                    p.sendMessage(Messages.GAME_TIE.getMessage());
                    t.sendMessage(Messages.GAME_TIE.getMessage());
                    Game.gameHashMap.remove(game.getStarter().getUniqueId());
                    Game.gameHashMap.remove(game.getOther().getUniqueId());
                    for (Player spectator : game.getList()) {
                        GameService.spectateVierGewinntHashMap.remove(spectator.getUniqueId());
                        spectator.sendMessage(Messages.GAME_TIE.getMessage());
                    }
                    game.getList().clear();
                    return;
                }
                game.changeTurn();
            }
        }
    }

    /**
     * Checks if a player has won the game.
     *
     * @param slot the slot that was clicked
     * @param inventory the game inventory
     * @param material the material of the player's pieces
     * @return true if the player has won, false otherwise
     */
    public boolean hasPlayerWon(int slot, Inventory inventory, Material material) {
        int score = 0;
        int row = slot / 9;
        int column = slot % 9;
        // Check horizontally
        for (int i = slot - 3; i <= slot + 3; i++) {
            if (i < 0 || i >= inventory.getSize()) continue;
            if (i / 9 == row) {
                ItemStack item = inventory.getItem(i);
                if (item == null || item.getType() != material) {
                    score = 0;
                    list.clear();
                    continue;
                }
                list.add(i);
                score++;
                if (score == 4) return true;
            }
        }
        list.clear();
        score = 0;
        // Check vertically
        for (int i = column; i < inventory.getSize(); i += 9) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() != material) {
                score = 0;
                list.clear();
                continue;
            }
            list.add(i);
            score++;
            if (score == 4) return true;
        }
        list.clear();
        score = 0;
        // Check diagonal (bottom-left to top-right)
        for (int i = slot - 3 * 8; i <= slot + 3 * 8; i += 8) {
            if (i < 0 || i >= inventory.getSize()) continue;
            int currentRow = i / 9;
            int currentColumn = i % 9;
            if (Math.abs(currentRow - row) == Math.abs(currentColumn - column)) {
                ItemStack item = inventory.getItem(i);
                if (item == null || item.getType() != material) {
                    score = 0;
                    list.clear();
                    continue;
                }
                list.add(i);
                score++;
                if (score == 4) return true;
            }
        }
        list.clear();
        score = 0;
        // Check diagonal (top-left to bottom-right)
        for (int i = slot - 3 * 10; i <= slot + 3 * 10; i += 10) {
            if (i < 0 || i >= inventory.getSize()) continue;
            int currentRow = i / 9;
            int currentColumn = i % 9;
            if (Math.abs(currentRow - row) == Math.abs(currentColumn - column)) {
                ItemStack item = inventory.getItem(i);
                if (item == null || item.getType() != material) {
                    score = 0;
                    list.clear();
                    continue;
                }
                list.add(i);
                score++;
                if (score == 4) return true;
            }
        }
        list.clear();
        return false;
    }

    /**
     * Checks if a slot is free to place a piece.
     *
     * @param slot the slot to check
     * @param inventory the game inventory
     * @return true if the slot is free, false otherwise
     */
    public boolean isFreeSlot(int slot, Inventory inventory) {
        ItemStack item = inventory.getItem(slot);
        if (item == null || item.getType() != Material.WHITE_STAINED_GLASS_PANE) {
            return false;
        }
        for (int i = slot + 9; i < inventory.getSize(); i += 9) {
            if (i >= inventory.getSize()) {
                return true;
            }
            item = inventory.getItem(i);
            if (item != null && item.getType() == Material.WHITE_STAINED_GLASS_PANE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the game is a draw.
     *
     * @param inventory the game inventory
     * @return true if the game is a draw, false otherwise
     */
    public boolean isDrawn(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() == Material.WHITE_STAINED_GLASS_PANE) {
                return false;
            }
        }
        return true;
    }
}