package de.cruelambition.listener.essential;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;

public class PreConnect implements Listener {

	@EventHandler
	public void handle(PlayerPreLoginEvent e) {
		if (e.getResult() != PlayerPreLoginEvent.Result.KICK_OTHER) return;

		if (e.getKickMessage().contains("Connection Throttled"))
			e.setResult(PlayerPreLoginEvent.Result.ALLOWED);
	}
}
