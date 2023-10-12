package de.cruelambition.cmd.moderation;

import de.cruelambition.language.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Arms implements CommandExecutor {

	public static String PERMISSION = "ItemGenerator.ArmorStand";

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

		if (!p.hasPermission(PERMISSION)) {
			sen.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"), PERMISSION));
			return false;
		}

		for (Entity ne : p.getNearbyEntities(1.25, 1.25, 1.25)) {
			if (ne.getType() != EntityType.ARMOR_STAND) continue;

			((ArmorStand) ne).setArms(true);
			((ArmorStand) ne).setBasePlate(false);
			((ArmorStand) ne).setMaxHealth(100);
			((ArmorStand) ne).setHealth(100);
		}

		p.sendMessage(Lang.PRE + l.getString("done"));

		return false;
	}
}
