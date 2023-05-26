package de.cruelambition.generator;

import java.util.List;

import org.bukkit.Material;

public class Generator {
    private List<Material> material;

    public Material getRandomMaterial() {
        return Material.AIR;
    }

    public Material getMaterialFromInt() {
        return Material.AIR;
    }

    public List<Material> getMaterialList() {
        return this.material;
    }

    public void startLoop() {
    }

    public boolean isForbiddenItem() {
        return false;
    }
}
