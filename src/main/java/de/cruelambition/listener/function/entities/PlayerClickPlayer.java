package de.cruelambition.listener.function.entities;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.Utils;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerClickPlayer implements Listener {

	@EventHandler
	public void handle(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if (!(e.getRightClicked() instanceof Player t)) return;

		Utils.particleOffset(t.getLocation().add(0, 0.5, 0), Particle.HEART, 6, 0.6);
		Utils.particleOffset(p.getLocation().add(0, 0.5, 0), Particle.HEART, 8, 0.85);

		t.sendTitle("§4💞❤", String.format(new Lang(t).getString("x_loves_you"), p.getName()), 8, 200, 50);
	}
}
