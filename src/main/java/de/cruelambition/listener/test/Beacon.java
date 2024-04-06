package de.cruelambition.listener.test;

import io.papermc.paper.event.block.BeaconActivatedEvent;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Beacon implements Listener {

	//	@EventHandler
	public void handle(BeaconActivatedEvent e) {
		PotionEffect pe = e.getBeacon().getPrimaryEffect();

		if (pe.getType() == PotionEffectType.DAMAGE_RESISTANCE) {
			pe.getAmplifier();
			pe.getDuration();

			e.getBeacon().setPrimaryEffect(PotionEffectType.GLOWING);
		}
	}
}
