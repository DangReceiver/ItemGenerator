package de.cruelambition.listener.function.entities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SneakGlow implements Listener {

	public static PotionEffect GLOW = new PotionEffect(PotionEffectType.GLOWING,
			Integer.MAX_VALUE, 1, false, false, false);

	@EventHandler
	public void handle(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();

		if (p.isSneaking() && p.hasPotionEffect(PotionEffectType.GLOWING))
			p.removePotionEffect(PotionEffectType.GLOWING);
		else p.addPotionEffect(GLOW);
	}
}
