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

	private static int timeEntry = 0, worldEntry = 0, deathEntry = 0, killEntry = 0;

	public static final int TIME_SLOT = 16, WORLD_SLOT = 13, DEATH_SLOT = 10, KILL_SLOT = 7;

	public static void setAllScoreBoards() {
		for (Player ap : Bukkit.getOnlinePlayers()) setDefaultScoreBoard(ap);
	}

	public static void scoreboardLoop() {
		Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {

			if (timeEntry >= 1) timeEntry = -1;
			timeEntry++;

			if (worldEntry >= 1) worldEntry = -1;
			worldEntry++;

			if (deathEntry >= 1) deathEntry = -1;
			deathEntry++;

			if (killEntry >= 1) killEntry = -1;
			killEntry++;

			for (Player ap : Bukkit.getOnlinePlayers()) {
				updateFullScoreBoard(ap);
//				ap.sendMessage("SB Updated!" + " || " + timeEntry);
			}

		}, 8 * 20, 6 * 20);
	}

	public static void timeLoop() {
		Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {
			for (Player ap : Bukkit.getOnlinePlayers()) Sb.updateTimeSlot(ap);
		}, 8 * 20, 20);
	}


	public static Objective getObjective(Scoreboard sb) {
		Objective obj = sb.getObjective("  " + Lang.CHAT);

		if (obj == null) obj = sb.getObjective("abc");
		if (obj == null) obj = sb.getObjective(Lang.CHAT + "  ");
		return obj;
	}

	public static void setScore(Player p, char ch, String path, Scoreboard sb, int score) {
		Lang l = new Lang(p);

		getObjective(sb).unregister();
		if (getObjective(sb) != null) return;

		sb.registerNewObjective("  " + Lang.CHAT, "abc", Lang.CHAT + "  ").getScore("  §8» §"
				+ ch + (l.getString(path).length() <= 16 ? l.getString(path) : "§8-")).setScore(score);
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

		time.setScore(TIME_SLOT);
		world.setScore(WORLD_SLOT);
		deaths.setScore(DEATH_SLOT);
		kills.setScore(KILL_SLOT);

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
		dWorld.setPrefix("    §b➥ ۩ " + p.getWorld().getName());
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
		updateTimeSlot(p);
		updateWorldSlot(p);
		updateKillSlot(p);
		updateDeathSlot(p);

//		sb.getTeam("dTime").setPrefix("    §e➥ ☞ " + String.format("%s", convertTime(System.currentTimeMillis())));
//		sb.getTeam("dWorld").setPrefix("    §b➥ \u06E9 " + p.getWorld().getName());
//		sb.getTeam("dDeaths").setPrefix("    §c➥ ⚔ " + String.format("%s", pc.getDeaths()));
//		sb.getTeam("dKills").setPrefix("    §d➥ ❤ " + String.format("%s", pc.getKills()));
	}

	public static void updateTimeSlot(Player p) {
		if (timeEntry == 0) updateTime(p);
		else if (timeEntry == 1) updateWorldTime(p);
		else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));

		updateTimeHeader(p);
	}

	public static void updateTimeHeader(Player p) {
		if (timeEntry == 0) {
			p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR_TEAM_AQUA);
			setScore(p, '3', "sb_time", p.getScoreboard(), TIME_SLOT);

		} else if (timeEntry == 1) {
			p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR_TEAM_AQUA);
			setScore(p, 'b', "sb_world_time", p.getScoreboard(), TIME_SLOT);

		} else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));
	}

	public static void updateTime(Player p) {
		Scoreboard sb = p.getScoreboard();
		sb.getTeam("dTime").setPrefix("    §e➥ ☞ " + String.format("%s", convertTime(System.currentTimeMillis())));
	}

	public static void updateWorldTime(Player p) {
		Scoreboard sb = p.getScoreboard();
		sb.getTeam("dTime").setPrefix("    §6➥ ☞ " + String.format("%s", convertTime(p.getWorld().getTime())));
	}

	public static void updateWorldSlot(Player p) {
		if (worldEntry == 0) updateWorld(p);
		else if (worldEntry == 1) updateNearbyEntities(p);
		else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));

		updateWorldHeader(p);
	}

	public static void updateWorldHeader(Player p) {
		if (worldEntry == 0) {
			p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR_TEAM_BLACK);
			setScore(p, '6', "sb_world", p.getScoreboard(), WORLD_SLOT);

		} else if (worldEntry == 1) {
			p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR_TEAM_BLACK);
			setScore(p, 'e', "sb_nearby_entities", p.getScoreboard(), WORLD_SLOT);
		} else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));
	}

	public static void updateWorld(Player p) {
		Scoreboard sb = p.getScoreboard();
		sb.getTeam("dWorld").setPrefix("    §b➥ ۩ " + p.getWorld().getName());
	}

	public static void updateNearbyEntities(Player p) {
		Scoreboard sb = p.getScoreboard();
		sb.getTeam("dWorld").setPrefix("    §3➥ ۩ " + p.getLocation().getNearbyEntities(16, 16, 16).size());
	}

	public static void updateDeathSlot(Player p) {
		if (deathEntry == 0) updateDeaths(p);
		else if (deathEntry == 1) updateXp(p);
		else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));

		updateDeathHeader(p);
	}

	public static void updateDeathHeader(Player p) {
		if (deathEntry == 0) {
			p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR_TEAM_BLUE);
			setScore(p, '4', "sb_deaths", p.getScoreboard(), DEATH_SLOT);
		} else if (deathEntry == 1) {
			p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR_TEAM_BLUE);
			setScore(p, 'c', "sb_xp", p.getScoreboard(), DEATH_SLOT);
		} else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));
	}

	public static void updateDeaths(Player p) {
		Scoreboard sb = p.getScoreboard();

		PC pc = new PC(p);
		sb.getTeam("dDeaths").setPrefix("    §c➥ ⚔ " + String.format("%s", pc.getDeaths()));
	}

	public static void updateXp(Player p) {
		Scoreboard sb = p.getScoreboard();

		PC pc = new PC(p);
		sb.getTeam("dDeaths").setPrefix("    §4➥ ⚔ " + String.format("%s", pc.getDeaths()));
	}

	public static void updateKillSlot(Player p) {
		if (killEntry == 0) updateKills(p);
		else if (killEntry == 1) updateKillLevel(p);
		else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));

		updateKillHeader(p);
	}

	public static void updateKillHeader(Player p) {
		if (killEntry == 0) {
			p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR_TEAM_DARK_AQUA);
			setScore(p, 'c', "sb_kills", p.getScoreboard(), KILL_SLOT);

		} else if (killEntry == 1) {
			p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR_TEAM_DARK_AQUA);
			setScore(p, '4', "sb_kill_level", p.getScoreboard(), KILL_SLOT);
		} else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));
	}

	public static void updateKills(Player p) {
		Scoreboard sb = p.getScoreboard();

		PC pc = new PC(p);
		sb.getTeam("dKills").setPrefix("    §d➥ ❤ " + String.format("%s", pc.getKills()));
	}

	public static void updateKillLevel(Player p) {
		Scoreboard sb = p.getScoreboard();
		sb.getTeam("dKills").setPrefix("    §5➥ ❤ " + String.format("%s", p.getTotalExperience()));
	}

	public static String convertTime(long time) {
		Date date = new Date(time);
		Format format = new SimpleDateFormat("HH:mm:ss");
		return format.format(date);
	}
}