package de.cruelambition.listener.function.entities;

import de.cruelambition.oo.Recipes;
import io.papermc.paper.event.block.BeaconActivatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Brute implements Listener {

	@EventHandler
	public void handle(EntityBreedEvent e) {
		e.setExperience(e.getExperience() + new Random().nextInt(5));
	}

	@EventHandler
	public void handle(EntityDropItemEvent e) {
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		if (e.getItemDrop().getItemStack().getType() != Material.EGG) {
			cs.sendMessage("not an egg");
			return;
		}

		Random r = new Random();
		if (r.nextInt(4) == 0) return;
		e.setCancelled(true);

		int i = r.nextInt(5);
		Recipes.delayedSpawning(e.getEntity().getLocation().add(0, 0.75, 0), EntityType.THROWN_EXP_BOTTLE, 3, i, 0);
	}
}
