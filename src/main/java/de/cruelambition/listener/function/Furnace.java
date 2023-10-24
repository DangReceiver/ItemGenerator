package de.cruelambition.listener.function;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.inventory.*;

public class Furnace implements Listener {

    @EventHandler
    public void handle(FurnaceStartSmeltEvent e) {
        for (Player ap : Bukkit.getOnlinePlayers()) {

            ap.sendMessage("Recipe: " + e.getRecipe());
            ap.sendMessage("Total Cook Time: " + e.getTotalCookTime());

            ap.sendMessage("Block (type): " + e.getBlock().getType());
            ap.sendMessage("Source: " + e.getSource().getType());
        }

    }

    @EventHandler
    public void handle(FurnaceSmeltEvent e) {
        for (Player ap : Bukkit.getOnlinePlayers()) {

            ap.sendMessage("Recipe: " + e.getRecipe());
            ap.sendMessage("Result: " + e.getResult());

            ap.sendMessage("Block (type): " + e.getBlock().getType());
            ap.sendMessage("Source: " + e.getSource());
        }
    }

    @EventHandler
    public void handle(FurnaceBurnEvent e) {
        for (Player ap : Bukkit.getOnlinePlayers()) {

            ap.sendMessage("Block (type): " + e.getBlock().getType());
            ap.sendMessage("Burn Time: " + e.getBurnTime());
            ap.sendMessage("Fuel: " + e.getFuel());
        }
    }

    @EventHandler
    public void handle(FurnaceExtractEvent e) {
        for (Player ap : Bukkit.getOnlinePlayers()) {

            ap.sendMessage("Player: " + e.getPlayer().getName());
            ap.sendMessage("Block (type): " + e.getBlock().getType());

            ap.sendMessage("Amount: " + e.getItemAmount());
            ap.sendMessage("Type: " + e.getItemType());
            ap.sendMessage("Exp: " + e.getExpToDrop());

        }
    }

    @EventHandler
    public void handle(BrewingStartEvent e) {
        for (Player ap : Bukkit.getOnlinePlayers()) {
            ap.sendMessage("Block (type): " + e.getBlock().getType());
            ap.sendMessage("Source: " +e.getSource());
            ap.sendMessage("Brew Time: " +e.getTotalBrewTime());
        }
    }

    @EventHandler
    public void handle(BrewingStandFuelEvent e) {
        for (Player ap : Bukkit.getOnlinePlayers()) {
            ap.sendMessage("Fuel: " + e.getFuel());
            ap.sendMessage("Block (type): " + e.getBlock().getType());
            ap.sendMessage("Fuel Power: " + e.getFuelPower());
        }
    }
}
