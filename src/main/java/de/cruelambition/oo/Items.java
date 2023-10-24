package de.cruelambition.oo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Items {

    // cmd = CustomModelData
    private int cmd = 1, cmdd = 1, cmde = 1, cmdh = 1;

    public static List<ItemStack> ITEMS = new ArrayList<>();

    public Items() {
        List<ItemStack> l = new ArrayList<>();

        ItemStack mini_jetpack = newItem("§6Mini Jetpack", "Click to be boosted " +
                "in the air every time you click"),
                sound = newItem("§eSound", "Click to produce a sound"),
                eraser = newItem("§cEraser", "click to remove a set of blocks"),
                crate = newHeadItem("§cItem Crate", "Click to roll the lucky wheel"),
                banana = newEdibleItem("§eBanana", "§e§oBanana!");

        l.add(mini_jetpack);
        l.add(sound);
        l.add(eraser);
        l.add(crate);
        l.add(banana);

        ITEMS.addAll(l);

//		for (Player ap : Bukkit.getOnlinePlayers()) for (ItemStack is : l) ap.getInventory().addItem(is);
    }

    public ItemStack newItem(String name, String lore) {
        ItemStack customItem = new ItemStack(Material.PAPER);

        IB.name(customItem, name);
        IB.lore(customItem, lore);
        IB.cmd(customItem, cmd);

        cmd++;
        return customItem;
    }

    public ItemStack newDamageableItem(String name, String lore) {
        ItemStack customItem = new ItemStack(Material.IRON_HOE);

        IB.name(customItem, name);
        IB.lore(customItem, lore);
        IB.cmd(customItem, cmdd);

        cmdd++;
        return customItem;
    }

    public ItemStack newEdibleItem(String name, String lore) {
        ItemStack customItem = new ItemStack(Material.APPLE);

        IB.name(customItem, name);
        IB.lore(customItem, lore);
        IB.cmd(customItem, cmde);

        cmde++;
        return customItem;
    }

    public ItemStack newHeadItem(String name, String lore) {
        ItemStack customItem = new ItemStack(Material.PLAYER_HEAD);

        IB.name(customItem, name);
        IB.lore(customItem, lore);
        IB.cmd(customItem, cmdh);

        cmdh++;
        return customItem;
    }

    public ItemStack newPotionItem(String name, String lore) {
        ItemStack customItem = new ItemStack(Material.POTION);

        IB.name(customItem, name);
        IB.lore(customItem, lore);
        IB.cmd(customItem, cmdh);

        cmdh++;
        return customItem;
    }

    public ItemStack newDisc(String name, String lore) {
        ItemStack customItem = new ItemStack(Material.MUSIC_DISC_CAT);

        IB.name(customItem, name);
        IB.lore(customItem, lore);
        IB.cmd(customItem, cmdh);

        cmdh++;
        return customItem;
    }
}
