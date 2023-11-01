package de.cruelambition.listener.essential;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Respawn implements Listener {

	@EventHandler
	public void handle(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,
				Integer.MAX_VALUE, 0, false, false, false));
	}
}
