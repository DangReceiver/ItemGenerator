package de.cruelambition.listener.function.blocks;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.inventory.*;

public class Furnace implements Listener {

    private ConsoleCommandSender cs = Bukkit.getConsoleSender();

    @EventHandler
    public void handle(FurnaceStartSmeltEvent e) {
        cs.sendMessage("§80: " + "Recipe: " + e.getRecipe() + " || "
                + "Total Cook Time: " + e.getTotalCookTime() + " || "
                + "Block (type): " + e.getBlock().getType() + " || "
                + "Source: " + e.getSource().getType());
    }

    @EventHandler
    public void handle(FurnaceSmeltEvent e) {
        cs.sendMessage("§11: " + "Recipe: " + e.getRecipe() + " || "
                + "Result: " + e.getResult() + " || "
                + "Block (type): " + e.getBlock().getType() + " || "
                + "Source: " + e.getSource());
    }

    @EventHandler
    public void handle(FurnaceBurnEvent e) {
        cs.sendMessage("§22: " + "Block (type): " + e.getBlock().getType() + " || "
                + "Burn Time: " + e.getBurnTime() + " || "
                + "Fuel: " + e.getFuel());
    }

    @EventHandler
    public void handle(FurnaceExtractEvent e) {
        cs.sendMessage("§33: " + "Player: " + e.getPlayer().getName() + " || "
                + "Block (type): " + e.getBlock().getType() + " || "
                + "Amount: " + e.getItemAmount() + " || "
                + "Type: " + e.getItemType() + " || "
                + "Exp: " + e.getExpToDrop());
    }

    @EventHandler
    public void handle(BrewingStartEvent e) {
        cs.sendMessage("§44: " + "Block (type): " + e.getBlock().getType() + " || "
                + "Source: " + e.getSource() + " || "
                + "Brew Time: " + e.getTotalBrewTime());
    }

    @EventHandler
    public void handle(BrewingStandFuelEvent e) {
        cs.sendMessage("§55: " + "Fuel: " + e.getFuel() + " || "
                + "Block (type): " + e.getBlock().getType() + " || "
                + "Fuel Power: " + e.getFuelPower());
    }
}
