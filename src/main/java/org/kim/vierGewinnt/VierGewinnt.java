package org.kim.vierGewinnt;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.kim.vierGewinnt.commands.AcceptCommand;
import org.kim.vierGewinnt.commands.VierGewinntCommand;
import org.kim.vierGewinnt.listeners.CloseInventoryListener;
import org.kim.vierGewinnt.listeners.VierGewinntGUI;

public final class  VierGewinnt extends JavaPlugin {
    @Getter
    public static VierGewinnt instance;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new VierGewinntGUI(),this);
        Bukkit.getPluginManager().registerEvents(new CloseInventoryListener(),this);
        this.getCommand("viergewinnt").setExecutor(new VierGewinntCommand());
        this.getCommand("accept").setExecutor(new AcceptCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
