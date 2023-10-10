package de.cruelambition.oo;

import de.cruelambition.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Sb {

	public static void setAllScoreBoards() {
		for (Player ap : Bukkit.getOnlinePlayers()) setDefaultScoreBoard(ap);
	}

	public static void setDefaultScoreBoard(Player p) {
		Lang l = new Lang(p);
		if (Lang.PRE.length() >= 17) Lang.PRE = "§cInvalid String";

		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective("  " + Lang.PRE, "abc", Lang.PRE + "  ");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		String ti = l.getString("sb_time").length() <= 16 ? l.getString("sb_time") : "null";
		String w = l.getString("sb_world").length() <= 16 ? l.getString("sb_world") : "null";

		String dea = l.getString("sb_deaths").length() <= 16 ? l.getString("sb_deaths") : "null";
		String ki = l.getString("sb_kills").length() <= 16 ? l.getString("sb_kills") : "null";

		Score space0 = obj.getScore("§0 "),
				space1 = obj.getScore(" §0"),
				space2 = obj.getScore(" §0 "),
				space3 = obj.getScore(" §0  "),
				space4 = obj.getScore("  §0 "),

				time = obj.getScore("  §8\u00BB §a" + ti),
				world = obj.getScore("  §8\u00BB §3" + w),
				deaths = obj.getScore("  §8\u00BB §5" + dea),
				kills = obj.getScore("  §8\u00BB §6" + ki);

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

		dTime.setPrefix("    §e➥ \u26C3 " + "%s");
		dWorld.setPrefix("    §b➥ \u06E9 " + p.getWorld().getName());
		dDeaths.setPrefix("    §2➥ \u2692 §c " + "%s");
		dKills.setPrefix("    §d➥ \u2692 §e" + "%s");

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
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		sb.getTeam("dTime").setPrefix("    §e➥ \u26C3 " + "%s");
		sb.getTeam("dWorld").setPrefix("    §b➥ \u06E9 " + p.getWorld().getName());
		sb.getTeam("dDeaths").setPrefix("    §2➥ \u2692 §c " + "%s");
		sb.getTeam("dKills").setPrefix("    §d➥ \u2692 §e" + "%s");
	}

	public static void updateTime(Player p) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		sb.getTeam("dTime").setPrefix("    §e➥ \u26C3 " + "%s");
	}

	public static void updateWorld(Player p) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		sb.getTeam("dWorld").setPrefix("    §b➥ \u06E9 " + p.getWorld().getName());
	}
}