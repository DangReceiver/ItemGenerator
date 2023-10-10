package de.cruelambition.listener.function;

import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

public class ItemDrop implements Listener {

	@EventHandler
	public void handle(ItemSpawnEvent e) {
		e.getEntity().setPickupDelay(25);

		if (e.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
			Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(),
					() -> e.getEntity().remove(), 20 * 20);
		}
	}

}
