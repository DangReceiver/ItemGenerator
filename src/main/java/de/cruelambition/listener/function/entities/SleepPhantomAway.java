package de.cruelambition.listener.function.entities;

import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class SleepPhantomAway implements Listener {

	@EventHandler
	public void handle(PlayerBedLeaveEvent e) {
		World w = e.getPlayer().getWorld();
		e.getPlayer().sendMessage(w.getTime() + "");
		e.getPlayer().getStatistic(Statistic.TIME_SINCE_REST);

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {
			e.getPlayer().sendMessage(w.getTime() + "");
			if (w.getTime() >= 40) for (Player ap : Bukkit.getOnlinePlayers())
				ap.setStatistic(Statistic.TIME_SINCE_REST, 0);
		}, 2 * 20);
	}
}
