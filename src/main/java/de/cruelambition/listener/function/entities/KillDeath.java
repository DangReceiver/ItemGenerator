package de.cruelambition.listener.function.entities;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.PC;
import de.cruelambition.oo.Sb;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillDeath implements Listener {

	@EventHandler
	public void handle(PlayerDeathEvent e) {
		Player p = e.getPlayer();

		PC pc = new PC(p);
		pc.increaseDeaths();
		pc.savePCon();

		Sb.updateDeathSlot(new Lang(p));
	}

	@EventHandler
	public void handle(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player p)) return;
		if (!(e.getEntity() instanceof Monster)) return;

		PC pc = new PC(p);
		pc.increaseKills();


		pc.savePCon();

		Sb.updateKillSlot(new Lang(p));
	}
}
