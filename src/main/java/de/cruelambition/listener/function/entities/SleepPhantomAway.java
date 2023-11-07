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
		Player p = e.getPlayer();
		World w = p.getWorld();

//		p.sendMessage(w.getTime() + "");
//		p.sendMessage("tsr:" + p.getStatistic(Statistic.TIME_SINCE_REST));

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {
			if (w.getTime() < 40) return;
			for (Player ap : Bukkit.getOnlinePlayers()) {

//				p.sendMessage(w.getTime() + "");
				ap.setStatistic(Statistic.TIME_SINCE_REST, 0);
//				ap.sendMessage("tsr: " + ap.getStatistic(Statistic.TIME_SINCE_REST));
			}
		}, 2 * 20);
	}
}
