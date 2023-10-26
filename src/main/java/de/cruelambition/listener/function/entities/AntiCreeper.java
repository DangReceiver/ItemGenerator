package de.cruelambition.listener.function.entities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.List;

public class AntiCreeper implements Listener {

	@EventHandler
	public void handle(EntitySpawnEvent e) {
		if (e.getEntityType() != EntityType.CREEPER) return;

		for (Entity en : e.getEntity().getNearbyEntities(8, 8, 8)) {

			if (en.getType() != EntityType.ARMOR_STAND) continue;
			if (en.getCustomName().equalsIgnoreCase("ANTICREEPER")) e.setCancelled(true);
		}
	}
}
