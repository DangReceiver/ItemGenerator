package de.cruelambition.listener.function;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.oo.Sb;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChange implements Listener {

	@EventHandler
	public void handle(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		Sb.updateWorldSlot(p);

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(),
				() -> Sb.updateWorldSlot(p), 3);
	}
}
