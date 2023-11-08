package de.cruelambition.cmd.user;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.PC;
import de.cruelambition.worlds.SpawnWorld;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Player p;
		Lang l = new Lang(null);

		if (sen instanceof Player) {
			p = (Player) sen;
			l.setPlayer(p);

		} else {
			sen.sendMessage(Lang.PRE + Lang.getMessage(null, "not_a_player"));
			return false;
		}

		sendToSpawn(p);
		return false;
	}

	public static void sendToSpawn(Player p) {
		Location loc = SpawnWorld.getSafeSpawnLocation();

		PC pc = new PC(p);
		if (p.getWorld() == Bukkit.getWorld("world")) {
			pc.setLogoutLocation(p.getLocation());
			pc.savePCon();
		}

		p.setGameMode(GameMode.ADVENTURE);
		p.teleport(loc);
	}
}
