package de.cruelambition.oo;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sb {

	public static void setAllScoreBoards() {
		for (Player ap : Bukkit.getOnlinePlayers()) setDefaultScoreBoard(ap);
	}

	public static void setDefaultScoreBoard(Player p) {
		Lang l = new Lang(p);
		String chat = Lang.CHAT;
		if (chat.length() >= 17) chat = "§cInvalid String";

		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective("  " + chat, "abc", chat + "  ");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		String ti = l.getString("sb_time").length() <= 16 ? l.getString("sb_time") : "§8-";
		String w = l.getString("sb_world").length() <= 16 ? l.getString("sb_world") : "§8-";

		String dea = l.getString("sb_deaths").length() <= 16 ? l.getString("sb_deaths") : "§8-";
		String ki = l.getString("sb_kills").length() <= 16 ? l.getString("sb_kills") : "§8-";

		Score space0 = obj.getScore("§0 "),
				space1 = obj.getScore(" §0"),
				space2 = obj.getScore(" §0 "),
				space3 = obj.getScore(" §0  "),
				space4 = obj.getScore("  §0 "),

				time = obj.getScore("  §8» §6" + ti),
				world = obj.getScore("  §8» §3" + w),
				deaths = obj.getScore("  §8» §4" + dea),
				kills = obj.getScore("  §8» §5" + ki);

		space0.setScore(17);
		space1.setScore(14);
		space2.setScore(11);
		space3.setScore(8);
		space4.setScore(5);

		time.setScore(16);
		world.setScore(13);
		deaths.setScore(10);
		kills.setScore(7);

		Team dTime = sb.registerNewTeam("dTime");
		Team dWorld = sb.registerNewTeam("dWorld");
		Team dDeaths = sb.registerNewTeam("dDeaths");
		Team dKills = sb.registerNewTeam("dKills");

		dTime.addEntry("" + ChatColor.AQUA);
		dWorld.addEntry("" + ChatColor.BLACK);
		dDeaths.addEntry("" + ChatColor.BLUE);
		dKills.addEntry("" + ChatColor.DARK_AQUA);

		PC pc = new PC(p);

		dTime.setPrefix("    §e➥ ☞ " + String.format("%s", convertTime(System.currentTimeMillis())));
		dWorld.setPrefix("    §b➥ \u06E9 " + p.getWorld().getName());
		dDeaths.setPrefix("    §c➥ ⚔ " + String.format("%s", pc.getDeaths()));
		dKills.setPrefix("    §d➥ ❤ " + String.format("%s", pc.getKills()));

		obj.getScore("" + ChatColor.AQUA).setScore(15);
		obj.getScore("" + ChatColor.BLACK).setScore(12);
		obj.getScore("" + ChatColor.BLUE).setScore(9);
		obj.getScore("" + ChatColor.DARK_AQUA).setScore(6);

		p.setScoreboard(sb);
//		updateTime(p);
	}

	public static void unsetAllScoreboards() {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective(Lang.PRE.replace(": ", ""),
				"abc", Lang.PRE.replace(": ", "") + "      ");

		for (Player ap : Bukkit.getOnlinePlayers()) ap.setScoreboard(sb);
	}

	public static void unsetScoreboard(Player p) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective(" ", "abc", " ");
		p.setScoreboard(sb);
	}

	public static void updateFullScoreBoard(Player p) {
		Scoreboard sb = p.getScoreboard();
		PC pc = new PC(p);

		sb.getTeam("dTime").setPrefix("    §e➥ ☞ " + String.format("%s", convertTime(System.currentTimeMillis())));
		sb.getTeam("dWorld").setPrefix("    §b➥ \u06E9 " + p.getWorld().getName());
		sb.getTeam("dDeaths").setPrefix("    §c➥ ⚔ " + String.format("%s", pc.getDeaths()));
		sb.getTeam("dKills").setPrefix("    §d➥ ❤ " + String.format("%s", pc.getKills()));
	}

	public static void updateTime(Player p) {
		Scoreboard sb = p.getScoreboard();
		sb.getTeam("dTime").setPrefix("    §e➥ ☞ " + String.format("%s", convertTime(System.currentTimeMillis())));
	}

	public static void updateWorld(Player p) {
		Scoreboard sb = p.getScoreboard();
		sb.getTeam("dWorld").setPrefix("    §b➥ \u06E9 " + p.getWorld().getName());
	}

	public static void updateDeaths(Player p) {
		Scoreboard sb = p.getScoreboard();

		PC pc = new PC(p);
		sb.getTeam("dDeaths").setPrefix("    §c➥ ⚔ " + String.format("%s", pc.getDeaths()));
	}

	public static void updateKills(Player p) {
		Scoreboard sb = p.getScoreboard();

		PC pc = new PC(p);
		sb.getTeam("dKills").setPrefix("    §d➥ ❤ " + String.format("%s", pc.getKills()));
	}

	public static String convertTime(long time) {
		Date date = new Date(time);
		Format format = new SimpleDateFormat("HH:mm:ss");
		return format.format(date);
	}

	public static void timeLoop() {
		Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {
			for (Player ap : Bukkit.getOnlinePlayers()) Sb.updateTime(ap);
		}, 20 * 5, 20 * 2);
	}
}