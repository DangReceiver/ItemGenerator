package de.cruelambition.oo;

import de.cruelambition.cmd.user.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class Afk implements Listener {

//	@EventHandler
	public void handle(PlayerKickEvent e) {
		if (e.getCause() != PlayerKickEvent.Cause.IDLING) return;

		Player p = e.getPlayer();
		p.sendMessage(e.getCause() + "0");

		e.setCancelled(true);
		p.resetIdleDuration();

		if (p.getWorld() != Bukkit.getWorld("Spawn")) {
			p.sendMessage(e.getCause() + "1");
			Spawn.sendToSpawn(p);
		}
	}

}
