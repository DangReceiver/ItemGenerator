package de.cruelambition.oo;

import de.cruelambition.cmd.user.Spawn;
import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Afk {

	private final BukkitTask checker;

	public Afk() {
		checker = Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {
			PC pc = null;
			for (Player ap : Bukkit.getOnlinePlayers()) {
				pc = new PC(ap);


				if (isAfk(pc)) sendToSpawn(ap);
			}
		}, 10 * 20, 20 * 20);
	}

	public BukkitTask getChecker() {
		return checker;
	}


	// needs to be checked properly
	public void updateAfkLead(PC pc) {
		pc.setAfkTime(System.currentTimeMillis() - pc.getAfkLead());
	}

	// ^^
	public boolean isAfk(PC pc) {
		if (pc.getAfkTime() >= 9000 || pc.getAfkLead() - System.currentTimeMillis()  <= 9000) {

			setAfk(pc, true);
			return true;
		}

		return false;
	}

	public void setAfk(PC pc, boolean isAfk) {
		pc.setAfk(isAfk);
	}

	public void sendToSpawn(Player p) {
		Spawn.sendToSpawn(p);
	}
}
