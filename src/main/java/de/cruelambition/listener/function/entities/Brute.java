package de.cruelambition.listener.function.entities;

import de.cruelambition.oo.Recipes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDropItemEvent;

import java.util.Random;

public class Brute implements Listener {

	@EventHandler
	public void handle(EntityBreedEvent e) {
		e.setExperience(e.getExperience() + new Random().nextInt(5));
		if(new Random().nextInt(6) == 0) e.setCancelled(true);
	}

	@EventHandler
	public void handle(EntityDropItemEvent e) {
		if (e.getItemDrop().getItemStack().getType() != Material.EGG) return;

		Random r = new Random();
		if (r.nextInt(5) == 0) return;
		e.setCancelled(true);

		int i = r.nextInt(5);
		Recipes.delayedSpawning(e.getEntity().getLocation().add(0, 0.75, 0), EntityType.THROWN_EXP_BOTTLE, 3, i, 0);
	}
}
