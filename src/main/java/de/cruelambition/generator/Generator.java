package de.cruelambition.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Generator {
    private List<Material> material, forbidden;

    public void fillList() {
        material.addAll(Arrays.asList(Material.values()));
    }

    public void removeForbiddenItemFromList() {

    }

    public void removeForbiddenItemsFromPlayer() {

    }

    public void syncForbiddenItems() {
        FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
        List<String> sl = c.getStringList("Item.List.Forbidden");
        List<Material> newForbidden = new ArrayList<>();


        for (String s : sl) {
            newForbidden.add(Material.valueOf(s));

        }

        for (Material m : newForbidden) {
            if (!forbidden.contains(m)) {
                forbidden.add(m);
                System.out.println("Item " + m + " added to forbidden items!");
            }
        }
    }

    public boolean isForbiddenItem() {
        return false;
    }

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
        int max = Material.values().length;

        Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {

            int i = new Random().nextInt(max);
            Material m1 = material.get(i);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.getInventory().addItem(new ItemStack(m1));
            }

        }, 20 * 2, 20 * 5);

    }
}
