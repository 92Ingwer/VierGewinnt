package org.kim.vierGewinnt.objects;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kim.vierGewinnt.services.GameService;
import org.kim.vierGewinnt.utils.InventoryBuilder;
import org.kim.vierGewinnt.utils.Messages;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Game {
    private Player starter;
    private Player other;
    private UUID turn;
    private List<Player> list;
    private Inventory gameInventory;
    public static HashMap<UUID, Game> gameHashMap = new HashMap<>();
    public static HashMap<UUID, Game> uniqueGameHashMap = new HashMap<>();

    public Game(Player starter, Player other, UUID turn, List<Player> list) {
        this.starter = starter;
        this.other = other;
        this.turn = turn;
        this.list = list;
    }

    public void start() {
        Inventory inventory = new InventoryBuilder("<gradient:#62D660:#09A9B7>Vier-Gewinnt <color:gray>| <color:#09A9B7>Turn: " + Bukkit.getPlayer(turn).getName() + " </gradient>", 6 * 9, 3)
                .aItem(53, Material.BLACK_WOOL, Component.text("§cAufgeben"), null)
                .build();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
            }
        }
        this.gameInventory = inventory;
        starter.openInventory(inventory);
        other.openInventory(inventory);
    }

    public void changeTurn() {
        if (turn.equals(starter.getUniqueId())) {
            setTurn(other.getUniqueId());
        } else {
            setTurn(starter.getUniqueId());
        }
        Inventory newInventory = new InventoryBuilder("<gradient:#62D660:#09A9B7>Vier-Gewinnt <color:gray>| <color:#09A9B7>Turn: " + Bukkit.getPlayer(turn).getName() + " </gradient>", 6 * 9, 3).build();
        newInventory.setContents(gameInventory.getContents());
        this.gameInventory = newInventory;
        starter.openInventory(newInventory);
        other.openInventory(newInventory);
        for(Player spectator : list) {
            spectator.openInventory(newInventory);
        }
    }

    public void winGame(Player p) {
        p.sendMessage(Messages.GAME_WON.getMessage());
        Player otherr = starter.equals(p) ? other : starter;
        otherr.sendMessage(Messages.GAME_LOOSE.getMessage());
        gameHashMap.remove(starter.getUniqueId());
        gameHashMap.remove(other.getUniqueId());
        uniqueGameHashMap.remove(starter.getUniqueId());
        for(Player spectator : list) {
            GameService.spectateVierGewinntHashMap.remove(spectator.getUniqueId());
            spectator.sendMessage(Messages.PREFIX.getMessage().append(Messages.mm("<color:#62D660> " + p.getName()+ " hat gewonnen! </color>")));
        }
        list.clear();
    }
}
