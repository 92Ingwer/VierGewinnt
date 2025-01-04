package org.kim.vierGewinnt;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.kim.vierGewinnt.commands.AcceptCommand;
import org.kim.vierGewinnt.commands.VierGewinntCommand;
import org.kim.vierGewinnt.configs.MaterialManager;
import org.kim.vierGewinnt.listeners.CloseInventoryListener;
import org.kim.vierGewinnt.listeners.VierGewinntGUI;

@Getter
public final class VierGewinnt extends JavaPlugin {
    @Getter
    public static VierGewinnt instance;
    private MaterialManager materialManager;

    @Override
    public void onEnable() {
        instance = this;
        //config
        saveDefaultConfig();
        materialManager = new MaterialManager(getConfig());
        //Events
        Bukkit.getPluginManager().registerEvents(new VierGewinntGUI(), this);
        Bukkit.getPluginManager().registerEvents(new CloseInventoryListener(), this);
        //Commands
        this.getCommand("viergewinnt").setExecutor(new VierGewinntCommand());
        this.getCommand("accept").setExecutor(new AcceptCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
