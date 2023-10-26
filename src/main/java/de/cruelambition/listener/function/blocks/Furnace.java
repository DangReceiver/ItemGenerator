package de.cruelambition.listener.function.blocks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.inventory.*;

public class Furnace implements Listener {

	@EventHandler
	public void handle(FurnaceStartSmeltEvent e) {
		Bukkit.getConsoleSender().sendMessage("0");

		Bukkit.getConsoleSender().sendMessage("Recipe: " + e.getRecipe());
		Bukkit.getConsoleSender().sendMessage("Total Cook Time: " + e.getTotalCookTime());

		Bukkit.getConsoleSender().sendMessage("Block (type): " + e.getBlock().getType());
		Bukkit.getConsoleSender().sendMessage("Source: " + e.getSource().getType());
	}

	@EventHandler
	public void handle(FurnaceSmeltEvent e) {
		Bukkit.getConsoleSender().sendMessage("1");

		Bukkit.getConsoleSender().sendMessage("Recipe: " + e.getRecipe());
		Bukkit.getConsoleSender().sendMessage("Result: " + e.getResult());

		Bukkit.getConsoleSender().sendMessage("Block (type): " + e.getBlock().getType());
		Bukkit.getConsoleSender().sendMessage("Source: " + e.getSource());
	}

	@EventHandler
	public void handle(FurnaceBurnEvent e) {
		Bukkit.getConsoleSender().sendMessage("2");

		Bukkit.getConsoleSender().sendMessage("Block (type): " + e.getBlock().getType());
		Bukkit.getConsoleSender().sendMessage("Burn Time: " + e.getBurnTime());
		Bukkit.getConsoleSender().sendMessage("Fuel: " + e.getFuel());
	}

	@EventHandler
	public void handle(FurnaceExtractEvent e) {
		Bukkit.getConsoleSender().sendMessage("3");

		Bukkit.getConsoleSender().sendMessage("Player: " + e.getPlayer().getName());
		Bukkit.getConsoleSender().sendMessage("Block (type): " + e.getBlock().getType());

		Bukkit.getConsoleSender().sendMessage("Amount: " + e.getItemAmount());
		Bukkit.getConsoleSender().sendMessage("Type: " + e.getItemType());
		Bukkit.getConsoleSender().sendMessage("Exp: " + e.getExpToDrop());
	}

	@EventHandler
	public void handle(BrewingStartEvent e) {
		Bukkit.getConsoleSender().sendMessage("4");

		Bukkit.getConsoleSender().sendMessage("Block (type): " + e.getBlock().getType());
		Bukkit.getConsoleSender().sendMessage("Source: " + e.getSource());
		Bukkit.getConsoleSender().sendMessage("Brew Time: " + e.getTotalBrewTime());
	}

	@EventHandler
	public void handle(BrewingStandFuelEvent e) {
		Bukkit.getConsoleSender().sendMessage("5");

		Bukkit.getConsoleSender().sendMessage("Fuel: " + e.getFuel());
		Bukkit.getConsoleSender().sendMessage("Block (type): " + e.getBlock().getType());
		Bukkit.getConsoleSender().sendMessage("Fuel Power: " + e.getFuelPower());
	}
}
