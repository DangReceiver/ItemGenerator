package de.cruelambition.listener.function.player;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.utils.Utils;
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

		Utils.particleOffsetDelayed(t, Particle.HEART,
				14, 0, 0.85, 4);
		Utils.particleOffsetDelayed(p, Particle.HEART,
				10, 0, 0.6, 5);

		t.sendTitle("§4💞❤", String.format(new Lang(t).getString("x_loves_you"),
				p.getName()), 6, 180, 60);
	}
}
