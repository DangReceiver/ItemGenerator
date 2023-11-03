package de.cruelambition.listener.function.items;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

public class ItemDrop implements Listener {

	@EventHandler
	public void handle(ItemSpawnEvent e) {
		Item en = e.getEntity();
		CreatureSpawnEvent.SpawnReason spr = en.getEntitySpawnReason();

		en.setPickupDelay(21);

		if (spr == CreatureSpawnEvent.SpawnReason.CUSTOM) {
			Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(),
					en::remove, 120 * 20);
		} else if (spr == CreatureSpawnEvent.SpawnReason.DEFAULT) {
			en.setPickupDelay(14);
		}
	}
}
