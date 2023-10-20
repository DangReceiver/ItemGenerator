package de.cruelambition.oo;

import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public class BossBar {

	public static void timeToBroadcastBar(Player p, String name) {
		org.bukkit.boss.BossBar b = Bukkit.createBossBar(name, BarColor.BLUE, BarStyle.SOLID);

		b.setProgress(0);
		b.setVisible(true);
		b.addPlayer(p);
		b.addFlag(BarFlag.CREATE_FOG);

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> loadBossBar(b), 30);
	}

	public static void loadBossBar(org.bukkit.boss.BossBar b) {
		if (b.getProgress() + 0.0325 >= 1) {
			dischargeBossBar(b);
			return;
		}

		b.setProgress(b.getProgress() + 0.0325);
		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> loadBossBar(b), 3);
	}

	public static void dischargeBossBar(org.bukkit.boss.BossBar b) {
		if (b.getProgress() - 0.005 <= 0) {
			b.removeAll();
			return;
		}

		if (b.getProgress() <= 0.92 && b.getProgress() >= 0.88) {
			b.setColor(BarColor.GREEN);
		} else if (b.getProgress() <= 0.34 && b.getProgress() >= 0.30) {
			b.setColor(BarColor.YELLOW);
		} else if (b.getProgress() <= 0.15 && b.getProgress() >= 0.10) {
			b.setColor(BarColor.RED);
		} else if (b.getProgress() <= 0.10 && b.getProgress() >= 0.06) {
			b.setColor(BarColor.PURPLE);
		}

		b.setProgress(b.getProgress() - 0.005);
		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> dischargeBossBar(b), 2);
	}
}
