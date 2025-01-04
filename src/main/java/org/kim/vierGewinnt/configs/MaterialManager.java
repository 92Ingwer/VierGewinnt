package org.kim.vierGewinnt.configs;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class MaterialManager {
    private Material starterMaterial;
    private Material otherMaterial;

    public MaterialManager(FileConfiguration config) {
        loadMaterials(config);
    }

    private void loadMaterials(FileConfiguration config) {
        String starterMaterialName = config.getString("settings.startermaterial", "STONE");
        String otherMaterialName = config.getString("settings.othermaterial", "DIRT");

        try {
            starterMaterial = Material.valueOf(starterMaterialName.toUpperCase());
            otherMaterial = Material.valueOf(otherMaterialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            starterMaterial = Material.BLUE_CONCRETE;
            otherMaterial = Material.RED_CONCRETE;
        }
    }
    public void reload(FileConfiguration config) {
        loadMaterials(config);
    }

}
